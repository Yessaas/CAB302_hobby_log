package com.example.demoplswork.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
/**
 * Material class stores material information.
 * It has fields for the name, quantity, and price of the material.
 * It has a constructor to initialize these fields.
 * It has getter methods to retrieve the values of these fields.
 */
// Material class to store material information
public class Material {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;

    /**
     * Constructor to initialize the fields of the Material object.
     * @param name The name of the material.
     * @param quantity The quantity of the material.
     * @param price The price of the material.
     */
    public Material(String name, int quantity, double price) {
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
    }

    /**
     * Getter method to retrieve the name of the material.
     * @return The name of the material.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Getter method to retrieve the quantity of the material.
     * @return The quantity of the material.
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * Getter method to retrieve the price of the material.
     * @return The price of the material.
     */
    public double getPrice() {
        return price.get();
    }
}
