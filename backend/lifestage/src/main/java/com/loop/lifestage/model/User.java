
package com.loop.lifestage.model;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String username;

}