package com.icloud.api.web.appraise.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.appriaise.AppraiseBizService;
import com.icloud.exceptions.ApiException;
import com.icloud.exceptions.ServiceException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseRequestDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseRequestItemDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.entity.*;
import com.icloud.modules.lm.enums.BizType;
import com.icloud.modules.lm.enums.OrderStatusType;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmImgService;
import com.icloud.modules.lm.service.LmOrderService;
import com.icloud.modules.lm.service.LmOrderSkuService;
import com.icloud.modules.lm.service.LmUserAppraiseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppraiseService {

    @Autowired
    private CacheComponent cacheComponent;
    @Autowired
    private LmOrderService lmOrderService;
    @Autowired
    private LmOrderSkuService lmOrderSkuService;
    @Autowired
    private LmUserAppraiseService lmUserAppraiseService;
    @Autowired
    private LmImgService lmImgService;
    @Autowired
    private AppraiseBizService appraiseBizService;

    /**
     * 增加评价
     * @param appraiseRequestDTO
     * @param user
     * @return
     * @throws ApiException
     */

    public boolean addAppraise(AppraiseRequestDTO appraiseRequestDTO, LmUser user) throws ApiException {
        if (appraiseRequestDTO.getOrderId() == null) {
            throw new ApiException("评价订单id为空");
        }
        //校验是否有对应等待评价的订单
        Integer integer = lmOrderService.count(
                new QueryWrapper<LmOrder>()
                        .eq("id", appraiseRequestDTO.getOrderId())
                        .eq("status", String.valueOf(OrderStatusType.WAIT_APPRAISE.getCode()))
                        .eq("user_id", user.getId()));
        if (integer == 0) {
            throw new ApiException("没有查到待评价的订单");
        }

        //如果传入评价list中没有数据，就直接转变订单状态发出
        Date now = new Date();
        if (CollectionUtils.isEmpty(appraiseRequestDTO.getAppraiseDTOList())) {
            LmOrder orderDO = new LmOrder();
            orderDO.setStatus(String.valueOf(OrderStatusType.COMPLETE.getCode()));
            orderDO.setId(appraiseRequestDTO.getOrderId());
            orderDO.setUpdatedTime(now);
            lmOrderService.updateById(orderDO);
        }

        //循环读取订单评价中所有商品的评价
        for (AppraiseRequestItemDTO appraiseDTO : appraiseRequestDTO.getAppraiseDTOList()) {
            Integer count = lmOrderSkuService.count(new QueryWrapper<LmOrderSku>()
                    .eq("order_id", appraiseRequestDTO.getOrderId())
                    .eq("spu_id", appraiseDTO.getSpuId())
                    .eq("sku_id", appraiseDTO.getSkuId()));
            //从order_sku表中 验证是否有对应的订单和商品
            if (count == 0) {
                throw new ApiException("没有查询到对应的订单明细");
            }

            LmUserAppraise appraiseDO = new LmUserAppraise();
            BeanUtils.copyProperties(appraiseDTO, appraiseDO);
            appraiseDO.setSpuId(appraiseDTO.getSpuId());
            appraiseDO.setId(null); //防止传入id,导致插入数据库出错
            appraiseDO.setOrderId(appraiseRequestDTO.getOrderId()); //从传入数据取出，不使用DTO中的冗余数据
            appraiseDO.setUserId(user.getId());
            appraiseDO.setCreatedTime(now);
            appraiseDO.setUpdatedTime(appraiseDO.getCreatedTime());
            lmUserAppraiseService.save(appraiseDO);  //插入该订单该商品评价
            cacheComponent.delPrefixKey(AppraiseBizService.CA_APPRAISE_KEY + appraiseDO.getSpuId()); //删除商品评论缓存
            if (appraiseDTO.getImgUrl() == null || appraiseDTO.getImgUrl().equals("")) {
                continue;
            }
            String imgUrlS = appraiseDTO.getImgUrl();
            String[] imgUrlList = imgUrlS.split(",");   //传入图片
            for (String imgurl : imgUrlList) {
                LmImg imgDO = new LmImg();
                imgDO.setBizType(String.valueOf(BizType.COMMENT.getCode()));
                imgDO.setBizId(appraiseDO.getId());
                imgDO.setUrl(imgurl);
                imgDO.setCreatedTime(now);
                imgDO.setUpdatedTime(imgDO.getCreatedTime());
                lmImgService.save(imgDO);
            }
        }

        //改变订单状态
        LmOrder orderDO = new LmOrder();
        orderDO.setStatus(String.valueOf(OrderStatusType.COMPLETE.getCode()));
        orderDO.setId(appraiseRequestDTO.getOrderId());
        orderDO.setUpdatedTime(now);
        return lmOrderService.updateById(orderDO);
    }

    /**
     * 根据评论Id删除评论
     * @param appraiseId
     * @param user
     * @return
     * @throws ApiException
     */
    public boolean deleteAppraiseById(Long appraiseId, UserDTO user) throws ApiException {
        return lmUserAppraiseService.remove(new QueryWrapper<LmUserAppraise>()
                .eq("id", appraiseId)
                .eq("user_id", user.getId())); //根据用户Id,评价Id
    }

    /**
     *查询用户所有评论
     * @param user
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApiException
     */
    public Page<AppraiseResponseDTO> getUserAllAppraise(@LoginUser LmUser user, Integer pageNo, Integer pageSize) throws ApiException {
        Integer count = lmUserAppraiseService.count(new QueryWrapper<LmUserAppraise>().eq("user_id", user.getId()));
        List<AppraiseResponseDTO> appraiseResponseDTOS = lmUserAppraiseService.selectUserAllAppraise(user.getId(),pageSize * (pageNo - 1),pageSize);

        for (AppraiseResponseDTO appraiseResponseDTO : appraiseResponseDTOS) {
            appraiseResponseDTO.setImgList(lmImgService.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
        }
        Page<AppraiseResponseDTO> page = new Page<>(appraiseResponseDTOS, pageNo, pageSize, count);
        return page;
    }


    /**
     *查询商品的所有评论
     * @param spuId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    public Page<AppraiseResponseDTO> getSpuAllAppraise(Long spuId, Integer pageNo, Integer pageSize) throws ServiceException {
        return appraiseBizService.getSpuAllAppraise(spuId, pageNo, pageSize);
    }


    /**
     *查询某一条评论
     * @param userId
     * @param appraiseId
     * @return
     * @throws ApiException
     */
    public AppraiseResponseDTO getOneById(Long userId, Long appraiseId) throws ApiException {
        AppraiseResponseDTO appraiseResponseDTO = lmUserAppraiseService.selectOneById(appraiseId);
        if (appraiseResponseDTO == null) {
            throw new ApiException("查询评价记录为空");
        }
        appraiseResponseDTO.setImgList(lmImgService.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
        return appraiseResponseDTO;
    }
}
