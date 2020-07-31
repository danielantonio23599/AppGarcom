package com.daniel.appgarcom.sync;




import com.daniel.appgarcom.modelo.beans.Endereco;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepAPI {
   // "ws/01001000/json/"
   @GET("{CEP}/json/")
   Call<Endereco> getEnderecoByCEP(@Path("CEP") String CEP);

}
