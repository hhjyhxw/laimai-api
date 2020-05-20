package com.icloud.api.sevice.recommend;

import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.dao.LmRecommendMapper;
import com.icloud.modules.lm.dto.RecommendDTO;
import com.icloud.modules.lm.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 2019-07-11
 * Time: 下午6:08
 */
@Service
public class RecommendBizService {

    @Autowired
    private CacheComponent cacheComponent;

    @Autowired
    private LmRecommendMapper recommendMapper;

    private static final String RECOMMEND_NAME = "RECOMMEND_TYPE_";

    public List<RecommendDTO> getRecommendByType(Integer recommendType, Integer pageNo, Integer pageSize) throws ApiException {
        List<RecommendDTO> recommendDTOList = cacheComponent.getObjList(RECOMMEND_NAME + recommendType + "_" + pageNo + "_" + pageSize, RecommendDTO.class);
        if (CollectionUtils.isEmpty(recommendDTOList)) {
            recommendDTOList = recommendMapper.getRecommendByType(recommendType, (pageNo - 1) * pageSize, pageSize);
            if (!CollectionUtils.isEmpty(recommendDTOList)) {
                cacheComponent.putObj(RECOMMEND_NAME + recommendType + "_" + pageNo + "_" + pageSize, recommendDTOList, 1000);
            }
        }
        return recommendDTOList;
    }

    public Page<RecommendDTO> queryAllRecommend(Integer pageNo, Integer pageSize) throws ApiException {
        Integer count = recommendMapper.selectCount(null);
        List<RecommendDTO> recommendDTOList = recommendMapper.getAllRecommend((pageNo - 1) * pageSize, pageSize);
        Page<RecommendDTO> page = new Page<>(recommendDTOList, pageNo, pageSize, count);
        return page;
    }
}
