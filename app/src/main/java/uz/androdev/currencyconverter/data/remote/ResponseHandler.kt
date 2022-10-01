package uz.androdev.currencyconverter.data.remote

import retrofit2.Response
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:29 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@kotlin.jvm.Throws(NoInternetConnectionException::class, ServerErrorException::class)
suspend fun <T : Any> handle(request: suspend () -> Response<T>): T {
    val response = request()
    if (!response.isSuccessful) {
        throw ServerErrorException("Server error occurred!")
    }

    return try {
        response.body()!!
    } catch (e: Exception) {
        throw NoInternetConnectionException("Fetch failed. Check internet connection!")
    }
}