package com.example.pro.service;

import com.example.pro.config.TywRestrict;
import com.example.pro.dto.LCQueryDto;
import com.example.pro.dto.ParamDto;
import com.example.pro.dto.ResultDto;
import org.springframework.stereotype.Service;

@Service
public class ProService {
    @TywRestrict
    public Object query(LCQueryDto dto) {
        ResultDto result = new ResultDto();
        return ParamDto.parse(dto);
    }
}
