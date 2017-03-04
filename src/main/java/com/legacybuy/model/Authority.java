package com.legacybuy.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

import com.legacybuy.enums.Role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Entity
@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class Authority extends AbstractEntity implements GrantedAuthority {

	private static final long serialVersionUID = -3506805573570762491L;

	@Setter(AccessLevel.NONE)
	@Column(unique = true, nullable = false)
	String authority;

	@SuppressWarnings("unused")
	private Authority() {
	}

	public Authority(String authority) {
		this.authority = authority;
	}

	public Authority(Role role) {
		this(role.toString());
	}
}
