package com.icloud.api.web.address.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.entity.LmAddress;
import com.icloud.modules.lm.service.LmAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AddressService {

    @Autowired
    private LmAddressService lmAddressService;

    /**
     * 添加\编辑用户地址
     * @return
     * @throws com.icloud.exceptions.ApiException
     */
    public Boolean addOrUpdateAddress(LmAddress address) throws ApiException {
            //查询用户所有地址
            Integer addressNum = lmAddressService.count(new QueryWrapper<LmAddress>().eq("user_id", address.getUserId()));
            LmAddress addressDO = null;
            if (addressNum > 0) {
                //defaultAddress = 1设置新添加地址为默认地址
                if ("1".equals(address.getIsDefault())) {
                    LmAddress preDefault = new LmAddress();
                    preDefault.setIsDefault("0");
                    //传入entity以及更新条件进行更新信息
                    //更新默认收货地址为  非默认地址
                    boolean result = lmAddressService.update(preDefault, new UpdateWrapper<LmAddress>().eq("user_id", address.getUserId()) .eq("is_default", "1"));
                }
            }
            if(address.getId()!=null){
                address.setUpdatedTime(new Date());
                return lmAddressService.updateById(address);
            }else {
                address.setCreatedTime(new Date());
                return lmAddressService.save(address);
            }
    }

    /**
     * 添加\编辑用户地址
     * @return
     * @throws com.icloud.exceptions.ApiException
     */
    public Boolean updateAddress(LmAddress address) throws ApiException {
        //查询用户所有地址
        Integer addressNum = lmAddressService.count(new QueryWrapper<LmAddress>().eq("user_id", address.getUserId()));
        LmAddress addressDO = null;
        if (addressNum > 0) {
            //defaultAddress = 1设置新添加地址为默认地址
            if ("1".equals(address.getIsDefault())) {
                LmAddress preDefault = new LmAddress();
                preDefault.setIsDefault("0");
                //传入entity以及更新条件进行更新信息
                //更新默认收货地址为  非默认地址
                boolean result = lmAddressService.update(preDefault, new UpdateWrapper<LmAddress>().eq("user_id", address.getUserId()) .eq("is_default", "1"));
            }
        }
        if(address.getId()!=null){
            address.setCreatedTime(new Date());
        }else {
            address.setUpdatedTime(new Date(0));
        }
        return lmAddressService.saveOrUpdate(address);
    }

    /**
     * 删除收货地址
     * @param addressId 地址id
     * @param userId    用户id
     * @return
     * @throws ApiException
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAddress(Long addressId, Long userId) throws ApiException {
        //判断是否是默认地址
        Integer defaultNum = lmAddressService.count(new QueryWrapper<LmAddress>()
                .eq("user_id", userId)
                .eq("id", addressId)
                .eq("is_default", "1"));
        //不是默认地址
        if (defaultNum == 0) {
            return lmAddressService.remove(new QueryWrapper<LmAddress>()
                    .eq("id", addressId)
                    .eq("user_id", userId));
        } else {
            //是默认地址
            if (!(lmAddressService.remove(new QueryWrapper<LmAddress>()
                    .eq("id", addressId)
                    .eq("user_id", userId)))) {
                throw new ApiException("删除地址失败");
            } else {
                //删除默认地址成功，修改剩余地址中的某个为默认地址
                List<LmAddress> addressDOS = lmAddressService.list(new QueryWrapper<LmAddress>().eq("user_id", userId));
                if (addressDOS.size() != 0) {
                    LmAddress addressDO = addressDOS.get(0);
                    addressDO.setIsDefault("1");
                    return lmAddressService.updateById(addressDO);
                }
                return true;
            }
        }
    }



    /**
     * 查询用户所有收货地址
     * @param userId
     * @return
     * @throws ApiException
     */
    public List<LmAddress> getAllAddress(Long userId) throws ApiException {
        return lmAddressService.list(new QueryWrapper<LmAddress>()
                .eq("user_id", userId));
    }

    /**
     * 根据地址ID，查询收货地址
     * @param addressId
     * @return
     * @throws ApiException
     */
    public LmAddress getAddressById(Long addressId) throws ApiException {
        return (LmAddress) lmAddressService.getById(addressId);
    }

    /**
     * 获取用户默认地址
     * @param userId
     * @return
     * @throws ApiException
     */
    public LmAddress getDefAddress(Long userId) throws ApiException {
        List<LmAddress> addressDOS = lmAddressService.list(
                new QueryWrapper<LmAddress>()
                        .eq("user_id", userId)
                        .eq("is_default", "1"));
        if (CollectionUtils.isEmpty(addressDOS)) {
            LmAddress addressDO = new LmAddress();
            addressDO.setCity("XXX");
            addressDO.setProvince("XXX");
            addressDO.setCounty("XXX");
            addressDO.setAddress("不需要收货地址");
            addressDO.setConsignee("匿名");
            addressDO.setPhone("XXXXXXXXXXX");
            return addressDO;
        } else {
            return addressDOS.get(0);
        }
    }


}
