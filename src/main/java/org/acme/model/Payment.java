package org.acme.model;

import java.util.Date;

import org.acme.enums.EPaymentMethods;
import org.acme.enums.EPaymentStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Payment {

    private String paymentId;
    private EPaymentMethods paymentMethod;
    private EPaymentStatus paymentStatus;
    private String paymentDate;
    private double amount;
    private User user;
    private Bikes[] bike;
    private Cars[] car;
    private Boats[] boat;
    private Planes[] plane;
    private Date createDate;
    private Date updateDate;

}
