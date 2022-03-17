package com.chidi.mystockapp.core.di

import android.content.Context
import androidx.room.Room
import com.chidi.mystockapp.data.local.MyStockAppDatabase
import com.chidi.mystockapp.data.local.dao.CompanyDao
import com.chidi.mystockapp.data.local.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, MyStockAppDatabase::class.java, "My_Stock_App_Database.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCompanyDao(db: MyStockAppDatabase) = db.companyDao()

    @Singleton
    @Provides
    fun provideLocalRepository(companyDao: CompanyDao) = LocalRepository(companyDao)
}
