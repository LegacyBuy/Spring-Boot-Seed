package com.legacybuy.enums;

public enum Category {

	DOMAIN_NAME("Domain Name");

	private String name;

	Category(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
