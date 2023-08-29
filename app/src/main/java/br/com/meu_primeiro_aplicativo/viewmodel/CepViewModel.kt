package br.com.meu_primeiro_aplicativo.viewmodel

import androidx.lifecycle.ViewModel
import br.com.meu_primeiro_aplicativo.model.CepResponse
import br.com.meu_primeiro_aplicativo.repository.CepRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Callback: é uma interface para receber a resposta de uma chamada a uma API REST

class CepViewModel : ViewModel() {
  private var repository : CepRepository = CepRepository()

  fun getCep(cep: String,
             onSuccess: (Response<CepResponse>) -> Unit,
             onError: (Throwable) -> Unit
  ) {
    val call = repository.fetchCep(cep)

    call.enqueue(object : Callback<CepResponse> {
      override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
        onSuccess(response)
      }

      override fun onFailure(call: Call<CepResponse>, t: Throwable) {
        onError(t)
      }

    })
  }
}