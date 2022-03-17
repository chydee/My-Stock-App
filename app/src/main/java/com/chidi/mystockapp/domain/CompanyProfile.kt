package com.chidi.mystockapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_table")
data class CompanyProfile(
    @PrimaryKey val name: String,
    val logo: String,
    val ticker: String
)