package br.com.meu_primeiro_aplicativo.network

import br.com.meu_primeiro_aplicativo.model.CepResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CepService {

  @GET("ws/{cep}/json/")
  fun getCep(@Path("cep") cep: String): Call<CepResponse>
}