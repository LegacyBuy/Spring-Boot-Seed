package com.legacybuy.security.social;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import com.legacybuy.enums.Role;
import com.legacybuy.model.Authority;
import com.legacybuy.model.User;
import com.legacybuy.repository.AuthorityRepository;
import com.legacybuy.repository.UserRepository;

@Service
public class SocialConnectionSignup implements ConnectionSignUp {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public String execute(Connection<?> connection) {
		System.out.println("signup === ");
		UserProfile userProfile = connection.fetchUserProfile();
		User user = userRepository.findByUsername(userProfile.getEmail());
		if (user == null) {
			user = new User();
			user.setUsername(userProfile.getEmail());
			user.setPassword(randomAlphabetic(8));
			List<Authority> authorities = authorityRepository.findAllByAuthority(Role.ROLE_CUSTOMER.toString());
			user.setAuthorities(authorities);
			user.setEnabled(true);
			userRepository.save(user);
		}

		return userProfile.getEmail();
	}

}
