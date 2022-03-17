package com.chidi.mystockapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chidi.mystockapp.domain.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _stockMap = MutableLiveData<TreeMap<String, StockItem>>()
    var stockMap: LiveData<TreeMap<String, StockItem>> = _stockMap

    fun setStockMap(map: TreeMap<String, StockItem>) {
        _stockMap.value = map
    }

}