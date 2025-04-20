package org.acme.model.Users;

import java.util.Date;

import org.acme.enums.EUserType;
import org.acme.model.Payment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {

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
    private Payment[] payment;
}
