package com.study.inflreanjpa1.domain;

import com.study.inflreanjpa1.domain.item.Item;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address extends Item {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
