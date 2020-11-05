package com.kiomnd2.demorestapi.accounts;

import lombok.*;

import javax.annotation.security.RolesAllowed;
import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter @Setter @EqualsAndHashCode(of= "id")
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue
    private int id;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;

}
