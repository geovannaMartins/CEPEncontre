package br.com.meu_primeiro_aplicativo.viewmodel

import androidx.lifecycle.ViewModel
import br.com.meu_primeiro_aplicativo.model.CepResponse
import br.com.meu_primeiro_aplicativo.repository.CepRepository
import retrofit2.Call
import retrofit2.Response

class CepViewModel : ViewModel() {

  private var repository : CepRepository = CepRepository();

  //Callback: é uma interface para receber a resposta de uma chamada a uma API Rest
  //Throwable: é um erro genérico para lidar com erros não mapeados

  fun getCep(cep : String, onSucess: (Response<CepResponse>) -> Unit, onError: (Throwable) -> Unit ) {

    val call = repository.fetchCep(cep);

    call.enqueue(object : retrofit2.Callback<CepResponse> {
      override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {
        onSucess(response);
      }

      override fun onFailure(call: Call<CepResponse>, t: Throwable) {
        onError(t);
      }

    })
  }

}