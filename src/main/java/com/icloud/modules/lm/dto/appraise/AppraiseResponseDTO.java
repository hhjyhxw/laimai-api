package com.icloud.modules.lm.dto.appraise;

import com.icloud.modules.lm.dto.SuperDTO;
import lombok.Data;

import java.util.List;

/*
向前端传出评价信息的数据结构
*/
@Data
public class AppraiseResponseDTO extends SuperDTO {

    private String content;

    private Integer score;

    private List<String> imgList;

    private Long userId;

    private String userNickName;

    private String userAvatarUrl;

    private Long orderId;

    private Long spuId;

    private String spuTitle;

    private Long skuId;

    private String skuTitle;

}
