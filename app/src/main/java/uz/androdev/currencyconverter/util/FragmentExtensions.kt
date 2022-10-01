package uz.androdev.currencyconverter.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment


/**
 * Created by: androdev
 * Date: 01-10-2022
 * Time: 9:57 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun Fragment.toast(@StringRes textId: Int) {
    Toast.makeText(requireContext(), textId, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideKeyboard() {
    view?.let {
        val imm: InputMethodManager = requireContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}