package com.example.demoplswork.model;

import java.util.Objects;
/**
 * Contact class represents a contact with a first name, last name, bio, and profile photo.
 * It has fields for the ID, first name, last name, bio, and photo.
 * It has a constructor to initialize these fields.
 * It has getter and setter methods to retrieve and update the values of these fields.
 * It has a method to get the full name of the contact.
 * It overrides the equals method to compare contacts based on their fields.
 * It overrides the hashCode method to generate a hash code based on the contact's fields.
 */
public class Contact {
    private int id;
    private String firstName;
    private String lastName;
    private String bio;
    private String photo;

    /**
     * Constructs a new Contact with the specified first name, last name, bio, and profile photo.
     * @param firstName The first name of the contact
     * @param lastName The last name of the contact
     * @param bio The bio of the contact
     * @param photo The profile photo of the contact
     */
    public Contact(String firstName, String lastName, String bio, String photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.photo = photo;
    }

    /*Getter for the ID of the contact*/
    public int getId() {
        return id;
    }
    /*Setter for the ID of the contact*/
    public void setId(int id) {
        this.id = id;
    }
    /*Getter for the first name of the contact*/
    public String getFirstName() {
        return firstName;
    }
    /*Setter for the first name of the contact*/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /*Getter for the last name of the contact*/
    public String getLastName() {
        return lastName;
    }
    /*Setter for the last name of the contact*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /*Getter for the bio of the contact*/
    public String getBio() {
        return bio;
    }
    /*Setter for the bio of the contact*/
    public void setBio(String bio) {
        this.bio = bio;
    }
    /*Getter for the profile photo of the contact*/
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    /*Method to get the full name of the contact*/
    public String getFullName() {
        return firstName + " " + lastName;
    }
    /*Method to compare contacts based on their fields*/
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
    /*Method to generate a hash code based on the contact's fields*/
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, bio, photo);
    }
}
