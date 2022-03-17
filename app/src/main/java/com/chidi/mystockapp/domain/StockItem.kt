package com.chidi.mystockapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class StockItem(
    val name: String,
    val logo: String = "",
    val ticker: String,
    val latestPrice: Double,
    val previousClose: Double
) : Parcelable