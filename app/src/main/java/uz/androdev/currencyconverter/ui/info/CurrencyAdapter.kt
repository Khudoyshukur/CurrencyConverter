package uz.androdev.currencyconverter.ui.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.androdev.currencyconverter.R
import uz.androdev.currencyconverter.databinding.ItemCurrencyInfoBinding
import uz.androdev.currencyconverter.model.Currency

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 6:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CurrencyAdapter : ListAdapter<Currency, CurrencyAdapter.ViewHolder>(Currency.DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyInfoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = getItem(position) ?: return
        holder.bind(currency)
    }

    class ViewHolder(private val binding: ItemCurrencyInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val resources by lazy { binding.root.resources }

        fun bind(currency: Currency) = with(currency) {
            Glide.with(binding.root)
                .load(currencyImagePath)
                .into(binding.currencyImage)

            binding.currencyInfo.text = String.format(
                resources.getString(R.string.currency_info), title, code
            )
            binding.cbPrice.text = String.format(
                resources.getString(R.string.cb_price), cbPrice
            )
            binding.buyPrice.text = String.format(
                resources.getString(R.string.buy_price), buyPrice
            )
            binding.sellPrice.text = String.format(
                resources.getString(R.string.sell_price), sellPrice
            )
        }
    }
}