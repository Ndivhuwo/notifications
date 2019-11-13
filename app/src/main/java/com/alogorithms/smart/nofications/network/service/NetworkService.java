package com.alogorithms.smart.nofications.network.service;

import com.alogorithms.smart.nofications.model.Alert;
import com.alogorithms.smart.nofications.model.FileObj;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface NetworkService {
    @PUT("alert/open/new")
    Call<Alert> postData(@Body Alert alert);

    @Multipart
    @POST("file/open/new")
    Call<FileObj> postFile(@Part MultipartBody.Part file);
}
