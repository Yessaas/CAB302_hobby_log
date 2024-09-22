package com.example.demoplswork;

import java.util.List;

public interface ContactDAO {
    void addContact(Contact contact);
    void updateContact(Contact contact);
    void deleteContact(Contact contact);

    Contact getContact(int id);

    List<Contact> getAllContacts();

    List<Contact> searchContacts(String query);
}
