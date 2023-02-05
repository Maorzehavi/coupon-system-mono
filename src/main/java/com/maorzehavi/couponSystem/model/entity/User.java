package com.maorzehavi.couponSystem.model.entity;

import com.maorzehavi.couponSystem.model.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "email")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class User {
    @Id
    @Column(name = "email", length = 200)
    // if the length is more than 200 it will throw an exception "Specified key was too long; max key length is 1000 bytes"
    private String email;

    private String password;


    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_email"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}