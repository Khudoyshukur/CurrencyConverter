package uz.androdev.currencyconverter.model.dto

import com.google.gson.annotations.SerializedName

data class CurrencyDto(
    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("cb_price")
    val cbPrice: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("nbu_buy_price")
    val nbuBuyPrice: String,

    @field:SerializedName("nbu_cell_price")
    val nbuCellPrice: String
)
