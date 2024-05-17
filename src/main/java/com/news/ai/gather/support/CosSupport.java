package com.news.ai.gather.support;

import cn.hutool.core.lang.UUID;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.CosHttpRequest;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.internal.CosServiceRequest;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.retry.RetryPolicy;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * @Created by zhiwei on 2022/3/27.
 */
@Component("cosSupport")
@Scope("singleton")
public class CosSupport {

    private static final Logger log = LoggerFactory.getLogger(CosSupport.class);
    @Value("${cos.secretId}")
    private String secretId;
    @Value("${cos.secretKey}")
    private String secretKey;
    @Value("${cos.region}")
    private String regionStr;
    @Value("${cos.bucket}")
    private String bucket;
    @Value("${cos.baseUrl}")
    private String baseUrl;

    private COSClient cosClient;

    @PostConstruct
    private void init() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(regionStr);
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setMaxErrorRetry(4);
//        clientConfig.setRetryPolicy(new OnlyIOExceptionRetryPolicy());
        cosClient = new COSClient(cred, clientConfig);
    }

    public void delCos(String fileKey) {
        if (fileKey != null && fileKey.contains("https://")) {
            fileKey = fileKey.replace(baseUrl, "");
        }
        if (cosClient == null) {
            init();
        }
        cosClient.deleteObject(bucket, fileKey);
    }

    public ObjectMetadata download(String cosFileName, String saveFileUrl) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, cosFileName);
        File downFile = new File(saveFileUrl);
        getObjectRequest = new GetObjectRequest(bucket, cosFileName);
        return cosClient.getObject(getObjectRequest, downFile);
    }

    public PutObjectResult upFile(String uploadFileName, InputStream io) {
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadFileName, io, metadata);
        return cosClient.putObject(putObjectRequest);
    }

    @Async
    public void upFile(String filePath, String remoteUrl) {
        try {
            int lastIndexOf = remoteUrl.lastIndexOf(".");
            String suffix = remoteUrl.substring(lastIndexOf);
            URL url = new URL(remoteUrl);
            try (InputStream in = url.openStream()) {
                String uploadFileName = UUID.fastUUID().toString(true) + suffix;
                log.debug("uploadFileName:{}", uploadFileName);
                PutObjectResult result = this.upFile(filePath + "/" + uploadFileName, in);
                log.debug("result:{}", result.toString());
            }
        } catch (Exception e) {
            log.error("upFile error", e);
        }
    }

    public String getNameByRemote(String filePath, String remoteUrl) {
        int lastIndexOf = remoteUrl.lastIndexOf(".");
        String suffix = remoteUrl.substring(lastIndexOf);
        String uploadFileName = UUID.fastUUID().toString(true) + suffix;
        return baseUrl + filePath + "/" + uploadFileName;
    }

    public PutObjectResult upFile(String uploadFileName, File file) {
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadFileName, file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 自定义重试的策略
     * 如果是客户端的 IOException 异常则重试，否则不重试
     */
    public static class OnlyIOExceptionRetryPolicy extends RetryPolicy {
        @Override
        public <X extends CosServiceRequest> boolean shouldRetry(CosHttpRequest<X> request, HttpResponse response, Exception exception, int retryIndex) {
            //
            return exception.getCause() instanceof IOException;
        }
    }
}
