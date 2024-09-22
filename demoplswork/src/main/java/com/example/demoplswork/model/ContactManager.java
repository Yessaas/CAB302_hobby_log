package com.example.demoplswork.model;

import com.example.demoplswork.Contact;
import com.example.demoplswork.ContactDAO;

import java.util.List;

public class ContactManager {
    private ContactDAO contactDAO;

    public ContactManager(ContactDAO contactDAO) {
        this.contactDAO = contactDAO;
    }

    public List<Contact> searchContacts(String query) {
        return contactDAO.searchContacts(query);
    }


    public void addContact(Contact contact) {
        contactDAO.addContact(contact);
    }

    public void updateContact(Contact contact) {
        contactDAO.updateContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactDAO.deleteContact(contact);
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAllContacts();
    }
}
