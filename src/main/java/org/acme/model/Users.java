package org.acme.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
import jakarta.persistence.OneToMany;
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

    // 🔹 Stripe field
    @Column(name = "stripe_account_id")
    private String stripeAccountId;

    @ManyToOne
    @JoinColumn(name = "transactionId")
    private Payment transaction;

    @OneToMany(mappedBy = "owner")
    private List<Bikes> bikes;

    @OneToMany(mappedBy = "owner")
    private List<Boats> boats;

    @OneToMany(mappedBy = "owner")
    private List<Cars> cars;

    @OneToMany(mappedBy = "owner")
    private List<Planes> planes;

    @Column(nullable = false, unique = true)
    private String keycloakId;
}
