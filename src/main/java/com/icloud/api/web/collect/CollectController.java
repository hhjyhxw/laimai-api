package com.icloud.api.web.collect;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.collect.CollectBizService;
import com.icloud.api.sevice.goods.GoodsBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dto.CollectDTO;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmCollect;
import com.icloud.modules.lm.entity.LmSpu;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/collect")
public class CollectController {

    //
    @Autowired
    private LmCollectService lmCollectService;

    @Autowired
    private CacheComponent cacheComponent;

    @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private CollectBizService collectBizService;
    /**
     * 增加收藏记录
     * @param spuId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/addCollect")
    @ResponseBody
    public ApiResponse addCollect(@LoginUser UserDTO user, Long spuId) throws ApiException {
        //校验SPU是否存在
        LmSpu spu = goodsBizService.getSpuById(spuId);
        if(spu==null){
            throw new ApiException("商品不存在");
        }
        List<LmCollect> collectDOS = lmCollectService.list(new QueryWrapper<LmCollect>()
                .eq("user_id", user.getId())
                .eq("collect_object_id", spuId));
        if (!CollectionUtils.isEmpty(collectDOS)) {
            throw new ApiException("已收藏");
        }
        LmCollect collectDO = new LmCollect();
        Date now = new Date();
        collectDO.setUserId(user.getId());
        collectDO.setCollectObjectId(spuId);
        collectDO.setCollectType("0");//商品spu收藏
        collectDO.setCreatedTime(now);
        collectDO.setUpdatedTime(collectDO.getCreatedTime());
        cacheComponent.putSetRaw(CollectBizService.CA_USER_COLLECT_HASH + user.getId(), spuId + "", Const.CACHE_ONE_DAY);

        return  new ApiResponse().okOrError(lmCollectService.save(collectDO));
    }

    /**
     * 删除收藏记录
     * @param spuId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/deleteCollect")
    @ResponseBody
    public ApiResponse deleteCollect(@LoginUser UserDTO user, Long spuId) throws ApiException{
        Boolean num = lmCollectService.remove(new QueryWrapper<LmCollect>()
                .eq("user_id", user.getId())
                .eq("collect_object_id", spuId));
        if (num) {
            cacheComponent.removeSetRaw(CollectBizService.CA_USER_COLLECT_HASH + user.getId(), spuId + "");
            return new ApiResponse().ok();
        }
        return new ApiResponse().error();
    }

    /**
     * 查询某一用户收藏记录
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getCollectAll")
    @ResponseBody
    public ApiResponse getCollectAll(@LoginUser UserDTO user,Integer pageSize,Integer pageNo) throws ApiException{
        pageNo=pageNo==null?1:pageNo;
        pageSize=pageSize==null?15:pageSize;
        Integer count = lmCollectService.count(new QueryWrapper<LmCollect>().eq("user_id", user.getId()));
        Integer offset = pageSize * (pageNo - 1);
        List<CollectDTO> collectAll = lmCollectService.getCollectAll(user.getId(), offset, pageSize);
        Page<CollectDTO> page = new Page<CollectDTO>(collectAll, pageNo, pageSize, count);
        return new ApiResponse().ok(page);
    }


    /**
     * 查询某一条收藏记录
     * @param collectId
     * @param spuId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getCollectById")
    @ResponseBody
    public ApiResponse getCollectById(@LoginUser UserDTO user, Long collectId, Long spuId ) throws ApiException{
        return new ApiResponse().ok(lmCollectService.getCollectById(user.getId(), collectId, spuId));
    }

    /**
     * 判断用户是否收藏
     * @param spuId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getCollectBySpuId")
    @ResponseBody
    public ApiResponse getCollectBySpuId(Long spuId, @LoginUser UserDTO user) throws ApiException{
        return new ApiResponse().okOrError( collectBizService.getCollectBySpuId(spuId, user.getId()));
    }


}
