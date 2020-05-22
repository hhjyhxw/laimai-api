package com.icloud.api.sevice.goods;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.api.sevice.appriaise.AppraiseBizService;
import com.icloud.api.sevice.category.CategoryBizService;
import com.icloud.api.sevice.collect.CollectBizService;
import com.icloud.api.sevice.footpring.FootprintBizService;
import com.icloud.api.sevice.freight.FreightBizService;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dao.LmImgMapper;
import com.icloud.modules.lm.dao.LmSpuAttributeMapper;
import com.icloud.modules.lm.dao.LmSpuMapper;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.dto.freight.FreightTemplateDTO;
import com.icloud.modules.lm.dto.goods.SpuDTO;
import com.icloud.modules.lm.entity.LmCategory;
import com.icloud.modules.lm.entity.LmSku;
import com.icloud.modules.lm.entity.LmSpu;
import com.icloud.modules.lm.entity.LmSpuAttribute;
import com.icloud.modules.lm.enums.BizType;
import com.icloud.modules.lm.enums.SpuStatusType;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class GoodsBizService {

    /**
     * SPU 分页缓存
     */
    public static final String CA_SPU_PAGE_PREFIX = "CA_SPU_PAGE_";

    /**
     * SPU DTO 缓存  CA_SPU_+spuId
     */
    public static final String CA_SPU_PREFIX = "CA_SPU_";

    /**
     * SPU 销量缓存
     */
    private static final String CA_SPU_SALES_HASH = "CA_SPU_SALES_HASH";

    /**
     * SPU DO 缓存，加速 getById...  hashKey = 'S' + spuId
     */
    private static final String CA_SPU_HASH = "CA_SPU_HASH";

    @Autowired
    private LmImgService lmImgService;

    @Autowired
    private LmSpuService lmSpuService;
    @Autowired
    private LmSpuMapper lmSpuMapper;
    @Autowired
    private LmSkuService lmSkuService;
    @Autowired
    private LmCategoryService lmCategoryService;
    @Autowired
    private LmSpuAttributeService lmSpuAttributeService;
    @Autowired
    private AppraiseBizService appraiseBizService;
    @Autowired
    private FootprintBizService footprintBizService;
    @Autowired
    private CategoryBizService categoryBizService;
    @Autowired
    private LmSpuAttributeMapper lmSpuAttributeMapper;
    @Autowired
    private FreightBizService freightBizService;

    @Autowired
    private CacheComponent cacheComponent;

    @Autowired
    private LmImgMapper lmImgMapper;

    @Autowired
    private CollectBizService collectBizService;
//
//
//    @Autowired
//    private GroupShopSkuMapper groupShopSkuMapper;


    public Page<SpuDTO> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) throws ApiException {
        QueryWrapper<LmSpu> wrapper = new QueryWrapper<LmSpu>();
        Map<String,Object> params = new HashMap<String,Object>();

        if (!StringUtils.isEmpty(title)) {
            wrapper.like("title", title);
            params.put("title",title);
        } else {
            //若关键字为空，尝试从缓存取列表
            Page objFromCache = cacheComponent.getObj(CA_SPU_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, Page.class);
            if (objFromCache != null) {
                return objFromCache;
            }
        }

        if (orderBy != null && isAsc != null) {
            if(isAsc==true) wrapper.orderByAsc(orderBy);
            else wrapper.orderByDesc(orderBy);
        }
        //分类id 不为空，也不是根 0 节点
        if (categoryId != null && categoryId != 0L) {
            List<LmCategory> childrenList = lmCategoryService.list(new QueryWrapper<LmCategory>().eq("parent_id", categoryId));
            if (CollectionUtils.isEmpty(childrenList)) {
                //目标节点为叶子节点,即三级类目

                LinkedList<Long> childrenIds = new LinkedList<>();
                childrenIds.add(categoryId);
                params.put("categoryIds",childrenIds);
            } else {
                //目标节点存在子节点
                LinkedList<Long> childrenIds = new LinkedList<>();
                LmCategory categoryDO = (LmCategory) lmCategoryService.getById(categoryId);

                // 检验传入类目是一级还是二级类目
                if (categoryDO.getParentId() != 0L) {
                    //二级分类
                    childrenList.forEach(item -> {
                        childrenIds.add(item.getId());
                    });
                } else {
                    //一级分类
                    childrenList.forEach(item -> {
                        List<LmCategory> leafList = lmCategoryService.list(new QueryWrapper<LmCategory>().eq("parent_id", item.getId()));
                        if (!CollectionUtils.isEmpty(leafList)) {
                            leafList.forEach(leafItem -> {
                                childrenIds.add(leafItem.getId());
                            });
                        }
                    });
                }
                wrapper.in("category_id", childrenIds);
                params.put("categoryIds",childrenIds);
            }
        }

        wrapper.eq("status", String.valueOf(SpuStatusType.SELLING.getCode()));
        params.put("status",String.valueOf(SpuStatusType.SELLING.getCode()));

        Integer count = lmSpuMapper.selectCount(wrapper);
        params.put("offset",pageSize * (pageNo - 1));
        params.put("pageSize",pageSize);
        List<LmSpu> spuDOS = lmSpuMapper.getAllPageByMap(params);
//        List<LmSpu> spuDOS = lmSpuMapper.getAllPage(params,pageSize * (pageNo - 1),pageSize);
//        List<LmSpu> spuDOS = pages.getRecords();
        //组装SPU
        List<SpuDTO> spuDTOList = new ArrayList<>();
        Map<String, String> salesHashAll = cacheComponent.getHashAll(CA_SPU_SALES_HASH);
        spuDOS.forEach(item -> {
            SpuDTO spuDTO = new SpuDTO();
            BeanUtils.copyProperties(item, spuDTO);
            if (salesHashAll != null) {
                String salesStr = salesHashAll.get("S" + item.getId());
                if (!StringUtils.isEmpty(salesStr)) {
                    spuDTO.setSales(new Integer(salesStr));
                }
            }
            spuDTOList.add(spuDTO);
        });
        Page<SpuDTO> page = new Page<SpuDTO>(spuDTOList,pageNo,pageSize,count);
        if (StringUtils.isEmpty(title)) {
            //若关键字为空，制作缓存
            cacheComponent.putObj(CA_SPU_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, page, Const.CACHE_ONE_DAY);
        }
        return page;
    }


    /**
     * 通过Id获取SpuDO 领域对象
     *
     * @param spuId
     * @return
     * @throws ServiceException
     */
    public LmSpu getSpuById(Long spuId) throws ServiceException {
        LmSpu objFromCache = cacheComponent.getHashObj(CA_SPU_HASH, "S" + spuId, LmSpu.class);
        if (objFromCache != null) {
            return objFromCache;
        }
        LmSpu spuDO = (LmSpu) lmSpuService.getById(spuId);
        if (spuDO == null) {
            throw new ApiException("商品不存在");
        }
        cacheComponent.putHashObj(CA_SPU_HASH, "S" + spuDO, spuDO, Const.CACHE_ONE_DAY);
        return spuDO;
    }

    /**
     * 获取商品名称
     * @param spuId
     * @param userId
     * @return
     * @throws ServiceException
     */
    public SpuDTO getGoods(Long spuId, Long userId) throws ServiceException {
        //虫缓存获取
        SpuDTO spuDTOFromCache = cacheComponent.getObj(CA_SPU_PREFIX + spuId, SpuDTO.class);
        if (spuDTOFromCache != null) {
            packSpuCollectInfo(spuDTOFromCache, userId);
            //获取第一页评论
            Page<AppraiseResponseDTO> spuAppraise = appraiseBizService.getSpuAllAppraise(spuId, 1, 10);
            spuDTOFromCache.setAppraisePage(spuAppraise);
            if (userId != null && userId != 0l) {
                footprintBizService.addOrUpdateFootprint(userId, spuId);
            }
//            if (userId != null && userId == 0l) {
//                // 从管理员后台进入，返回最新的库存
//                List<LmSku> skuDOList = lmSkuService.list( new QueryWrapper<SkuDO>().eq("spu_id", spuId));
//                spuDTOFromCache.setSkuList(skuDOList);
//                int sum = skuDOList.stream().mapToInt(item -> item.getStock()).sum();
//                spuDTOFromCache.setStock(sum);
//            }
            return spuDTOFromCache;
        }
        //
        LmSpu spuDO = (LmSpu) lmSpuService.getById(spuId);
        SpuDTO spuDTO = new SpuDTO();
        BeanUtils.copyProperties(spuDO, spuDTO);
        spuDTO.setImgList(lmImgMapper.getImgs(BizType.GOODS.getCode(), spuId));
        if(spuDTO.getImgList()==null || spuDTO.getImgList().size()==0){
            List<String> imgList = new ArrayList<String>();
            imgList.add(spuDTO.getImg());
            spuDTO.setImgList(imgList);
        }
        List<LmSku> skuDOList = lmSkuService.list(new QueryWrapper<LmSku>().eq("spu_id", spuId));
        if(skuDOList!=null && skuDOList.size()>0){
            for (LmSku lmSku:skuDOList){
                if(lmSku.getImg()==null || "".equals(lmSku.getImg())){
                    lmSku.setImg(spuDTO.getImg());
                }
            }
        }
        spuDTO.setSkuList(skuDOList);
        //类目族 叶子分类id 及分类id...
        spuDTO.setCategoryIds(categoryBizService.getCategoryFamily(spuDO.getCategoryId()));
        String salesStr = cacheComponent.getHashRaw(CA_SPU_SALES_HASH, "S" + spuId);
        if (!StringUtils.isEmpty(salesStr)) {
            spuDTO.setSales(new Integer(salesStr));
        }
        int sum = skuDOList.stream().mapToInt(item -> item.getStock()).sum();
        spuDTO.setStock(sum);
        //获取商品属性
        List<LmSpuAttribute> spuAttributeList = lmSpuAttributeMapper.selectList(new QueryWrapper<LmSpuAttribute>().eq("spu_id", spuId));
        spuDTO.setAttributeList(spuAttributeList);
        //获取运费模板
        if(spuDO.getFreightTemplateId()!=null){
            FreightTemplateDTO templateDTO = freightBizService.getTemplateById(spuDO.getFreightTemplateId());
            spuDTO.setFreightTemplate(templateDTO);
        }
        //放入缓存
        cacheComponent.putObj(CA_SPU_PREFIX + spuId, spuDTO, Const.CACHE_ONE_DAY / 2);
        packSpuCollectInfo(spuDTO, userId);
        //获取第一页评论
        Page<AppraiseResponseDTO> spuAppraise = appraiseBizService.getSpuAllAppraise(spuId, 1, 10);
        spuDTO.setAppraisePage(spuAppraise);
        if (userId != null) {
            footprintBizService.addOrUpdateFootprint(userId, spuId);
        }
        return spuDTO;
    }

    public void clearGoodsCache(Long spuId) {

        cacheComponent.del(CA_SPU_PREFIX + spuId);

        cacheComponent.delPrefixKey(CA_SPU_PAGE_PREFIX);

        cacheComponent.delHashObj(CA_SPU_HASH, "S" + spuId);

    }

    private void packSpuCollectInfo(SpuDTO spuDTO, Long userId) throws ServiceException {
        if (userId != null) {
            Boolean collectStatus = collectBizService.getCollectBySpuId(spuDTO.getId(), userId);
            spuDTO.setCollect(collectStatus);
        }
    }
}