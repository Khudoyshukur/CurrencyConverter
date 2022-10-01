package uz.androdev.currencyconverter.data.repository.impl

import uz.androdev.currencyconverter.data.datasource.RemoteCurrencyDataSource
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException
import uz.androdev.currencyconverter.data.repository.CurrencyRepository
import uz.androdev.currencyconverter.model.Currency
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:51 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CurrencyRepositoryImpl @Inject constructor(
    private val remoteCurrencyDataSource: RemoteCurrencyDataSource
) : CurrencyRepository {

    @kotlin.jvm.Throws(NoInternetConnectionException::class, ServerErrorException::class)
    override suspend fun getCurrencies(): List<Currency> {
        return remoteCurrencyDataSource.getCurrencies()
    }
}