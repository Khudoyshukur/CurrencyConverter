package uz.androdev.currencyconverter.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.currencyconverter.data.datasource.RemoteCurrencyDataSource
import uz.androdev.currencyconverter.data.datasource.impl.RemoteCurrencyDataSourceImpl
import javax.inject.Singleton

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 4:03 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface RemoteCurrencyDataSourceModule {
    @Singleton
    @Binds
    fun bindRemoteCurrencyDataSource(impl: RemoteCurrencyDataSourceImpl): RemoteCurrencyDataSource
}