package com.alogorithms.smart.nofications.network;

import com.alogorithms.smart.nofications.Constants;
import com.alogorithms.smart.nofications.model.Alert;
import com.alogorithms.smart.nofications.model.FileObj;
import com.alogorithms.smart.nofications.network.contract.NetworkManager;
import com.alogorithms.smart.nofications.network.service.NetworkService;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class NetworkManagerImpl implements NetworkManager {

    private final NetworkService networkService;

    public NetworkManagerImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public Alert postData(Alert alert) throws Exception {

        Response<Alert> response = networkService.postData(alert).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }
        return response.body();
    }

    @Override
    public FileObj uploadImage(File image) throws IOException{
        String imageName = image.getName();
        String extention = imageName.substring(imageName.lastIndexOf(".") + 1);
        String mediaTypeString = Constants.CONTENT_TYPE_JPEG;
        switch (extention){
            case "png":
                mediaTypeString = Constants.CONTENT_TYPE_PNG;
                break;
            case "gif":
                mediaTypeString = Constants.CONTENT_TYPE_GIF;
                break;
        }

        MediaType mediaType = MediaType.parse(mediaTypeString);

        RequestBody requestBody = RequestBody.create(mediaType, image);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", image.getName(), requestBody);
        Response<FileObj> response = networkService.postFile(fileToUpload).execute();
        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }
        return response.body();
    }
}
