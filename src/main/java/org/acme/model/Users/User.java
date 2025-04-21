package org.acme.model.Users;

import java.util.Date;

import org.acme.enums.EUserType;
import org.acme.model.Payment;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class User extends PanacheEntityBase {

    @Id
    private int id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private String cpf;
    private String rg;
    private Date birthDate;
    private Date createDate;
    private Date updateDate;
    private EUserType userType;
    @ManyToOne
    private Payment[] payment;
}
