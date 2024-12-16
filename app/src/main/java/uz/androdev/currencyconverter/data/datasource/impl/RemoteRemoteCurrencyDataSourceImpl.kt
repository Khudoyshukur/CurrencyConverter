package uz.androdev.currencyconverter.data.datasource.impl

import org.threeten.bp.LocalDate
import uz.androdev.currencyconverter.data.datasource.RemoteCurrencyDataSource
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.exception.ServerErrorException
import uz.androdev.currencyconverter.data.remote.CurrencyService
import uz.androdev.currencyconverter.data.remote.handle
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.model.mapper.toCurrency
import uz.androdev.currencyconverter.util.Constants.NBU_CURRENCY_IMAGE_PATH_FORMAT
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
        return currencies.map { it.toCurrency() } + listOf(
            Currency(
                title = "UZS",
                code = "UZS",
                cbPrice = 1f,
                buyPrice = 1f,
                sellPrice = 1f,
                date = LocalDate.now(),
                currencyImagePath = "https://upload.wikimedia.org/wikipedia/commons/0/0b/Flag_of_Uzbekistan.png"
            )
        )
    }
}