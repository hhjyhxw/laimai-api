package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmSpuAttribute;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmSpuAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 产品：标准化产品单元属性 SPU 属性(不会影响到库存和价格的属性, 又叫关键属性)
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmSpuAttributeService extends BaseServiceImpl<LmSpuAttributeMapper,LmSpuAttribute> {

    @Autowired
    private LmSpuAttributeMapper lmSpuAttributeMapper;
}

