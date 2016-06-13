package com.yimilan.tiku.service;

import java.io.File;
import java.io.IOException;

/**
 * Created by xufeng on 15/4/7.
 */
public interface YunStorageService {

    Boolean putObject(String bucketName, String fileName, byte[] datas);
    Boolean putObject(String bucketName, String fileName, File file) throws IOException;

    Boolean deleteObject(String bucketName, String fileName);
}
