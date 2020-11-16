package com.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Pablo on 20/03/2016.
 */
@Entity
public class User {

    @Id
    private String email;
    private String name;
    private String photo;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public User() {
        this.email = new String();
        this.name = new String();
        this.photo = new String();
    }
    public User(String email, String name, String photo) {
        this.email = new String();
        this.name = new String();
        this.photo = new String();
    }
    public User(String email, String name) {
        this.email = new String();
        this.name = new String();
        this.photo = new String();
    }
    public User(String email) {
        this.email = new String();
        this.name = new String();
        this.photo = new String();
    }

}
