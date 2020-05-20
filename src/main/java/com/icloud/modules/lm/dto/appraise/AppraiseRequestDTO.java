package com.icloud.modules.lm.dto.appraise;

import com.icloud.modules.lm.dto.SuperDTO;
import lombok.Data;

import java.util.List;

/*
前端评价时，传入信息的数据结构
@date  2019/7/6 - 14:26
*/
@Data
public class AppraiseRequestDTO extends SuperDTO {

    private Long orderId;

    private List<AppraiseRequestItemDTO> appraiseDTOList;

}


