package org.acme.model;

import java.util.Date;

import org.acme.enums.EPaymentMethods;
import org.acme.enums.EPaymentStatus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "payments")
public class Payment extends PanacheEntityBase {

    @Id
    private int id;
    private String paymentId;
    private EPaymentMethods paymentMethod;
    private EPaymentStatus paymentStatus;
    private String paymentDate;
    private double amount;

    @OneToMany
    private Users[] user;

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
