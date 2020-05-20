package com.icloud.api.sevice.appriaise;


import com.aliyun.oss.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dao.LmImgMapper;
import com.icloud.modules.lm.dao.LmUserAppraiseMapper;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.enums.BizType;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmImgService;
import com.icloud.modules.lm.service.LmUserAppraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class AppraiseBizService {

    @Autowired
    private LmUserAppraiseService lmUserAppraiseService;
    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private LmImgService lmImgService;
    @Autowired
    private LmImgMapper LmImgMapper;

    @Autowired
    private LmUserAppraiseMapper lmUserAppraiseMapper;

    public static final String CA_APPRAISE_KEY = "CA_APPRAISE_";//评价缓存key

    /**
     * 获取商品评价
     * @param spuId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    public Page<AppraiseResponseDTO> getSpuAllAppraise(Long spuId, Integer pageNo, Integer pageSize) throws ServiceException {
        String cacheKey = CA_APPRAISE_KEY + spuId + "_" + pageNo + "_" + pageSize;
        com.icloud.modules.lm.model.Page obj = cacheComponent.getObj(cacheKey, com.icloud.modules.lm.model.Page.class);
        if (obj != null) {
            return obj;
        }
        PageHelper.startPage(pageNo, pageSize);
        List<AppraiseResponseDTO> appraiseResponseDTOS = lmUserAppraiseMapper.selectSpuAllAppraise(spuId,pageSize * (pageNo - 1),pageSize);
        for (AppraiseResponseDTO appraiseResponseDTO : appraiseResponseDTOS) {
            appraiseResponseDTO.setImgList(LmImgMapper.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
        }
        PageInfo<AppraiseResponseDTO> pageInfo = new PageInfo<AppraiseResponseDTO>(appraiseResponseDTOS);
        Page<AppraiseResponseDTO> page = new Page<AppraiseResponseDTO>(appraiseResponseDTOS,pageNo,pageSize,pageInfo.getTotal());

        cacheComponent.putObj(cacheKey, page, Const.CACHE_ONE_DAY);
        return page;
    }
}
