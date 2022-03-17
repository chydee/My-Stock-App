package com.chidi.mystockapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chidi.mystockapp.data.remote.repository.RemoteDataSource
import com.chidi.mystockapp.domain.Candle
import com.chidi.mystockapp.domain.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) : ViewModel() {
    private val disposeBag: CompositeDisposable = CompositeDisposable()

    private val _candle = MutableLiveData<Candle>()
    val candle: LiveData<Candle> = _candle

    fun getCandles(stockItem: StockItem, resolution: String, from: Long) {
        remoteDataSource.getCandles(stockItem.ticker, resolution, from, System.currentTimeMillis() / 1000)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _candle.postValue(it)
            }, {
                it.printStackTrace()
            })
            .let { disposeBag.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
        disposeBag.clear()
    }
}