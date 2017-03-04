package com.legacybuy.model;

import javax.persistence.Entity;

@Entity
public class Address extends AbstractEntity {

	private static final long serialVersionUID = -7891629051576586698L;

	String streetAddress1;
	String streetAddress2;
	String city;
	String state;
	String country;
	String zipCode;
}
