package uz.androdev.currencyconverter.model

import androidx.recyclerview.widget.DiffUtil
import org.threeten.bp.LocalDate

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class Currency(
    val title: String,
    val code: String,
    val cbPrice: Float,
    val buyPrice: Float?,
    val sellPrice: Float?,
    val date: LocalDate,
    val currencyImagePath: String
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Currency>() {
            override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
                return oldItem == newItem
            }
        }
    }
}