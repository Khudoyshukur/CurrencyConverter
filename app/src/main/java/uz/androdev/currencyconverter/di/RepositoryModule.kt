package uz.androdev.currencyconverter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.currencyconverter.data.repository.ConversionHistoryRepository
import uz.androdev.currencyconverter.data.repository.CurrencyRepository
import uz.androdev.currencyconverter.data.repository.impl.CurrencyRepositoryImpl
import uz.androdev.currencyconverter.data.repository.impl.FirebaseConversionHistoryRepositoryImpl
import javax.inject.Singleton

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:01 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyRepositoryModule {

    @Singleton
    @Binds
    fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface ConversionHistoryRepositoryModule {

    @Singleton
    @Binds
    fun bindConversionHistoryRepository(
        impl: FirebaseConversionHistoryRepositoryImpl
    ): ConversionHistoryRepository
}