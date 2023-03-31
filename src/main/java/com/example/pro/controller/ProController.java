package com.example.pro.controller;

import com.example.pro.config.TywRestrictAspectHadler;
import com.example.pro.dto.LCQueryDto;
import com.example.pro.dto.TywResponse;
import com.example.pro.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return TywResponse.success(proService.query(lcQueryDto));
    }
}
