package org.acme.dtos;

public class PriceFallbackDTO {
    public String id;
    public Long unit_amount;
    public String currency;
    public Recurring recurring;
    public String product;

    public static class Recurring {
        public String interval;
    }
}
