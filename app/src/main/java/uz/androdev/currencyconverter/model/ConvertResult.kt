package uz.androdev.currencyconverter.model

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 9:02 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class ConvertResult(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val result: String = ""
) {
    companion object {
        val DIFF_UTIL = object : ItemCallback<ConvertResult>() {
            override fun areItemsTheSame(oldItem: ConvertResult, newItem: ConvertResult): Boolean {
                return oldItem.dateTime == newItem.dateTime
            }

            override fun areContentsTheSame(
                oldItem: ConvertResult,
                newItem: ConvertResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}