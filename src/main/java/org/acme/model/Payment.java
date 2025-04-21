package org.acme.model;

import java.util.Date;

import org.acme.enums.EPaymentMethods;
import org.acme.enums.EPaymentStatus;
import org.acme.model.Bikes.Bikes;
import org.acme.model.Boats.Boats;
import org.acme.model.Cars.Cars;
import org.acme.model.Planes.Planes;
import org.acme.model.Users.User;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Payment extends PanacheEntityBase {

    @Id
    private int id;
    private String paymentId;
    private EPaymentMethods paymentMethod;
    private EPaymentStatus paymentStatus;
    private String paymentDate;
    private double amount;

    @OneToMany
    private User[] user;

    @ManyToOne
    private Bikes[] bike;
    @ManyToOne
    private Cars[] car;
    @ManyToOne
    private Boats[] boat;
    @ManyToOne
    private Planes[] plane;
    private Date createDate;
    private Date updateDate;

}
