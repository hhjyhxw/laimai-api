package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmUserMapper;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmUserService extends BaseServiceImpl<LmUserMapper,LmUser> {

    @Autowired
    private LmUserMapper lmUserMapper;

    public UserDTO login( String phoneString,String cryptPassword){
        return lmUserMapper.login(phoneString,cryptPassword);
    }

    public List<LmUser> getUserList(
            Long id, String nickname,
            String level,String gender,
            String status,Integer offset,
            Integer limit){
        return lmUserMapper.getUserList(id,nickname,level,gender,status,offset,limit);
    }

    public Integer countUser(
            Long id, String nickname,
            String level, String gender,
            String status){
        return lmUserMapper.countUser(id,nickname,level,gender,status);
    }
}

