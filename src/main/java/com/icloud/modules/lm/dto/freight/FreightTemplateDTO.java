package com.icloud.modules.lm.dto.freight;


import com.icloud.modules.lm.dto.SuperDTO;
import com.icloud.modules.lm.entity.LmFreightemplate;
import com.icloud.modules.lm.entity.LmFreightemplateCarriage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:

 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FreightTemplateDTO extends SuperDTO {

    private LmFreightemplate freightTemplateDO;

    private List<LmFreightemplateCarriage> freightTemplateCarriageDOList;

}
