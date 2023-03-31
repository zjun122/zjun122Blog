package com.zjun122.service;

import com.zjun122.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传图片（仅限于png和jpg）
     * @param img 图片文件
     * @return
     */
    ResponseResult uploadImg(MultipartFile img);
}
