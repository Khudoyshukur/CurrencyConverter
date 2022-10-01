package uz.androdev.currencyconverter.domain.failure

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 10:55 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed interface SaveConversionResultUseCaseFailure : UseCaseFailure {
    object NoInternet : SaveConversionResultUseCaseFailure
}