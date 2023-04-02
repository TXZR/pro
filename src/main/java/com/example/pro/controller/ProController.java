package com.example.pro.controller;

import com.alibaba.fastjson.JSON;
import com.example.pro.config.TywRestrictAspectHadler;
import com.example.pro.dto.LCQueryDto;
import com.example.pro.dto.TywResponse;
import com.example.pro.service.ProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api")
public class ProController {

    @Autowired
    private ProService proService;
    @Autowired
    private TywRestrictAspectHadler tywRestrictAspectHadler;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public Object query(@RequestBody LCQueryDto lcQueryDto) {
        log.info("query param:{}", JSON.toJSONString(lcQueryDto));
        return TywResponse.success(proService.query(lcQueryDto));
    }
}
