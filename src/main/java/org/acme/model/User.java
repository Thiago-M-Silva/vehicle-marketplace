package org.acme.model;

import java.util.Date;

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

    public String name;
    public String email;
    public String password;
    public String phoneNumber;
    public String address;
    public String city;
    public String state;
    public String country;
    public String cpf;
    public String rg;
    public Date birthDate;
}
