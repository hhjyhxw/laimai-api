package com.icloud.api.sevice.freight;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dao.LmAddressMapper;
import com.icloud.modules.lm.dao.LmFreightemplateCarriageMapper;
import com.icloud.modules.lm.dao.LmFreightemplateMapper;
import com.icloud.modules.lm.dto.freight.FreightTemplateDTO;
import com.icloud.modules.lm.dto.freight.ShipTraceDTO;
import com.icloud.modules.lm.dto.order.OrderRequestDTO;
import com.icloud.modules.lm.dto.order.OrderRequestSkuDTO;
import com.icloud.modules.lm.entity.LmAddress;
import com.icloud.modules.lm.entity.LmFreightemplate;
import com.icloud.modules.lm.entity.LmFreightemplateCarriage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: kbq
 * Date: 2019-07-07
 * Time: 下午7:48
 */
@Service
public class FreightBizService {

    @Autowired
    private LmFreightemplateMapper freightTemplateMapper;

    @Autowired
    private LmFreightemplateCarriageMapper freightTemplateCarriageMapper;

    @Autowired
    private LmAddressMapper addressMapper;

    @Autowired
    private ShipTraceQuery shipTraceQuery;

    //根据传入的订单数据，计算邮费
    public Integer getFreightMoney(OrderRequestDTO orderRequestDTO) {
        List<OrderRequestSkuDTO> orderRequestSkuDTOList = orderRequestDTO.getSkuList();

        //根据模板Id,来存储这次订单中，该模板下的商品的总价格,计价
        HashMap<Long, Integer> freightMoney = new HashMap<Long, Integer>();

        //根据模板Id,来存储该模板下商品的数量,记件
        HashMap<Long, Integer> freightNum = new HashMap<Long, Integer>();

        //根据模板ID，来映射该模板的信息。
        HashMap<Long, FreightTemplateDTO> freightDTO = new HashMap<Long, FreightTemplateDTO>();

        //遍历skuID得到，对应的运费模板,并填充上面三个map
        for (OrderRequestSkuDTO orderRequestSkuDTO : orderRequestSkuDTOList) {
            LmFreightemplate freightTemplateDO = freightTemplateMapper.selectFreightBySkuId(orderRequestSkuDTO.getSkuId());
            List<LmFreightemplateCarriage> freightTemplateCarriageDOList = freightTemplateCarriageMapper.selectList(new QueryWrapper<LmFreightemplateCarriage>()
                    .eq("template_id", freightTemplateDO.getId()));

            Long templateId = freightTemplateDO.getId(); //获得模板ID

            Integer money = freightMoney.get(templateId); //计算对应模板的总费用
            if (money == null) {
                freightMoney.put(templateId, orderRequestSkuDTO.getNum() * orderRequestSkuDTO.getPrice());
            } else {
                freightMoney.put(templateId, money + orderRequestSkuDTO.getNum() * orderRequestSkuDTO.getPrice());
            }

            Integer num = freightNum.get(templateId);//计算对应模板的总数量
            if (num == null) {
                freightNum.put(templateId, orderRequestSkuDTO.getNum());
            } else {
                freightNum.put(templateId, num + orderRequestSkuDTO.getNum());
            }

            if (freightDTO.get(templateId) == null) {
                FreightTemplateDTO tempDTO = new FreightTemplateDTO(freightTemplateDO, freightTemplateCarriageDOList);
                freightDTO.put(templateId, tempDTO);
            }
        }

        Integer totalMoney = 0;
        LmAddress addressDO = addressMapper.selectById(orderRequestDTO.getAddressId());
        Integer discount = orderRequestDTO.getCoupon() != null ? orderRequestDTO.getCoupon().getDiscount() : 0;
        for (Long key : freightNum.keySet()) {
            totalMoney = totalMoney + getMoney(freightDTO.get(key), freightNum.get(key), addressDO == null ? "" : addressDO.getProvince(), freightMoney.get(key) - discount);
        }
        return totalMoney;
    }

    //根据传入的运费模板信息\使用同一模板商品数量\快递省份地址\使用同一模板商品总价减使用的优惠卷，计算这类商品在该订单中的运费
    private Integer getMoney(FreightTemplateDTO freightTemplateDTO, Integer num, String province, Integer moneySubDiscount) {

        List<LmFreightemplateCarriage> freightTemplateCarriageDOList = freightTemplateDTO.getFreightTemplateCarriageDOList();

        //先遍历是否是属于非默认运费规则，如果是，计算并返回值
        for (LmFreightemplateCarriage freightTemplateCarriageDO : freightTemplateCarriageDOList) {
            //邮寄地址省份在其中
            if (freightTemplateCarriageDO.getDesignatedArea().contains(province)) {
                //如果包邮返回0
                if (freightTemplateCarriageDO.getFreePrice() == 0) {
                    return 0;
                }
                //必须为正数才是满邮活动
                if (moneySubDiscount >= freightTemplateCarriageDO.getFreePrice() && freightTemplateCarriageDO.getFreePrice() > 0) {
                    return 0;
                }

                Integer open = freightTemplateCarriageDO.getFirstMoney();
                num = num - freightTemplateCarriageDO.getFirstNum();

                //该订单中使用该运费模板的商品总数小于或等于该运费模板的起价的件数
                if (num <= 0) {
                    return open;
                }

                if (num % freightTemplateCarriageDO.getContinueNum() != 0) {
                    open = open + freightTemplateCarriageDO.getContinueMoney() * ((num / freightTemplateCarriageDO.getContinueNum()) + 1);
                } else {
                    open = open + freightTemplateCarriageDO.getContinueMoney() * (num / freightTemplateCarriageDO.getContinueNum());
                }
                return open;
            }
        }

        //如果使用默认运费规则
        LmFreightemplate freightTemplateDO = freightTemplateDTO.getFreightTemplateDO();

        if (freightTemplateDO.getDefaultFreePrice() == 0) {
            return 0;
        }
        if (freightTemplateDO.getDefaultFreePrice() > 0 && moneySubDiscount >= freightTemplateDO.getDefaultFreePrice()) {
            return 0;
        }

        Integer open = freightTemplateDO.getDefaultFirstMoney();
        num = num - freightTemplateDO.getDefaultFirstNum();

        //该订单中使用该运费模板的商品总数小于或等于该运费模板的起价的件数
        if (num <= 0) {
            return open;
        }

        if (num % freightTemplateDO.getDefaultContinueNum() != 0) {
            open = open + freightTemplateDO.getDefaultContinueMoney() * ((num / freightTemplateDO.getDefaultContinueNum()) + 1);
        } else {
            open = open + freightTemplateDO.getDefaultContinueMoney() * (num / freightTemplateDO.getDefaultContinueNum());
        }
        return open;
    }

    public FreightTemplateDTO getTemplateById(Long templateId) throws ApiException {
        LmFreightemplate freightTemplateDO = freightTemplateMapper.selectById(templateId);
        if (freightTemplateDO == null) {
            throw new ApiException("运费模板不存在");
        }

        //查出副表中其他地区的东西
        List<LmFreightemplateCarriage> freightTemplateCarriageDOList = freightTemplateCarriageMapper.selectList(new QueryWrapper<LmFreightemplateCarriage>()
                .eq("template_id", freightTemplateDO.getId()));

        FreightTemplateDTO freightTemplateDTO = new FreightTemplateDTO(freightTemplateDO, freightTemplateCarriageDOList);
        return freightTemplateDTO;
    }

    public ShipTraceDTO getShipTraceList(String shipNo, String shipCode) throws ApiException {
        return shipTraceQuery.query(shipNo, shipCode);
    }
}
