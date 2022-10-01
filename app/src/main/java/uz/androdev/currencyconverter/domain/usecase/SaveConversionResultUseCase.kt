package uz.androdev.currencyconverter.domain.usecase

import uz.androdev.currencyconverter.data.exception.NoInternetConnectionException
import uz.androdev.currencyconverter.data.repository.ConversionHistoryRepository
import uz.androdev.currencyconverter.domain.failure.SaveConversionResultUseCaseFailure
import uz.androdev.currencyconverter.domain.response.UseCaseResponse
import uz.androdev.currencyconverter.model.ConvertResult
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 10:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class SaveConversionResultUseCase @Inject constructor(
    private val conversionHistoryRepository: ConversionHistoryRepository
) {
    suspend operator fun invoke(
        convertResult: ConvertResult
    ): UseCaseResponse<Unit, SaveConversionResultUseCaseFailure> {
        return try {
            UseCaseResponse.Success(conversionHistoryRepository.saveConversionResult(convertResult))
        } catch (e: NoInternetConnectionException) {
            UseCaseResponse.Failure(SaveConversionResultUseCaseFailure.NoInternet)
        }
    }
}