package com.example.pro.service;

import com.example.pro.config.TywRestrict;
import com.example.pro.dto.LCQueryDto;
import com.example.pro.dto.ParamDto;
import com.example.pro.dto.ProductDto;
import com.example.pro.dto.ResultDto;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProService {
    @TywRestrict
    public Object query(LCQueryDto dto) {
        ResultDto result = new ResultDto();
        ProductDto productDto = new ProductDto(dto.getCommodity(), dto.getDestination(), new SimpleDateFormat("YYYY-MM-dd").format(new Date()) + "T16:00:00.000Z");
        Map<String, Object> map = new HashMap<>();
        map.put("productParam", productDto);
        map.put("queryParam", ParamDto.parse(dto));
        map.put("speed", dto.getSpeed());
        String product = null;
        switch (dto.getCommodity()) {
            case "General Cargo, not restr" :
                product = "General Cargo";
                break;
            case "Chemicals, not restr, temp-sens" :
                product = "Passive Temp Support";
                break;
        }
        map.put("product", product);
        return map;
    }
}
