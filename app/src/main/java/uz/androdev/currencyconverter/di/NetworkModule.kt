package uz.androdev.currencyconverter.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.androdev.currencyconverter.BuildConfig
import uz.androdev.currencyconverter.data.remote.CurrencyService
import javax.inject.Singleton

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:45 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://nbu.uz/en/"

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Singleton
    @Provides
    fun provideGsonBuilder(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
        if (!BuildConfig.DEBUG) {
            return null
        }
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor?): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (httpLoggingInterceptor != null) {
                    addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        factory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(factory)
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)
}