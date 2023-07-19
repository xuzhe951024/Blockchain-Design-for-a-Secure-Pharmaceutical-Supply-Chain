package com.rbpsc.biz.impl;


import org.rbpsc.api.entities.supplychain.roles.RoleBase;
import com.rbpsc.biz.service.WebPageService;
import com.rbpsc.repository.service.RoleBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WebPageServiceImpl implements WebPageService {
    
    final RoleBaseRepository roleBaseRepository;

    public WebPageServiceImpl(RoleBaseRepository roleBaseRepository) {
        this.roleBaseRepository = roleBaseRepository;
    }

    @Override
    public List<String> getAddressListByBatchId(String batchId) {
        RoleBase roleBaseKey = new RoleBase();
        roleBaseKey.setBatchId(batchId);
        
        List<RoleBase> roleBaseList = roleBaseRepository.findByExample(roleBaseKey);

        return new ArrayList<String>(){{
            roleBaseList.forEach(roleBase -> {
                add(roleBase.getAddress());
            });
        }};

    }
}
