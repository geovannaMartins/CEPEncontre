package br.com.meu_primeiro_aplicativo.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import br.com.meu_primeiro_aplicativo.R
import br.com.meu_primeiro_aplicativo.viewmodel.CepViewModel

class CepActivity : AppCompatActivity() {
  private lateinit var etCep: EditText
  private lateinit var tvTitleResult: TextView
  private lateinit var tvDescriptionResult: TextView
  private lateinit var btConsult: AppCompatButton
  private lateinit var viewModel: CepViewModel
  private lateinit var loading: View

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    setContentView(R.layout.mpa_activity_main)
    supportActionBar?.hide()
    viewModel = ViewModelProvider(this)[CepViewModel::class.java]
    setupView()
    setListeners()
    setListenerCep()
  }

  private fun setupView() {
    etCep = findViewById(R.id.mpaCep)
    tvTitleResult = findViewById(R.id.mpaTitleResult)
    tvDescriptionResult = findViewById(R.id.mpaDescriptionResult)
    btConsult = findViewById(R.id.mpaBtConsulte)
    loading = findViewById(R.id.mpaLoading)
  }

  private fun setListeners() {
    etCep.setOnClickListener {
      getConfigCepAndCall()
    }

    btConsult.setOnClickListener {
      getConfigCepAndCall()
    }
  }

  private fun setListenerCep() {
    var previousCep = ""
    etCep.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        previousCep = p0.toString()
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // empty
      }

      override fun afterTextChanged(s: Editable?) {
        val currentCep = s.toString()

        if (currentCep == previousCep) {
          return
        }

        val formattedText = currentCep.replace("-", "").replace(".", "")
        val formattedCep = StringBuilder(formattedText)
        if (formattedCep.length > 5) {
          formattedCep.insert(5, "-")
        }
        etCep.setText(formattedCep.toString())
        etCep.setSelection(formattedCep.length)
      }
    })
  }

  private fun getConfigCepAndCall() {
    val cep = etCep.text.toString().replace(".", "").replace("-", "")
    if (cep.isNotEmpty() && cep.length == 8) {
      loading.visibility = View.VISIBLE
      hideKeyboard()
    }
  }

  private fun setVisibilityResult(responseSuccess: Boolean) {
    if (responseSuccess) {
      tvTitleResult.visibility = View.VISIBLE
      tvDescriptionResult.visibility = View.VISIBLE
    } else {
      tvTitleResult.visibility = View.GONE
      tvDescriptionResult.visibility = View.GONE
    }
  }

  private fun hideKeyboard() {
    val inputManager =
      baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
  }
}