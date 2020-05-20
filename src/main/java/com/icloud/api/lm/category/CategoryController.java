package com.icloud.api.lm.category;


import com.icloud.api.sevice.category.CategoryBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryBizService categoryBizService;

    /**
     * 获取类目列表"
     * @return
     * @throws ApiException
     */

    @RequestMapping("/categoryList")
    @ResponseBody
    public ApiResponse categoryList() throws ApiException {
        List<CategoryDTO> list = categoryBizService.categoryList();
        return new ApiResponse().ok(list);
    }


    /**
     * 获取分类父节点
     * @param categoryId
     * @return
     * @throws ApiException
     */

    @RequestMapping("/getCategoryFamily")
    @ResponseBody
    public ApiResponse getCategoryFamily(Long categoryId) throws ApiException{
        List<Long> list = categoryBizService.getCategoryFamily(categoryId);
        return new ApiResponse().ok(list);
    }

}
