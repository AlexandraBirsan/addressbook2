package com.addressbook.model;

import javax.persistence.*;

/**
 * Created by birsan on 5/30/2016.
 */
@Entity
@Table(name = "ADDRESSBOOK_USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "userGenerator")
    @SequenceGenerator(name = "userGenerator", sequenceName = "usr_sequence")
    @Column(name = "U_ID")
    private Integer id;
    @Column(name = "U_USERNAME")
    private String username;
    @Column(name="U_PASSWORD")
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
