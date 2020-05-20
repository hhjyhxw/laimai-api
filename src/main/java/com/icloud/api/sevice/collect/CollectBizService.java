package com.icloud.api.sevice.collect;

import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dao.LmCollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by rize on 2019/7/12.
 */
@Service
public class CollectBizService {

    @Autowired
    private LmCollectMapper collectMapper;

    @Autowired
    private CacheComponent cacheComponent;

    public static final String CA_USER_COLLECT_HASH = "CA_USER_COLLECT_";

    public Boolean getCollectBySpuId(Long spuId, Long userId) throws ApiException {
        boolean hasKey = cacheComponent.hasKey(CA_USER_COLLECT_HASH + userId);
        if (!hasKey) {
            //若没有Key，则添加缓存
            List<String> spuIds = collectMapper.getUserCollectSpuIds(userId);
            if (CollectionUtils.isEmpty(spuIds)) {
                //redis set不能为空
                spuIds.add("0");
            }
            cacheComponent.putSetRawAll(CA_USER_COLLECT_HASH + userId, spuIds.toArray(new String[0]), Const.CACHE_ONE_DAY);
        }
        return cacheComponent.isSetMember(CA_USER_COLLECT_HASH + userId, spuId + "");
    }

}
