package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import java.util.UUID;
import java.util.List;

public class CustomerDatabaseAdapter implements Database {

    private CustomerDatabase database;

    public CustomerDatabaseAdapter(CustomerDatabase customerDatabase) {
        this.database = customerDatabase;
    }

    @Override
    public List<Customer> getCustomers()
    {
        return database.getCustomers();
    }

    @Override
    public boolean isRegisteredId(UUID cardID) {
        return database.isRegisteredId(cardID);
    }
}