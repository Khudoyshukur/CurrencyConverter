package uz.androdev.currencyconverter.app

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 3:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltAndroidApp
class CurrencyConverterApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.database.setPersistenceEnabled(true)
    }
}