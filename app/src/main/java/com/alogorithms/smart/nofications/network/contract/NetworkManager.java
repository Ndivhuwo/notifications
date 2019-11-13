package com.alogorithms.smart.nofications.network.contract;

import com.alogorithms.smart.nofications.model.Alert;
import com.alogorithms.smart.nofications.model.FileObj;

import java.io.File;

public interface NetworkManager {
    Alert postData(Alert alert) throws Exception;

    FileObj uploadImage(File image) throws Exception;
}
