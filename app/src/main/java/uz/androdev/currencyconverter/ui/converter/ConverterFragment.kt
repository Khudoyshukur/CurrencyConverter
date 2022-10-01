package uz.androdev.currencyconverter.ui.converter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.currencyconverter.R
import uz.androdev.currencyconverter.databinding.FragmentConverterBinding
import uz.androdev.currencyconverter.domain.failure.GetCurrenciesUseCaseFailure
import uz.androdev.currencyconverter.model.ConvertResult
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.ui.base.BaseFragment
import uz.androdev.currencyconverter.util.hideKeyboard
import uz.androdev.currencyconverter.util.toast

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 5:05 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@AndroidEntryPoint
class ConverterFragment :
    BaseFragment<FragmentConverterBinding>(FragmentConverterBinding::inflate) {

    private val viewModel: ConverterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindContent(
            uiState = viewModel.uiState,
            processAction = viewModel::processAction
        )
    }

    private fun FragmentConverterBinding.bindContent(
        uiState: StateFlow<UiState>,
        processAction: (Action) -> Unit
    ) {
        bindConvertContainer(
            currenciesFlow = uiState.map { it.currencies },
            currentConvertResult = uiState.map { it.currentConvertResult },
            onConvert = { processAction(Action.ProcessConversion(it)) }
        )

        bindConvertHistory(
            isVisible = uiState.map { !it.loadingCurrencies && it.failureToLoadCurrencies != null },
            convertHistory = uiState.map { it.convertHistory }
        )

        bindLoading(loading = uiState.map { it.loadingCurrencies })

        bindFailure(
            failure = uiState.map { it.failureToLoadCurrencies },
            reload = { processAction(Action.ReloadCurrencies) }
        )
    }

    private fun FragmentConverterBinding.bindConvertContainer(
        currenciesFlow: Flow<List<Currency>>,
        currentConvertResult: Flow<ConvertResult?>,
        onConvert: (Conversion) -> Unit,
    ) {
        var selectedSourceCurrency: Currency? = null
        var selectedTargetCurrency: Currency? = null

        btnConvert.setOnClickListener {
            val price = sourcePriceInputLayout.editText?.text?.toString()?.toFloatOrNull()

            if (price == null) {
                toast(R.string.enter_valid_price)
                return@setOnClickListener
            }

            if (selectedSourceCurrency == null) {
                toast(R.string.select_source_currency)
                return@setOnClickListener
            }

            if (selectedTargetCurrency == null) {
                toast(R.string.select_target_currency)
                return@setOnClickListener
            }

            hideKeyboard()
            onConvert(
                Conversion(
                    price = price,
                    sourceCurrency = selectedSourceCurrency!!,
                    targetCurrency = selectedTargetCurrency!!
                )
            )
        }

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            launch {
                currenciesFlow.distinctUntilChanged().collectLatest { currencies ->
                    sourceCurrencyInputLayout.isVisible = currencies.isNotEmpty()
                    targetCurrencyInputLayout.isVisible = currencies.isNotEmpty()
                    sourcePriceInputLayout.isVisible = currencies.isNotEmpty()
                    imgCompare.isVisible = currencies.isNotEmpty()
                    btnConvert.isVisible = currencies.isNotEmpty()
                    tvConvertResult.isVisible = currencies.isNotEmpty()

                    val txtCurrencies = currencies.map { it.code }

                    val sourceAdapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, txtCurrencies
                    )
                    sourceCurrencyAutocomplete.setAdapter(sourceAdapter)
                    sourceCurrencyAutocomplete.setOnItemClickListener { _, _, position, _ ->
                        selectedSourceCurrency = currencies.getOrNull(position)
                    }

                    val targetAdapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_list_item_1, txtCurrencies
                    )
                    targetCurrencyAutocomplete.setAdapter(targetAdapter)
                    targetCurrencyAutocomplete.setOnItemClickListener { _, _, position, _ ->
                        selectedTargetCurrency = currencies.getOrNull(position)
                    }
                }
            }

            launch {
                currentConvertResult.distinctUntilChanged().collectLatest {
                    tvConvertResult.text = if (it == null) {
                        ""
                    } else {
                        String.format(getString(R.string.search_result), it.result)
                    }
                }
            }
        }
    }

    private fun FragmentConverterBinding.bindConvertHistory(
        isVisible: Flow<Boolean>,
        convertHistory: Flow<List<ConvertResult>>
    ) {
        val adapter = ConvertHistoryAdapter()
        rvHistory.adapter = adapter

        val dividerDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvHistory.addItemDecoration(dividerDecoration)

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            launch {
                convertHistory.distinctUntilChanged().collectLatest(adapter::submitList)
            }

            launch {
                convertHistory.map { it.isNotEmpty() }
                    .combine(isVisible, Boolean::and).collectLatest {
                        rvHistory.isVisible = it
                        tvHistory.isVisible = it
                    }
            }
        }
    }

    private fun FragmentConverterBinding.bindLoading(loading: Flow<Boolean>) {
        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            loading.distinctUntilChanged().collect {
                progressBar.isVisible = it
            }
        }
    }

    private inline fun FragmentConverterBinding.bindFailure(
        failure: Flow<GetCurrenciesUseCaseFailure?>,
        crossinline reload: () -> Unit
    ) {
        btnReload.setOnClickListener { reload() }

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            failure.distinctUntilChanged().collect {
                errorContainer.isVisible = it != null
                errorMessage.text = when (it) {
                    GetCurrenciesUseCaseFailure.NoInternet -> getString(R.string.internet_error)
                    GetCurrenciesUseCaseFailure.ServerError -> getString(R.string.server_error)
                    null -> ""
                }
            }
        }
    }
}