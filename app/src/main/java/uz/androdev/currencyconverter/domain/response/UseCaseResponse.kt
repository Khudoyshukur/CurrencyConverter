package uz.androdev.currencyconverter.domain.response

import uz.androdev.currencyconverter.domain.failure.UseCaseFailure

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:21 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed interface UseCaseResponse<out Response : Any, out Failure : Any> {
    data class Success<out Data : Any>(val data: Data) : UseCaseResponse<Data, Nothing>
    data class Failure<out Fail : UseCaseFailure>(val failure: Fail) :
        UseCaseResponse<Nothing, Fail>
}