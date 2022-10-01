package uz.androdev.currencyconverter.domain.failure

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed interface GetCurrenciesUseCaseFailure : UseCaseFailure {
    object NoInternet : GetCurrenciesUseCaseFailure
    object ServerError : GetCurrenciesUseCaseFailure
}