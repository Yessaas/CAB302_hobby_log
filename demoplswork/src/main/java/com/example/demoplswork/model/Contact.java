package com.example.demoplswork.model;

import java.util.Objects;

public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String bio;
    private String photo;

    public Contact(String firstName, String lastName, String bio, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equals(contact.firstName) &&
                lastName.equals(contact.lastName) &&
                bio.equals(contact.bio) &&
                photo.equals(contact.photo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, bio, photo);
    }
}
