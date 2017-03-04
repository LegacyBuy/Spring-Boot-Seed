package com.legacybuy.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserDetail extends AbstractEntity {

	private static final long serialVersionUID = 4329017573753313270L;

	String firstName;
	String lastName;
	String mobile;

	@OneToOne(cascade = CascadeType.REMOVE)
	Address address;

}
