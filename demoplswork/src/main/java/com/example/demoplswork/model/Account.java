package com.example.demoplswork.model;

import javafx.beans.property.SimpleStringProperty;

// Material class to store material information
public class Account {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty password;

    public Account(String firstName, String lastName, String email, String password) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
    }

    public String getfirstName() {
        return firstName.get();
    }

    public String getlastName() {
        return lastName.get();
    }

    public String getemail() {
        return email.get();
    }

    public String getpassword() {
        return password.get();
    }
}
