package com.legacybuy.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.userdetails.UserDetails;

import com.legacybuy.utils.PasswordUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
@Audited
public class User extends AbstractEntity implements UserDetails {

	private static final long serialVersionUID = 2507177602107639240L;

	@ManyToMany(fetch = FetchType.EAGER)
	@NotAudited
	private List<Authority> authorities;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String username;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = false;

	@OneToOne(cascade = CascadeType.ALL)
	@NotAudited
	private UserDetail userDetail;

	public void setPassword(String password) {
		this.password = PasswordUtil.encode(password);
	}
}
