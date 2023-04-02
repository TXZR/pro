package com.example.pro.service;

import com.alibaba.fastjson.JSON;
import com.example.pro.config.TywRestrict;
import com.example.pro.constant.TywConstant;
import com.example.pro.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ProService {
    @TywRestrict
    public Object query(LCQueryDto dto) {
        String[] strs = dto.getCommodity().split("\\|");
        dto.setEdDate(dto.getEdDate().split("T")[0]);
        dto.setCommodity(strs[0]);
        ProductDto productDto = new ProductDto(dto.getCommodity(), dto.getDestination(), new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "T16:00:00.000Z");
        Map<String, Object> map = new HashMap<>();
        map.put("productParam", productDto);
        map.put("queryParam", ParamDto.parse(dto));
        map.put("speed", dto.getSpeed());
        String product = null;
        boolean jq = "2".equals(strs[1]);
        switch (dto.getCommodity()) {
            case "General Cargo, not restr" :
                product = "General Cargo";
                break;
            case "Chemicals, not restr, temp-sens" :
                product = "Passive Temp Support";
                break;
        }
        map.put("product", product);
        LCRespDto lcRespDto = pythonCall(map);
        log.info("python返回结果{}", JSON.toJSONString(lcRespDto));
        if(!"0".equals(lcRespDto.getReturnCode().getReturnCodeNumber())) {
            throw new RuntimeException("查询错误！");
        }
        return ResultDto.parse(lcRespDto, dto.getWeight(), jq, 3.0);
    }

    private LCRespDto pythonCall(Map<String, Object> map) {
        String body = HttpTemplate.httpPost(TywConstant.PY_URL, map);
        log.info("查询python返回:{}", body);
        return JSON.parseObject(body, LCRespDto.class);
    }

}
