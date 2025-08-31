package org.acme.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehiclesListRequestDTO<T> {
    @JsonProperty("data")
    private List<T> data;
}