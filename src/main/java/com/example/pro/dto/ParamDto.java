package com.example.pro.dto;

import lombok.Data;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ParamDto {
    private BestRoutesRequest bestRoutesRequest;

    public static ParamDto parse(LCQueryDto lcQueryDto) {
        ParamDto paramDto = new ParamDto();
        BestRoutesRequest bestRoutesRequest = new BestRoutesRequest();
        paramDto.setBestRoutesRequest(bestRoutesRequest);
        bestRoutesRequest.setShipmentOrigin(lcQueryDto.getOrigin());
        bestRoutesRequest.setShipmentDestination(lcQueryDto.getDestination());
        bestRoutesRequest.setEarliestDeliveryTime(lcQueryDto.getEdDate() + "T00:00:00.000Z");
        bestRoutesRequest.setProductDetail(new ProductDetail());
        bestRoutesRequest.getProductDetail().setSpeed(lcQueryDto.getSpeed());
        switch (lcQueryDto.getCommodity()) {
            case "General Cargo, not restr":
                bestRoutesRequest.getProductDetail().setCommodityCode("MISCELLANE");
                break;
            case "Chemicals, not restr, temp-sens":
                bestRoutesRequest.getProductDetail().setCommodityCode("TSCHEMICAL");
                if(!StringUtils.isEmpty(lcQueryDto.getTemperature())) {
                    switch (lcQueryDto.getTemperature()) {
                        case "1":
                            bestRoutesRequest.getProductDetail().setFromTemperature(-20);
                            bestRoutesRequest.getProductDetail().setToTemperature(-10);
                            break;
                        case "2":
                            bestRoutesRequest.getProductDetail().setFromTemperature(2);
                            bestRoutesRequest.getProductDetail().setToTemperature(8);
                            break;
                        case "3":
                            bestRoutesRequest.getProductDetail().setFromTemperature(15);
                            bestRoutesRequest.getProductDetail().setToTemperature(25);
                            break;
                    }
                }
                break;
        }
        bestRoutesRequest.setShipmentTotals(new ShipmentTotals());
        bestRoutesRequest.getShipmentTotals().setNumberOfPieces(lcQueryDto.getPiece());
        bestRoutesRequest.getShipmentTotals().setNetWeight(lcQueryDto.getWeight());
        bestRoutesRequest.getShipmentTotals().setNetVolume(lcQueryDto.getVolume());
        BigDecimal piece = new BigDecimal(lcQueryDto.getPiece());
        BigDecimal weight = BigDecimal.valueOf(lcQueryDto.getWeight());
        BigDecimal volume = BigDecimal.valueOf(lcQueryDto.getVolume());
        int i = 1;
        bestRoutesRequest.getShipmentTotals().setChargeableWeight(lcQueryDto.getVolume());
        BigDecimal nwight = weight.divide(piece, 2, RoundingMode.HALF_UP);
        BigDecimal h = volume.divide(piece, 2, RoundingMode.HALF_UP);
        if(nwight.compareTo(new BigDecimal("150")) >= 0 || h.compareTo(new BigDecimal("0.46")) >= 0) {
            PieceGroupInformation pieceGroupInformation = new PieceGroupInformation();
            pieceGroupInformation.setDryIce(false);
            pieceGroupInformation.setPieceGroupNumber(i++);
            pieceGroupInformation.setPieceLength(100);
            pieceGroupInformation.setPieceWidth(100);
            //Total volume (mc)/Total no. of pieces / 1/1 *100
            pieceGroupInformation.setPieceHeight(h.multiply(new BigDecimal("100")).intValue());
            pieceGroupInformation.setPieceWeight(nwight.toPlainString());
            pieceGroupInformation.setStackable(true);
            pieceGroupInformation.setTotalPieces(lcQueryDto.getPiece().toString());
            pieceGroupInformation.setUprightLoadable(true);
            bestRoutesRequest.getShipmentTotals().getPieceGroupInformation().add(pieceGroupInformation);
        }
        if(lcQueryDto.isDryIce()) {
            PieceGroupInformation pieceGroupInformation = new PieceGroupInformation();
            BigDecimal iceweight = BigDecimal.TEN.divide(piece, 2, RoundingMode.HALF_UP);
            pieceGroupInformation.setDryIce(true);
            pieceGroupInformation.setPieceGroupNumber(i);
            pieceGroupInformation.setPieceLength(0);
            pieceGroupInformation.setPieceWidth(0);
            pieceGroupInformation.setPieceHeight(0);
            pieceGroupInformation.setPieceWeight(iceweight.toPlainString());
            pieceGroupInformation.setStackable(true);
            pieceGroupInformation.setTotalPieces(lcQueryDto.getPiece().toString());
            pieceGroupInformation.setUprightLoadable(false);
            bestRoutesRequest.getShipmentTotals().getPieceGroupInformation().add(pieceGroupInformation);
        }
        return paramDto;
    }

    @Data
    public static class ApiHeader {
        private String conversationId = "767ba649-cb67-4e2a-b70c-52a8acdf9545";
        private String channelId = "eBooking";
        private String channelConsumer = "eBooking";
        private String channelUser = "ptssha01";
        private String tenantId = "183";
    }

    @Data
    public static class BestRoutesRequest {
        private String quotationDetails;
        private String booking;
        private String routeOffer;
        private String shipmentOrigin;
        private String shipmentDestination;
        private String chargeCode = "PP";
        private Map<String, String> serviceUnits = new HashMap<String, String>() {
            {
                this.put("weightUnit", "kg");
                this.put("volumeUnit", "mc");
                this.put("measurementUnit", "cm");
                this.put("currency", "CNY");
            }
        };
        private Map<String, String> shipmentIdentifier = new HashMap<String, String>() {
            {
                this.put("carrierNumericCode", "020");
            }
        };
        private String earliestDeliveryTime;
        private String latestPickupTime;
        private ProductDetail productDetail;
        private List<Map<String, String>> handlingCodes = new ArrayList<Map<String, String>>() {{
            this.add(new HashMap<String, String>() {
                {
                    this.put("handlingCode", "SLY");
                }
            });
            this.add(new HashMap<String, String>() {
                {
                    this.put("handlingCode", "PAS");
                }
            });
        }};
        private String routingConditions;
        private List<BusinessPartner> businessPartners = new ArrayList<BusinessPartner>(){{
            this.add(new BusinessPartner());
        }};
        private ShipmentTotals shipmentTotals;
    }

    @Data
    public static class ProductDetail {
        private String temperatureUnit = "C";
        private String speed;
        //需要接口获取
        private String productCode;
        //General Cargo, not restr - MISCELLANE  Chemicals, not restr, temp-sens TSCHEMICAL
        private String commodityCode;
        //温度设置
        private Integer fromTemperature;
        private Integer toTemperature;
    }

    @Data
    public static class BusinessPartner {
        private String customerRole = "Agent";
        private String customerNumber = "331686783";
        private String issuePlaceCityCode = "SHA";
        private String customerName = "Shanghai Pilotrans International Logistics Co.Ltd Bestway";
        private String address = "No 128 An Yuan Rd";
        private String cityName = "Shanghai";
        private String countryCode = "CN";
        private String postalCode = "200060";
    }

    @Data
    public static class ShipmentTotals {
        private Integer numberOfPieces;
        private Double netWeight;
        private Double netVolume;
        private Double chargeableWeight;
        private List<PieceGroupInformation> pieceGroupInformation = new ArrayList<>();
    }

    @Data
    public static class PieceGroupInformation {
        private Integer pieceGroupNumber;
        private String totalPieces;
        private String pieceWeight;
        private Integer pieceLength;
        private Integer pieceWidth;
        private Integer pieceHeight;
        private boolean uprightLoadable;
        private boolean stackable;
        private boolean dryIce;
    }

}

