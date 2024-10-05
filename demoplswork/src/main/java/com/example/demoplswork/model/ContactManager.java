package com.example.demoplswork.model;


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
            String fullName = contact.getFullName().toLowerCase(); //bothname
            if (fullName.contains(lowerCaseQuery) ||
                    contact.getFirstName().toLowerCase().contains(lowerCaseQuery) || //Fname
                    contact.getLastName().toLowerCase().contains(lowerCaseQuery) || //LName
                    contact.getBio().toLowerCase().contains(lowerCaseQuery) || //EMail
                    contact.getPhoto().contains(query)) { //Check phone
                filteredContacts.add(contact);
            }
        }
        return filteredContacts;
    }




    public void createAccount(Contact contact) {
        sqliteContactDAO.createAccount(
                contact.getFirstName(),
                contact.getLastName(),
                contact.getBio(),
                "password"
        );
    }
}
