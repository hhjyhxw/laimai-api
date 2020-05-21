package com.icloud.api.web.footprint;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.LoginUser;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.FootprintDTO;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmFooprint;
import com.icloud.modules.lm.service.LmFooprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/footprint")
public class FootprintController {

    @Autowired
    private LmFooprintService lmFooprintService;

    /**
     * 删除足迹"
     * @param footprintId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/deleteFootprint")
    @ResponseBody
    public ApiResponse deleteFootprint(@LoginUser UserDTO user, Long footprintId) throws ApiException {
        boolean judgeSQL = lmFooprintService.remove(new QueryWrapper<LmFooprint>()
                .eq("user_id",user.getId())
                .eq("id",footprintId));
        return new ApiResponse().okOrError(judgeSQL);
    }

    /**
     *     @Override
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getAllFootprint")
    @ResponseBody
    public ApiResponse getAllFootprint(@LoginUser UserDTO user) throws ApiException {
        List<FootprintDTO> footprintDTOList = lmFooprintService.getAllFootprint(user.getId(),0,30);
        return new ApiResponse().ok(footprintDTOList);
    }
}
