package com.icloud.api.web.groupshop;

import com.icloud.annotation.AuthIgnore;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.goods.GroupShopDTO;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmGroupShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/groupshop")
public class GroupShopController {

    @Autowired
    private LmGroupShopService lmGroupShopService;

    /**
     * 获取团购列表"
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getGroupShopPage")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getGroupShopPage(Integer pageNo, Integer pageSize) throws ApiException {
        Integer count = lmGroupShopService.count(null);
        List<GroupShopDTO> groupShopPage = lmGroupShopService.getGroupShopPage((pageNo - 1) * pageSize, pageSize);
        Page<GroupShopDTO>  page = new Page<>(groupShopPage, pageNo, pageSize, count);
        return new ApiResponse().ok(page);
    }


}
