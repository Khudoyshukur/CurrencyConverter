package uz.androdev.currencyconverter.data.datasource.impl

import uz.androdev.currencyconverter.data.datasource.RemoteCurrencyDataSource
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException
import uz.androdev.currencyconverter.data.remote.CurrencyService
import uz.androdev.currencyconverter.data.remote.handle
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.model.mapper.toCurrency
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:55 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RemoteCurrencyDataSourceImpl @Inject constructor(
    private val currencyService: CurrencyService
) : RemoteCurrencyDataSource {

    @kotlin.jvm.Throws(NoInternetConnectionException::class, ServerErrorException::class)
    override suspend fun getCurrencies(): List<Currency> {
        val currencies = handle { currencyService.getCurrencies() }
        return currencies.map { it.toCurrency() }
    }
}