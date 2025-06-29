package org.acme.model;

import java.util.Date;
import java.util.UUID;

import org.acme.dtos.UsersRequestDTO;
import org.acme.enums.EUserType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
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

    public Users(UsersRequestDTO data) {
        this.name = data.name();
        this.email = data.email();
        this.password = data.password();
        this.phoneNumber = data.phoneNumber();
        this.address = data.address();
        this.city = data.city();
        this.state = data.state();
        this.country = data.country();
        this.cpf = data.cpf();
        this.rg = data.rg();
        this.birthDate = data.birthDate();
        this.createDate = new Date(); // Set to current date
        this.updateDate = new Date(); // Set to current date
        this.userType = EUserType.valueOf(data.userType());
    }
}
