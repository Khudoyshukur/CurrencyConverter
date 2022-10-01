package uz.androdev.currencyconverter.domain.usecase

import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException
import uz.androdev.currencyconverter.data.repository.CurrencyRepository
import uz.androdev.currencyconverter.domain.failure.GetCurrenciesUseCaseFailure
import uz.androdev.currencyconverter.domain.response.UseCaseResponse
import uz.androdev.currencyconverter.model.Currency
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): UseCaseResponse<List<Currency>, GetCurrenciesUseCaseFailure> {
        return try {
            UseCaseResponse.Success(currencyRepository.getCurrencies())
        } catch (e: NoInternetConnectionException) {
            UseCaseResponse.Failure(GetCurrenciesUseCaseFailure.NoInternet)
        } catch (e: ServerErrorException) {
            UseCaseResponse.Failure(GetCurrenciesUseCaseFailure.NoInternet)
        }
    }
}

