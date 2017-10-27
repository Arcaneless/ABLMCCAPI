package com.arcaneless.ablmccapi.types;


import com.arcaneless.ablmccapi.Utils;

import java.util.ArrayList;
import java.util.List;

// Protocol 5
public class ContactInfo {

    private List<ContactCategory> cc;

    public ContactInfo() {
        cc = new ArrayList<>();
    }

    public List<ContactCategory> getList() {
        return cc;
    }

    public void addCategory(ContactCategory c) {
        cc.add(c);
    }

    @Override
    public String toString() {
        String list = "[";
        for (ContactCategory contact : cc) {
            list = list.concat(contact.toString()).concat(Utils.endOf(cc, contact) ? "]" : ",");
        }
        return list;
    }

    public static class ContactCategory {
        private List<Contact> contacts;
        private String category;

        public ContactCategory(String category) {
            this.category = category;
            contacts = new ArrayList<>();
        }

        public String getCategory() {
            return category;
        }

        public List<Contact> getContacts() {
            return contacts;
        }

        public void add(Contact contact) {
            contacts.add(contact);
        }

        @Override
        public String toString() {
            String list = "{" + category + ": [";
            for (Contact contact : contacts) {
                list = list.concat(contact.toString()).concat(Utils.endOf(contacts, contact) ? "]" : ",\n");
            }
            return list.concat("},\n");
        }
    }

    public static class Contact {
        private String name, email;

        public Contact(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String toString() {
            return "{\'name\': " + name + ", \'email\':" + email + "}";
        }
    }
}
