package com.legacybuy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.legacybuy.model.User;
import com.legacybuy.repository.UserRepository;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid Username or password");
		}

		return user;
	}

	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	User findOrCreateUser(String email, String password) {
		User user = userRepository.findByUsername(email);
		if (user == null) {
			user = new User();
			user.setUsername(email);
			if (password == null) {
				password = randomAlphabetic(8);
			}
			user.setPassword(password);
			userRepository.save(user);
		}
		return user;
	}

}
