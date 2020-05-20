package com.icloud.api.sevice.groupshop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.modules.lm.dao.LmGroupShopMapper;
import com.icloud.modules.lm.dao.LmGroupShopSkuMapper;
import com.icloud.modules.lm.dto.goods.GroupShopDTO;
import com.icloud.modules.lm.entity.LmGroupShopSku;
import com.icloud.modules.lm.enums.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: rize
 * Date: 2019/11/25
 * Time: 11:30
 */
@Service
public class GroupShopBizService {

    @Autowired
    private LmGroupShopMapper groupShopMapper;

    @Autowired
    private LmGroupShopSkuMapper groupShopSkuMapper;

    public GroupShopDTO getGroupShopById(Long id) {
        GroupShopDTO detail = groupShopMapper.detail(id);
//        GroupShopDTO detail = null;
        if (detail == null || detail.getStatus() == StatusType.LOCK.getCode()) {
            return null;
        }
        List<LmGroupShopSku> groupShopSkuList = groupShopSkuMapper.selectList(new QueryWrapper<LmGroupShopSku>().eq("group_shop_id", id));
        detail.setGroupShopSkuList(groupShopSkuList);
        return detail;
    }

}
