package org.acme.dtos;

import java.math.BigDecimal;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchDTO {
    @QueryParam("brand")
    private String brand;
    @QueryParam("model")
    private String model;
    @QueryParam("yearMin")
    private Integer yearMin;
    @QueryParam("yearMax")
    private Integer yearMax;
    @QueryParam("priceMin")
    private BigDecimal priceMin;
    @QueryParam("priceMax")
    private BigDecimal priceMax;
    @QueryParam("category")
    private String category;
    @QueryParam("color")
    private String color;
    @QueryParam("vehicleStatus")
    private String vehicleStatus;
    @QueryParam("fuelType")
    private String fuelType;
    @QueryParam("ownerId")
    private String ownerId;
    @QueryParam("sortBy")
    private String sortBy;
    @QueryParam("direction")
    private String direction;
    @QueryParam("page")
    private int page;
    @QueryParam("size")
    private int size;
}
