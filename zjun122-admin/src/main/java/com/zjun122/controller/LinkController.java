package com.zjun122.controller;

import com.zjun122.domain.ResponseResult;
import com.zjun122.domain.dto.LinkDto;
import com.zjun122.domain.entity.Link;
import com.zjun122.domain.vo.LinkListVo;
import com.zjun122.domain.vo.LinkVo;
import com.zjun122.service.LinkService;
import com.zjun122.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    // 直接修改审核通过和审核未通过的接口没写

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, String name, String status) {
        return linkService.pageList(pageNum, pageSize, name, status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody LinkDto linkDto) {
        linkDto.setId(null);
        linkService.save(BeanCopyUtils.copyBean(linkDto, Link.class));
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getLink(@PathVariable Long id) {
        Link link = linkService.getById(id);
        return ResponseResult.okResult(BeanCopyUtils.copyBean(link, LinkListVo.class));
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkDto linkDto) {
        linkService.updateById(BeanCopyUtils.copyBean(linkDto, Link.class));
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delLink(@PathVariable List<Long> id) {
        linkService.removeByIds(id);
        return ResponseResult.okResult();
    }

}
