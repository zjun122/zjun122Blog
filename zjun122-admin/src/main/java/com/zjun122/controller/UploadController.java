package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping
    public ResponseResult uploadImg(MultipartFile img) {
        try {
            return uploadService.uploadImg(img);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
    }
}
