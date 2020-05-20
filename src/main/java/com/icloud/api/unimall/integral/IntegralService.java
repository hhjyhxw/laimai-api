import com.icloud.api.sevice.goods.GoodsBizService;
import com.icloud.modules.lm.dto.AdvertisementDTO;
import com.icloud.modules.lm.dto.IntegralIndexDataDTO;
import com.icloud.modules.lm.dto.RecommendDTO;
import com.icloud.modules.lm.dto.goods.SpuDTO;
import com.icloud.modules.lm.entity.LmAdvert;
import com.icloud.modules.lm.enums.AdvertisementType;
import com.icloud.modules.lm.enums.RecommendType;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmAdvertService;
import com.icloud.modules.lm.service.LmSpuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 将多个接口聚合到一起，减少HTTP访问次数
 * Created by rize on 2019/7/14.
 */
@Service
public class IntegralService {

    @Autowired
    private LmSpuService lmSpuService;
    @Autowired
    private LmAdvertService lmAdvertService;
    @Autowired
    private GoodsBizService goodsBizService;

    public IntegralIndexDataDTO getIndexData(){
        //分类
        List<LmAdvert> activeAd = lmAdvertService.list();
        Map<String, List<AdvertisementDTO>> adDTOMap = activeAd.stream().map(item -> {
            AdvertisementDTO advertisementDTO = new AdvertisementDTO();
            BeanUtils.copyProperties(item, advertisementDTO);
            return advertisementDTO;
        }).collect(Collectors.groupingBy(item -> "t" + item.getAdType()));

        //分类code 4  AdType
        List<AdvertisementDTO> categoryPickAd = adDTOMap.get("t" + AdvertisementType.CATEGORY_PICK.getCode());
        //封装 分类精选 商品
        if (!CollectionUtils.isEmpty(categoryPickAd)) {
            for (AdvertisementDTO item : categoryPickAd) {
                Page<SpuDTO> pickPage = goodsBizService.getGoodsPage(1, 10, new Long(item.getUrl().substring(item.getUrl().lastIndexOf("=") + 1)), "sales", false,null);
                item.setData(pickPage.getItems());
            }
        }
        IntegralIndexDataDTO integralIndexDataDTO = new IntegralIndexDataDTO();
        integralIndexDataDTO.setAdvertisement(adDTOMap);

        /**
         * 橱窗推荐
         */
        List<RecommendDTO> windowRecommend = recommendBizService.getRecommendByType(RecommendType.WINDOW.getCode(), 1, 10);
        integralIndexDataDTO.setWindowRecommend(windowRecommend);

        /**
         * 销量冠军
         */
        List<SpuDTO> salesTop = goodsBizService.getGoodsPage(1, 8, null, "sales", false, null).getItems();
        integralIndexDataDTO.setSalesTop(salesTop);

        /**
         * 最近上新
         */
        List<SpuDTO> newTop = goodsBizService.getGoodsPage(1, 8, null, "id", false, null).getItems();
        integralIndexDataDTO.setNewTop(newTop);
        return integralIndexDataDTO;
    }

}
