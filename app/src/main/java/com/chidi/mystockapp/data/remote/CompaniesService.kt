package com.chidi.mystockapp.data.remote

import com.chidi.mystockapp.domain.CompanyProfile
import io.reactivex.Observable
import retrofit2.http.GET

interface CompaniesService {
    @GET("chydee/listing/main/listing.json")
    fun getCompanies(): Observable<List<CompanyProfile>>
}