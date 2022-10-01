package uz.androdev.currencyconverter.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.androdev.currencyconverter.data.repository.ConversionHistoryRepository
import uz.androdev.currencyconverter.model.ConvertResult
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 10:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetConversionHistoryUseCase @Inject constructor(
    private val conversionHistoryRepository: ConversionHistoryRepository
) {
    operator fun invoke(): Flow<List<ConvertResult>> {
        return conversionHistoryRepository.getConversionResults()
    }
}