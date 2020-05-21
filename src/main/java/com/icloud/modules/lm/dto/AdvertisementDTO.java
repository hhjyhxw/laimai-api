package com.icloud.modules.lm.dto;

import lombok.Data;

/**
 * Created by rize on 2019/7/14.
 */
@Data
public class AdvertisementDTO extends SuperDTO {

    private String adType;

    private String title;

    private String pageUrl;

    private String imgUrl;

    private Integer status;

    private String color;

    /**
     * 附加广告数据
     */
    private Object data;
}
