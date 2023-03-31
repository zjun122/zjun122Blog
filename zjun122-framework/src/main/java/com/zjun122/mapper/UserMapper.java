package com.zjun122.mapper;

import com.zjun122.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zjun122
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2023-03-22 22:09:28
* @Entity com.zjun122.domain.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 添加用户和角色的关联关系
     * @param id
     * @param roleIds
     */
    void addUserAndRole(@Param("id") Long id, @Param("roleIds") List<Long> roleIds);

    /**
     * 更改用户状态
     * @param userId
     * @param status
     */
    void updateUserStatus(@Param("id") Long userId, @Param("status") String status);

    /**
     * 查询用户对应的角色关联关系
     * @param id
     * @return
     */
    List<Long> selectUserAndRole(Long id);

    void delUserAndRole(Long id);
}




