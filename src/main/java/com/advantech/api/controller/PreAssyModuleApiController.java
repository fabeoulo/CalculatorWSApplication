/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.api.controller;

import com.advantech.api.model.PreAssyModulesDto;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Justin.Yeh
 */
@RestController
@RequestMapping("/Api/PreAssyModule")
public class PreAssyModuleApiController {

    private final Logger logger = LoggerFactory.getLogger(PreAssyModuleApiController.class);

    @Autowired
    private PreAssyModuleStandardTimeService preAssyModuleStandardTimeService;

    @ResponseBody
    @RequestMapping(value = "/getModules", method = RequestMethod.GET)
    public List<PreAssyModulesDto> getModules() {
        return preAssyModuleStandardTimeService.findAllWithTypes().stream()
                .map(f -> new PreAssyModulesDto(f.getModelName(), f.getPreAssyModuleType().getName()))
                .collect(Collectors.toList());
    }
}
