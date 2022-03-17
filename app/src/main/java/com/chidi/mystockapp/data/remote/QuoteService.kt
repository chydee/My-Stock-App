package com.chidi.mystockapp.data.remote

import com.chidi.mystockapp.domain.Quote
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface QuoteService {
    @GET("stock/{symbol}/quote")
    fun getQuote(@Path("symbol") ticker: String): Observable<Quote>
}