package org.acme.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.acme.dtos.UsersRequestDTO;
import org.acme.enums.EUserRole;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "mktplace")
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
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private EUserRole userRole;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createDate;

    @UpdateTimestamp
    private Instant updateDate;

    @ManyToOne
    @JoinColumn(name = "transactionId")
    private Payment transaction;

    @ManyToOne
    @JoinColumn(name = "bikeId")
    private Bikes bikes;

    @ManyToOne
    @JoinColumn(name = "boatId")
    private Boats boats;

    @ManyToOne
    @JoinColumn(name = "carId")
    private Cars cars;

    @ManyToOne
    @JoinColumn(name = "planeId")
    private Planes planes;

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
        this.userRole = data.userRole();
    }
}
