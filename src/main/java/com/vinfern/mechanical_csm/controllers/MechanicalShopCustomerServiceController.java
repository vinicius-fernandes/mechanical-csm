package com.vinfern.mechanical_csm.controllers;

import com.vinfern.mechanical_csm.models.Query;
import com.vinfern.mechanical_csm.models.QueryAnswer;
import com.vinfern.mechanical_csm.models.ServiceInfo;
import com.vinfern.mechanical_csm.services.CsmService;
import com.vinfern.mechanical_csm.services.VectorStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer-service")
public class MechanicalShopCustomerServiceController {

    @Autowired
    VectorStoreService vectorStoreService;
    @Autowired
    CsmService csmService;

    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public void addServiceInfo(@RequestBody List<ServiceInfo> serviceInfo) {
        vectorStoreService.addServiceInfo(serviceInfo);
    }

    @PostMapping("/query")
    @ResponseStatus(HttpStatus.OK)
    public QueryAnswer query(@RequestBody Query userQuery) {
        return csmService.query(userQuery);
    }
}
