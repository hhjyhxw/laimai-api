package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.goods.GroupShopDTO;
import com.icloud.modules.lm.entity.LmGroupShop;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 团购主商品表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmGroupShopMapper extends BaseMapper<LmGroupShop> {

	List<LmGroupShop> queryMixList(Map<String, Object> map);

    public List<GroupShopDTO> getGroupShopPage(@Param("offset") Integer offset, @Param("limit") Integer limit);

    public GroupShopDTO detail(Long groupShopId);

    /**
     * 增加当前人数
     * @param id 团购活动Id
     * @param num 增加人数量
     * @return
     */
    public Integer incCurrentNum(@Param("id") Long id,@Param("num") Integer num);
}
