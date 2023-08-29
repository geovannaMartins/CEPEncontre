package br.com.meu_primeiro_aplicativo.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
      callCepService(cep)
      hideKeyboard()
    } else {
      Toast.makeText(
        this,
        "O CEP precisa conter 8 dígitos",
        Toast.LENGTH_LONG
      ).show()
    }
  }

  private fun callCepService(cep: String) {
    viewModel.getCep(
      cep = cep,
      onSuccess = { cepResponse ->
        loading.visibility = View.GONE
        if (cepResponse.isSuccessful) {
          setVisibilityResult(true)
          val address = cepResponse.body()

          if (address?.erro != true) {
            tvDescriptionResult.text = String.format(
              resources.getString(R.string.mpa_address),
              address?.cep,
              address?.logradouro,
              address?.complemento,
              address?.bairro,
              address?.localidade,
              address?.uf,
              address?.ddd
            )
          } else {
            setVisibilityResult(false)
            Toast.makeText(
              this,
              resources.getString(R.string.mpa_error_cep),
              Toast.LENGTH_LONG
            ).show()
          }
          etCep.text.clear()
        } else {
          setVisibilityResult(false)
          Toast.makeText(
            this,
            resources.getString(R.string.mpa_error_response),
            Toast.LENGTH_SHORT
          ).show()
        }
      },
      onError = {
        loading.visibility = View.GONE
        setVisibilityResult(false)
        Toast.makeText(
          this,
          resources.getString(R.string.mpa_error_comunication_api),
          Toast.LENGTH_LONG
        ).show()
      }
    )
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