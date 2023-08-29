package br.com.meu_primeiro_aplicativo.repository

import br.com.meu_primeiro_aplicativo.model.CepResponse
import br.com.meu_primeiro_aplicativo.network.CepService
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CepRepository {

  private var retrofit : Retrofit = Retrofit.Builder().baseUrl("https://viacep.com.br/")
    .addConverterFactory(GsonConverterFactory.create()).build()

  fun fetchCep(cep: String) : Call<CepResponse> {
    val cepService = retrofit.create(CepService::class.java)
    return cepService.getCep(cep)
  }
}