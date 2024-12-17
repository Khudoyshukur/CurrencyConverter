package uz.androdev.currencyconverter.model.mapper

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.model.dto.CurrencyDto
import uz.androdev.currencyconverter.util.Constants.NBU_API_DATE_FORMAT
import uz.androdev.currencyconverter.util.Constants.NBU_CURRENCY_IMAGE_PATH_FORMAT

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun CurrencyDto.toCurrency(): Currency {
    val formatter = DateTimeFormatter.ofPattern(NBU_API_DATE_FORMAT)
    val date = try {
        LocalDate.parse(date!!.take(NBU_API_DATE_FORMAT.length), formatter)
    } catch (e: Exception) {
        LocalDate.now()
    }

    val currencyImagePath = String.format(
        NBU_CURRENCY_IMAGE_PATH_FORMAT, code
    )

    return Currency(
        title = title ?: "",
        code = code ?: "",
        cbPrice = cbPrice?.toFloatOrNull() ?: 0f,
        buyPrice = nbuBuyPrice?.toFloatOrNull(),
        sellPrice = nbuCellPrice?.toFloatOrNull(),
        date = date,
        currencyImagePath = currencyImagePath
    )
}