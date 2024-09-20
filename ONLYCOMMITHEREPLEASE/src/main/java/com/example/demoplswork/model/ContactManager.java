package com.example.demoplswork.model;


import com.example.demoplswork.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    private IContactDAO sqliteContactDAO;

    public ContactManager(IContactDAO contactDAO) {
        this.sqliteContactDAO = contactDAO;
    }

    public List<Contact> searchContacts(String query) {
        List<Contact> allContacts = sqliteContactDAO.getAllContacts();
        List<Contact> filteredContacts = new ArrayList<>();

        if (query == null || query.isEmpty()) {
            return allContacts;
        }

        String lowerCaseQuery = query.toLowerCase();
        for (Contact contact : allContacts) {
            if (contact.getFirstName().toLowerCase().contains(lowerCaseQuery) || //OR
                    contact.getLastName().toLowerCase().contains(lowerCaseQuery) ||//OR
                    contact.getEmail().toLowerCase().contains(lowerCaseQuery)) {//OR
                filteredContacts.add(contact);
            }
        }
        return filteredContacts;
    }


    public void createAccount(Contact contact) {
        sqliteContactDAO.createAccount(
                contact.getFirstName(),
                contact.getLastName(),
                contact.getEmail(),
                "password"
        );
    }
}
