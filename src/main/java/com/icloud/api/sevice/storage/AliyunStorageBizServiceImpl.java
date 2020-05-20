package com.icloud.api.sevice.storage;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.icloud.config.global.MyPropertitys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 */
@Service
public class AliyunStorageBizServiceImpl implements StorageBizService {

    @Autowired
    private MyPropertitys myPropertitys;

//    private String endpoint = myPropertitys.getOss().getEndpoint();
//    private String bucket = myPropertitys.getOss().getBucket();
//    private String baseUrl = myPropertitys.getOss().getBasekUrl();

    private OSSClient ossClient;

    @Override
    public String upload(String fileName, InputStream is, long contentLength, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);
        objectMetadata.setContentType(contentType);
        PutObjectRequest putObjectRequest = new PutObjectRequest(myPropertitys.getOss().getBucket(), fileName, is, objectMetadata);
        ossClient =  new OSSClient(myPropertitys.getOss().getEndpoint(), myPropertitys.getOss().getAccessId(),
                myPropertitys.getOss().getAccessKey());
        ossClient.putObject(putObjectRequest);
        return myPropertitys.getOss().getBasekUrl() + fileName;
    }
}
