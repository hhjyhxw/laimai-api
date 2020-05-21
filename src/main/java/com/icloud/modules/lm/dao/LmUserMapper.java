package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmUserMapper extends BaseMapper<LmUser> {

	List<LmUser> queryMixList(Map<String, Object> map);

    public UserDTO login(@Param("phone") String phone, @Param("cryptPassword") String cryptPassword);


    public List<LmUser> getUserList(
            @Param("id") Long id, @Param("nickname") String nickname,
            @Param("level") String level, @Param("gender") String gender,
            @Param("status") String status, @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    public Integer countUser(
            @Param("id") Long id, @Param("nickname") String nickname,
            @Param("level") String level, @Param("gender") String gender,
            @Param("status") String status);
}
