package com.chidi.mystockapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chidi.mystockapp.data.local.dao.CompanyDao
import com.chidi.mystockapp.domain.CompanyProfile

@Database(version = 1, entities = [CompanyProfile::class], exportSchema = false)
abstract class MyStockAppDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
}