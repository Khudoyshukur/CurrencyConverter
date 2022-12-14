package uz.androdev.currencyconverter.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.currencyconverter.domain.failure.GetCurrenciesUseCaseFailure
import uz.androdev.currencyconverter.domain.response.UseCaseResponse
import uz.androdev.currencyconverter.domain.usecase.GetConversionHistoryUseCase
import uz.androdev.currencyconverter.domain.usecase.GetCurrenciesUseCase
import uz.androdev.currencyconverter.domain.usecase.SaveConversionResultUseCase
import uz.androdev.currencyconverter.model.ConvertResult
import uz.androdev.currencyconverter.model.Currency
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 9:09 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val getConversionHistoryUseCase: GetConversionHistoryUseCase,
    private val saveConversionResultUseCase: SaveConversionResultUseCase
) : ViewModel() {
    private val currenciesState = MutableStateFlow<List<Currency>>(emptyList())
    private val loadingCurrenciesState = MutableStateFlow(false)
    private val failureToLoadCurrenciesState = MutableStateFlow<GetCurrenciesUseCaseFailure?>(null)
    private val currentConvertResultState = MutableStateFlow<ConvertResult?>(null)

    private val _message = Channel<Message>()
    val message get() = _message.receiveAsFlow()

    val uiState: StateFlow<UiState> = combine(
        currenciesState,
        loadingCurrenciesState,
        failureToLoadCurrenciesState,
        currentConvertResultState,
        getConversionHistoryUseCase()
    ) { currencies, loadingCurrencies, failureToLoadCurrencies, currentConvertResult, history ->
        UiState(
            loadingCurrencies = loadingCurrencies,
            failureToLoadCurrencies = failureToLoadCurrencies,
            currencies = currencies,
            currentConvertResult = currentConvertResult,
            convertHistory = history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = UiState()
    )

    init {
        viewModelScope.launch { loadCurrencies() }
    }

    fun processAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.ProcessConversion -> {
                    processConversion(action.conversion)
                }
                Action.ReloadCurrencies -> {
                    loadCurrencies()
                }
            }
        }
    }

    private suspend fun processConversion(conversion: Conversion) = with(conversion) {
        val sourcePriceText = "$price ${sourceCurrency.code}"

        val result = if (sourceCurrency == targetCurrency) {
            ConvertResult(
                result = "$sourcePriceText = $sourcePriceText"
            )
        } else {
            val targetPrice = (price * targetCurrency.cbPrice) * sourceCurrency.cbPrice
            ConvertResult(
                result = "$sourcePriceText = $targetPrice ${targetCurrency.code}"
            )
        }
        currentConvertResultState.emit(result)

        when (saveConversionResultUseCase(result)) {
            is UseCaseResponse.Failure -> {
                _message.send(Message.ConvertResultNotSaved)
            }
            is UseCaseResponse.Success -> {}
        }
    }

    private suspend fun loadCurrencies() {
        if (loadingCurrenciesState.value) return

        loadingCurrenciesState.emit(true)
        failureToLoadCurrenciesState.emit(null)

        when (val result = getCurrenciesUseCase()) {
            is UseCaseResponse.Failure -> {
                failureToLoadCurrenciesState.emit(result.failure)
            }
            is UseCaseResponse.Success -> {
                currenciesState.emit(result.data)
            }
        }

        loadingCurrenciesState.emit(false)
    }
}

sealed interface Action {
    object ReloadCurrencies : Action
    data class ProcessConversion(val conversion: Conversion) : Action
}

sealed interface Message {
    object ConvertResultNotSaved : Message
}

data class UiState(
    val loadingCurrencies: Boolean = false,
    val failureToLoadCurrencies: GetCurrenciesUseCaseFailure? = null,
    val currentConvertResult: ConvertResult? = null,
    val convertHistory: List<ConvertResult> = emptyList(),
    val currencies: List<Currency> = emptyList()
)

data class Conversion(
    val price: Float,
    val sourceCurrency: Currency,
    val targetCurrency: Currency
)