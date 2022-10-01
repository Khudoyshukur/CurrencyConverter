package uz.androdev.currencyconverter.ui.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.currencyconverter.domain.failure.GetCurrenciesUseCaseFailure
import uz.androdev.currencyconverter.domain.response.UseCaseResponse
import uz.androdev.currencyconverter.domain.usecase.GetCurrenciesUseCase
import uz.androdev.currencyconverter.model.Currency
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 5:58 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase
) : ViewModel() {
    private val queryState = MutableStateFlow("")
    private val currenciesState = MutableStateFlow<List<Currency>>(emptyList())
    private val loadingCurrenciesState = MutableStateFlow(false)
    private val failureToLoadState = MutableStateFlow<GetCurrenciesUseCaseFailure?>(null)

    val uiState: StateFlow<UiState> = combine(
        queryState, currenciesState, failureToLoadState, loadingCurrenciesState
    ) { query, currencies, failureToLoad, loadingCurrencies ->
        UiState(query = query,
            failureToLoad = failureToLoad,
            loadingCurrencies = loadingCurrencies,
            currencies = currencies.filter {
                it.title.contains(query)
            })
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
                is Action.ChangeQuery -> {
                    queryState.emit(action.query)
                }
                Action.ReloadCurrencies -> {
                    loadCurrencies()
                }
            }
        }
    }

    private suspend fun loadCurrencies() {
        if (loadingCurrenciesState.value) return

        loadingCurrenciesState.emit(true)
        failureToLoadState.emit(null)

        when (val result = getCurrenciesUseCase()) {
            is UseCaseResponse.Failure -> {
                failureToLoadState.emit(result.failure)
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
    data class ChangeQuery(val query: String) : Action
}

data class UiState(
    val loadingCurrencies: Boolean = false,
    val failureToLoad: GetCurrenciesUseCaseFailure? = null,
    val currencies: List<Currency> = emptyList(),
    val query: String = "",
)