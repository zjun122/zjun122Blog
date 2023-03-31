package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.TagDto;
import com.zjun122.domain.dto.TagListDto;
import com.zjun122.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        return tagService.pageTagList(pageNum, pageSize, tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagDto tagDto) {
        return tagService.addTag(tagDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable List<Long> id) {
        return tagService.delTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Integer id) {
        return tagService.getTag(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagDto tagDto) {
        return tagService.updateTag(tagDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag() {
        return tagService.listAllTag();
    }
}
