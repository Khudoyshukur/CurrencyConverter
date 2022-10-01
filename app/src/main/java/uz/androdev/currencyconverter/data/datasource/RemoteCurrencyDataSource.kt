package uz.androdev.currencyconverter.data.datasource

import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException
import uz.androdev.currencyconverter.model.Currency

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:55 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface RemoteCurrencyDataSource {

    @kotlin.jvm.Throws(NoInternetConnectionException::class, ServerErrorException::class)
    suspend fun getCurrencies(): List<Currency>
}