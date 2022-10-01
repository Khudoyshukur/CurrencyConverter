package uz.androdev.currencyconverter.ui.info

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import uz.androdev.currencyconverter.R
import uz.androdev.currencyconverter.databinding.FragmentInfoBinding
import uz.androdev.currencyconverter.domain.failure.GetCurrenciesUseCaseFailure
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.ui.base.BaseFragment

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 5:05 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(FragmentInfoBinding::inflate) {
    private val viewModel: InfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bindContent(
            uiState = viewModel.uiState,
            processAction = viewModel::processAction
        )
    }

    private fun FragmentInfoBinding.bindContent(
        uiState: StateFlow<UiState>,
        processAction: (Action) -> Unit
    ) {
        bindSearchInput(
            query = uiState.map { it.query },
            onQueryChanged = { processAction(Action.ChangeQuery(it)) }
        )

        bindCurrencies(currencies = uiState.map { it.currencies })

        bindFailure(
            failure = uiState.map { it.failureToLoad },
            reload = { processAction(Action.ReloadCurrencies) }
        )

        bindLoading(loading = uiState.map { it.loadingCurrencies })
    }

    private fun FragmentInfoBinding.bindSearchInput(
        query: Flow<String>,
        onQueryChanged: (String) -> Unit,
    ) = with(textInputLayout) {
        val textWatcher = editText?.doOnTextChanged { text, _, _, _ ->
            onQueryChanged(text?.toString() ?: return@doOnTextChanged)
        }
        runOnDestroy { editText?.removeTextChangedListener(textWatcher) }

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            query.filter { it != editText?.text.toString() }
                .collectLatest { editText?.setText(it) }
        }
    }

    private fun FragmentInfoBinding.bindCurrencies(currencies: Flow<List<Currency>>) {
        val adapter = CurrencyAdapter()
        rvCurrencies.adapter = adapter

        val dividerDecoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvCurrencies.addItemDecoration(dividerDecoration)

        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            currencies.distinctUntilChanged().collectLatest(adapter::submitList)
        }
    }

    private fun FragmentInfoBinding.bindLoading(loading: Flow<Boolean>) {
        repeatOnViewLifecycle(Lifecycle.State.STARTED) {
            loading.distinctUntilChanged().collect {
                progressBar.isVisible = it
            }
        }
    }

    private inline fun FragmentInfoBinding.bindFailure(
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