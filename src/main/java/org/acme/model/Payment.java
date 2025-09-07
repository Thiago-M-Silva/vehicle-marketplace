package org.acme.model;

import java.util.Date;
import java.util.UUID;

import org.acme.enums.EPaymentMethods;
import org.acme.enums.EPaymentStatus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "payment", schema = "mktplace")
public class Payment extends PanacheEntityBase {
    @Id
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod", length = 50)
    private EPaymentMethods paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus", length = 50)
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
