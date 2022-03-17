package com.chidi.mystockapp.data.remote

import com.chidi.mystockapp.domain.Candle
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CandleService {
    @GET("stock/candle")
    fun getCandles(
        @Query("symbol") symbol: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long,
    ) : Observable<Candle>
}