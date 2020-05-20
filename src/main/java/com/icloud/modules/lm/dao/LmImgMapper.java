package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 图片存储表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:00
 */
public interface LmImgMapper extends BaseMapper<LmImg> {

	List<LmImg> queryMixList(Map<String, Object> map);

    public List<String> getImgs(@Param("bizType") Integer bizType, @Param("bizId") Long bizId);

    public Integer insertImgs(List<LmImg> imgs);
}
