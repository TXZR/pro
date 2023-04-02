package com.example.pro.dto;

import lombok.Data;

import java.util.List;

@Data
public class LCRespDto {
    private ReturnCode returnCode;

    private BestRoutesResponse bestRoutesResponse;


    @Data
    public static class ReturnCode {
        private String returnCodeNumber;
        private String returnDescription;
    }

    @Data
    public static class BestRoutesResponse {
        private List<BookableRoute> bookableRoutes;
    }

    @Data
    public static class BookableRoute {
        private Integer routeNumber;
        private String routeOrigin;
        private String routeDestination;
        private String earliestDeliveryTimeOfRouting;
        private String latestPickupTimeOfRouting;
        private String routeCO2Emission;
        private String routeStatus;
        private Integer numberOfStops;
        private List<RouteQueueRemark> routeQueueRemarks;
        private RevenuePriceInformation revenuePriceInformation;
        private SurchargeInformation surchargeInformation;
        private List<FlightSegment> flightSegments;
    }

    @Data
    public static class RouteQueueRemark {
        private String bookingCheck;
        private String reasonCode;
        private String reasonDescription;
    }

    @Data
    public static class RevenuePriceInformation {
        private Double revenuePriceInRequestedCurrency;
        private String revenuePriceType;
        private String rateOrCharge;
        private boolean surchargeIncludedInRevenuePrice;
    }

    @Data
    public static class SurchargeInformation {
        private Double totalSurchargesInRequestedCurrency;
    }

    @Data
    public static class FlightSegment {
        private Integer segmentSequenceNumber;
        private String segmentOrigin;
        private String segmentDestination;
        private SegmentFlightDesignator segmentFlightDesignator;
        private String segmentFlightDepatureTimeLocal;
        private String segmentFlightArrivalDateTimeLocal;
        private String segmentAircraftType;
        private String segmentFlightTransportCarrierType;
    }

    @Data
    public static class SegmentFlightDesignator {
        private String airlineDesignator;
        private String flightNumber;
    }

}
