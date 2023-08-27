package br.com.meu_primeiro_aplicativo.repository

import br.com.meu_primeiro_aplicativo.model.CepResponse
import br.com.meu_primeiro_aplicativo.network.CepService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CepRepository {

  fun fetchCep(cep: String): Call<CepResponse> {
    val retrofit =
      Retrofit.Builder().baseUrl(HOST).addConverterFactory(GsonConverterFactory.create()).build()
    val cepService = retrofit.create(CepService::class.java)

    return cepService.getCepResponse(cep)
  }

  companion object {
    private const val HOST = "https://viacep.com.br/"
  }
}