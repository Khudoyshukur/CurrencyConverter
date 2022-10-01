package uz.androdev.currencyconverter.model.mapper

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import uz.androdev.currencyconverter.model.Currency
import uz.androdev.currencyconverter.model.dto.CurrencyDto
import uz.androdev.currencyconverter.util.Constants.NBU_API_DATE_FORMAT

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun CurrencyDto.toCurrency(): Currency {
    val formatter = DateTimeFormatter.ofPattern(NBU_API_DATE_FORMAT)
    val date = LocalDate.parse(date, formatter)

    return Currency(
        title = title,
        code = code,
        cbPrice = cbPrice.toFloat(),
        buyPrice = nbuBuyPrice.toFloatOrNull(),
        sellPrice = nbuCellPrice.toFloatOrNull(),
        date = date
    )
}