package uz.androdev.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.androdev.currencyconverter.R
import uz.androdev.currencyconverter.domain.usecase.GetCurrenciesUseCase
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                println("TTTT ${getCurrenciesUseCase()}")
            } catch (e: Exception) {
                println("TTTT $e")
            }
        }
    }
}