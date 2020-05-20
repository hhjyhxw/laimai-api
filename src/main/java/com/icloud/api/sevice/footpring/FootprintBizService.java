package com.icloud.api.sevice.footpring;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.entity.LmFooprint;
import com.icloud.modules.lm.service.LmFooprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FootprintBizService {

    @Autowired
    private LmFooprintService lmFooprintService;

    public boolean addOrUpdateFootprint(Long userId, Long spuId) throws ApiException {
        Date now = new Date();
        List<LmFooprint> footprintDOList = lmFooprintService.list(
                new QueryWrapper<LmFooprint>()
                        .eq("user_id", userId)
                        .eq("spu_id", spuId)
                        .orderByDesc("updated_time"));
        if (CollectionUtils.isEmpty(footprintDOList)) {
            LmFooprint footprintDO = new LmFooprint();
            footprintDO.setUserId(userId);
            footprintDO.setSpuId(spuId);
            footprintDO.setCreatedTime(now);
            footprintDO.setUpdatedTime(now);
            return lmFooprintService.save(footprintDO);
        }else {
            LmFooprint footprintDO = footprintDOList.get(0);
            footprintDO.setUpdatedTime(now);
            return lmFooprintService.updateById(footprintDO);
        }
    }
}
