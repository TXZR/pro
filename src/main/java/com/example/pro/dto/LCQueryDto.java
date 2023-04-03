package com.example.pro.dto;

import lombok.Data;

@Data
public class LCQueryDto {
    private String origin = "PVG";
    private String destination;
    private String edDate;
    private String commodity;
    private String speed;
    private Integer piece;
    private Double weight;
    private Double volume;
    private boolean dryIce;
    private String temperature;
}
