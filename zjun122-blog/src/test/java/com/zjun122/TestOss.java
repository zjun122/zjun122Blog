package com.zjun122;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
public class TestOss {

    @Value("${oss.accessKey}")
    private String accessKey;
    @Value("${oss.secretKey}")
    private String secretKey;
    @Value("${oss.bucket}")
    private String bucket;

    @Test
    public void testOss() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);

        //...生成上传凭证，然后准备上传
/*        String accessKey = "ue_Q1QJb8Pufwm6qxFKeYuxt1amaCJwpHC3eTGfD";
        String secretKey = "_8yWdGC5zmFFlQkXponekTQpa5QsgXfg8hIZliDF";
        String bucket = "zjun122-zj-blog";*/

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "2023/test.png";
        try {
/*            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);*/

            InputStream inputStream = new FileInputStream("C:\\Users\\zjun122\\Pictures\\3c789e3d7089b98538cc2add87404a81.jpg");

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
    }
}
