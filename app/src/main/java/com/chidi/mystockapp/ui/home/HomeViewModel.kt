package com.chidi.mystockapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chidi.mystockapp.data.local.repository.LocalRepository
import com.chidi.mystockapp.data.remote.repository.RemoteDataSource
import com.chidi.mystockapp.domain.CompanyProfile
import com.chidi.mystockapp.domain.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: LocalRepository, private val remote: RemoteDataSource) : ViewModel() {

    private val disposeBag: CompositeDisposable = CompositeDisposable()

    //storage
    private val _stockMap: MutableLiveData<TreeMap<String, StockItem>> =
        MutableLiveData<TreeMap<String, StockItem>>()
    val stockMap: LiveData<TreeMap<String, StockItem>>
        get() = _stockMap

    private var companyList: List<CompanyProfile>? = repo.companyList


    fun configureStockMap() {
        if (companyList.isNullOrEmpty()) {
            viewModelScope.launch {
                getCompanies()
            }
        } else {
            viewModelScope.launch {
                createStocks()
            }
        }
    }

    private fun getCompanies() {
        remote.getCompanies().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ companies ->
                companyList = companies
                viewModelScope.launch {
                    createStocks()
                }

                insertCompaniesToDB()
            }, {

            }).let { disposeBag.add(it) }
    }

    private fun insertCompaniesToDB() {
        if (companyList != null) {
            for (company in companyList!!) {
                repo.insert(company)
            }
        }
    }

    private fun createStocks() {
        val map = TreeMap<String, StockItem>()
        if (companyList != null) {
            for (company in companyList!!) {
                remote.getQuotes(company.ticker)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ quote ->
                        val stockItem = StockItem(
                            company.name,
                            company.logo,
                            company.ticker,
                            quote.latestPrice,
                            quote.previousClose
                        )

                        map[company.ticker] = stockItem
                        _stockMap.postValue(map)
                    },
                        { throwable ->
                            Log.i(
                                Log.getStackTraceString(throwable),
                                throwable.message.toString()
                            )
                        }).let { disposeBag.add(it) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
        disposeBag.clear()
    }
}