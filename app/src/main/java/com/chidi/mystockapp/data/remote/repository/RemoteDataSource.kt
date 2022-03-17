package com.chidi.mystockapp.data.remote.repository

import com.chidi.mystockapp.data.remote.CandleService
import com.chidi.mystockapp.data.remote.CompaniesService
import com.chidi.mystockapp.data.remote.QuoteService
import com.chidi.mystockapp.domain.Candle
import com.chidi.mystockapp.domain.CompanyProfile
import com.chidi.mystockapp.domain.Quote
import io.reactivex.Observable
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val companiesService: CompaniesService, private val quoteService: QuoteService, private val candleService: CandleService) {
    fun getCompanies(): Observable<List<CompanyProfile>> {
        return companiesService.getCompanies()
    }

    fun getQuotes(ticker: String): Observable<Quote> {
        return quoteService.getQuote(ticker)
    }

    fun getCandles(symbol: String, resolution: String, from: Long, to: Long): Observable<Candle> {
        return candleService.getCandles(symbol, resolution, from, to)
    }
}
