package com.example.demoplswork;

import java.util.ArrayList;
import java.util.List;

public class MockContactDAO implements ContactDAO {
    /**
     * A static list of contacts to be used as a mock database.
     */
    private final ArrayList<Contact> contacts = new ArrayList<>();
    private int autoIncrementedId = 0;
    private ContactDAO contactDAO;

    @Override
    public void addContact(Contact contact) {
        contact.setId(autoIncrementedId++);
        contacts.add(contact);
        System.out.println("Added contact: " + contact.getFullName() + " | Total contacts: " + contacts.size());
    }


    @Override
    public void updateContact(Contact contact) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == contact.getId()) {
                contacts.set(i, contact);
                break;
            }
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        contacts.removeIf(c -> c.getId() == contact.getId());
    }

    @Override
    public Contact getContact(int id) {
        for (Contact contact : contacts) {
            if (contact.getId() == id) {
                return contact;
            }
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    public List<Contact> searchContacts(String query) {
        System.out.println("Searching contacts with query: " + query);
        if (query == null || query.isEmpty()) {
            System.out.println("Query is null or empty. Returning all contacts: " + contacts.size());
            return new ArrayList<>(contacts);
        }
        query = query.toLowerCase();
        ArrayList<Contact> results = new ArrayList<>();
        for (Contact contact : contacts) {
            String searchString = contact.getFullName().toLowerCase()
                    + " " + contact.getEmail().toLowerCase()
                    + " " + contact.getPhone().toLowerCase();
            System.out.println("Checking contact: " + contact.getFullName());
            System.out.println("Search String: " + searchString);
            if (searchString.contains(query)) {
                results.add(contact);
                System.out.println("Match found: " + contact.getFullName());
            }
        }
        System.out.println("Total matches found: " + results.size());
        return results;
    }
    public void clearContacts() {
        System.out.println("Clearing contacts: " + contacts.size() + " contacts before clearing.");
        contacts.clear();
        System.out.println("Contacts cleared. Current count: " + contacts.size());
    }

    public void resetId() {
        autoIncrementedId = 0;
    }

}
