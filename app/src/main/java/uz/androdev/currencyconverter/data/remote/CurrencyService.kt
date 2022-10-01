package uz.androdev.currencyconverter.data.remote

import retrofit2.Response
import retrofit2.http.GET
import uz.androdev.currencyconverter.model.dto.CurrencyDto

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:47 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface CurrencyService {
    @GET("exchange-rates/json/")
    suspend fun getCurrencies(): Response<List<CurrencyDto>>
}