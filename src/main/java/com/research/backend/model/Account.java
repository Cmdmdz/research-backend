package com.research.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    @Column(length = 50)
    private String branch;

    @Column(length = 50)
    private String faculty;

    @Column(length = 100)
    private String address;

    @NotBlank
    @Size(max = 120)
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "accountFK", joinColumns = @JoinColumn(name = "accountId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();


    public Account( @NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email, String firstname, String lastname, String branch, String faculty, String address, @NotBlank @Size(max = 120) String password) {

        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.branch = branch;
        this.faculty = faculty;
        this.address = address;
        this.password = password;
    }

    public enum ERole {
        ROLE_ADMIN,
        ROLE_USER
    }


}
