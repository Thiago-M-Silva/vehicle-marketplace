package org.acme.model;

import java.util.Date;
import java.util.UUID;

import org.acme.enums.EPaymentMethods;
import org.acme.enums.EPaymentStatus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment extends PanacheEntityBase {

    @Id
    private UUID id;
    private EPaymentMethods paymentMethod;
    private EPaymentStatus paymentStatus;
    private double amount;

    private int transactionId;

    @ManyToOne
    @JoinColumn(name = "client")
    private Users user;
    @ManyToOne
    @JoinColumn(name = "seller")
    private Users seller;
    @ManyToOne
    @JoinColumn(name = "bike")
    private Bikes bike;
    @ManyToOne
    @JoinColumn(name = "cars")
    private Cars car;
    @ManyToOne
    @JoinColumn(name = "boats")
    private Boats boat;
    @ManyToOne
    @JoinColumn(name = "planes")
    private Planes plane;
    private Date createDate;
    private Date updateDate;

}
