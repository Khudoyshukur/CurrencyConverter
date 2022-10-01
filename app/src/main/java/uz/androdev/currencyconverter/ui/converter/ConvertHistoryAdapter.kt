package uz.androdev.currencyconverter.ui.converter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.format.DateTimeFormatter
import uz.androdev.currencyconverter.R
import uz.androdev.currencyconverter.databinding.ItemConvertHistoryBinding
import uz.androdev.currencyconverter.model.ConvertResult
import uz.androdev.currencyconverter.util.Constants.SEARCH_HISTORY_DATE_TIME_FORMAT

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 9:04 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class ConvertHistoryAdapter :
    ListAdapter<ConvertResult, ConvertHistoryAdapter.ViewHolder>(ConvertResult.DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemConvertHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchHistory = getItem(position) ?: return
        holder.bind(searchHistory)
    }

    class ViewHolder(private val binding: ItemConvertHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val resources by lazy { binding.root.resources }

        fun bind(convertResult: ConvertResult) = with(convertResult) {
            val dateTimeFormatter = DateTimeFormatter.ofPattern(SEARCH_HISTORY_DATE_TIME_FORMAT)
            binding.tvDateTime.text = dateTime.format(dateTimeFormatter)
            binding.tvConvertResult.text = String.format(
                resources.getString(R.string.search_result), result
            )
        }
    }
}