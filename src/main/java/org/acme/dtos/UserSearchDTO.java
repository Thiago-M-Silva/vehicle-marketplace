package org.acme.dtos;

import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDTO {
    @QueryParam("name")
    private String name;
    @QueryParam("email")
    private String email;
    @QueryParam("city")
    private String city;
    @QueryParam("state")
    private String state;
    @QueryParam("country")
    private String country;
    @QueryParam("stripeAccountId")
    private String stripeAccountId;
    @QueryParam("sortBy")
    private String sortBy;
    @QueryParam("direction")
    private String direction;
    @QueryParam("page")
    private int page;
    @QueryParam("size")
    private int size;
}