package com.example.pro.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class ProductDto {
    private String requestChannel = "LCAGPortal";
    private String commodityDescription;
    private String shipmentOrigin = "PVG";
    private String shipmentDestination;
    private String earliestDeliveryDate;
    private Map<String, Object> customerCertificates = new HashMap<String, Object>() {{
        this.put("certificate", new ArrayList<Object>() {{
                    this.add(new HashMap<String, String>() {{
                        this.put("certificateName", "BUP");
                        this.put("certificateValue", "Y");
                    }});
                }}
        );
    }};

    public ProductDto(String commodityDescription, String shipmentDestination, String earliestDeliveryDate) {
        this.commodityDescription = commodityDescription;
        this.shipmentDestination = shipmentDestination;
        this.earliestDeliveryDate = earliestDeliveryDate;
    }
}
