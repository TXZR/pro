package com.example.pro.dto;

import com.example.pro.service.ProUtils;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResultDto {
    private String id;
    private List<String> carrier;
    private List<String> comment;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private String stops;
    private String price;
    private String surcharge;
    private List<ResultDto> children;

    public static List<ResultDto> parse(LCRespDto lcRespDto, boolean jq, Double money) {
        List<ResultDto> resultDtoList = new ArrayList<>();
        try {
            if(!CollectionUtils.isEmpty(lcRespDto.getBestRoutesResponse().getBookableRoutes())) {
                BigDecimal w = BigDecimal.valueOf(lcRespDto.getBestRoutesResponse().getShipmentTotals().getChargeableWeight());
                for (LCRespDto.BookableRoute bookableRoute : lcRespDto.getBestRoutesResponse().getBookableRoutes()) {
                    ResultDto main = new ResultDto();
                    List<ResultDto> childrenList = new ArrayList<>();
                    main.setChildren(childrenList);
                    resultDtoList.add(main);
                    main.setId(bookableRoute.getRouteNumber().toString());
                    //先解析出主
                    List<LCRespDto.FlightSegment> list = bookableRoute.getFlightSegments();
                    main.setCarrier(new ArrayList<>());
                    if(bookableRoute.getNumberOfStops() == 0) {
                        main.setStops("Direct");
                    } else {
                        main.setStops(bookableRoute.getNumberOfStops() + " stop");
                    }
                    main.setComment(new ArrayList<>());
                    if(!CollectionUtils.isEmpty(bookableRoute.getRouteQueueRemarks())) {
                        for (LCRespDto.RouteQueueRemark routeQueueRemark : bookableRoute.getRouteQueueRemarks()) {
                            main.getComment().add(routeQueueRemark.getReasonCode() + " " + routeQueueRemark.getReasonDescription());
                        }
                    }
                    main.setOrigin(bookableRoute.getRouteOrigin());
                    main.setDestination(bookableRoute.getRouteDestination());
                    main.setDeparture(ProUtils.parseDate(bookableRoute.getEarliestDeliveryTimeOfRouting()));
                    main.setArrival(ProUtils.parseDate(bookableRoute.getLatestPickupTimeOfRouting()));
                    LCRespDto.RevenuePriceInformation priceInformation = bookableRoute.getRevenuePriceInformation();
                    LCRespDto.SurchargeInformation surchargeInformation = bookableRoute.getSurchargeInformation();
                    BigDecimal price = BigDecimal.valueOf(priceInformation.getRevenuePriceInRequestedCurrency());
                    BigDecimal newP = price.divide(w, 2, RoundingMode.HALF_UP);
                    if(jq) {
                        newP = newP.add(BigDecimal.valueOf(money));
                    }
                    main.setPrice(newP.toPlainString() + " CNY/kg");
                    if(priceInformation.isSurchargeIncludedInRevenuePrice()) {
                        main.setSurcharge("0 CNY/kg");
                    } else {
                        BigDecimal surcharge = BigDecimal.valueOf(surchargeInformation.getTotalSurchargesInRequestedCurrency());
                        main.setSurcharge(surcharge.divide(w, 2, RoundingMode.HALF_UP).toPlainString() + " CNY/kg");
                    }
                    for (LCRespDto.FlightSegment flightSegment : list) {
                        main.getCarrier().add(flightSegment.getSegmentFlightDesignator().getAirlineDesignator() + flightSegment.getSegmentFlightDesignator().getFlightNumber());
                        ResultDto children = new ResultDto();
                        main.getChildren().add(children);
                        children.setId(String.format("%s-%s", bookableRoute.getRouteNumber(), flightSegment.getSegmentSequenceNumber()));
                        children.setCarrier(new ArrayList<>());
                        children.getCarrier().add(flightSegment.getSegmentFlightDesignator().getAirlineDesignator() + flightSegment.getSegmentFlightDesignator().getFlightNumber());
                        children.getCarrier().add("A/C type:" + flightSegment.getSegmentAircraftType());
                        children.setDeparture(ProUtils.parseDate(flightSegment.getSegmentFlightDepatureTimeLocal()));
                        children.setArrival(ProUtils.parseDate(flightSegment.getSegmentFlightArrivalDateTimeLocal()));
                        children.setStops(ProUtils.getHours(flightSegment.getSegmentFlightDepatureTimeLocal(), flightSegment.getSegmentFlightArrivalDateTimeLocal()) + " hours");
                        children.setOrigin(flightSegment.getSegmentOrigin());
                        children.setDestination(flightSegment.getSegmentDestination());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultDtoList;
    }
}
