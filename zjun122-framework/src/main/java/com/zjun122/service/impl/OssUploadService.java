package com.zjun122.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.zjun122.domain.ResponseResult;
import com.zjun122.enums.AppHttpCodeEnum;
import com.zjun122.service.UploadService;
import com.zjun122.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型或者文件大小
        String originalFilename = img.getOriginalFilename();
        assert originalFilename != null;
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果判断通过 上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = uploadOss(img, "img/" + filePath);
        return ResponseResult.okResult(url);
    }

    @Value("${oss.accessKey}")
    private String accessKey;
    @Value("${oss.secretKey}")
    private String secretKey;
    @Value("${oss.bucket}")
    private String bucket;

    private String uploadOss(MultipartFile imgFile, String filePath) {
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
        String key = filePath;
        try {
/*            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);*/

            InputStream inputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rryissvl8.hn-bkt.clouddn.com/" + filePath;
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
        return "null";
    }
}
