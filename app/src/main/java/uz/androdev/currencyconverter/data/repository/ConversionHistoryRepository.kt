package uz.androdev.currencyconverter.data.repository

import kotlinx.coroutines.flow.Flow
import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.model.ConvertResult

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 10:22 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface ConversionHistoryRepository {
    @kotlin.jvm.Throws(NoInternetConnectionException::class)
    suspend fun saveConversionResult(convertResult: ConvertResult)

    fun getConversionResults(): Flow<List<ConvertResult>>
}