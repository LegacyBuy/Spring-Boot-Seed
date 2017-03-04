package com.legacybuy.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.legacybuy.enums.Role;
import com.legacybuy.model.Authority;
import com.legacybuy.model.User;
import com.legacybuy.repository.AuthorityRepository;
import com.legacybuy.repository.UserRepository;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class BootStrap implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	AuthorityRepository authorityRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationReadyEvent arg0) {

		createRoles();
		createAdminUser();
	}

	@Transactional
	private void createRoles() {
		List<Authority> authorities = authorityRepository.findAll();

		Set<String> existingRoles = new HashSet<>(authorities.size());

		for (Authority authority : authorities) {
			existingRoles.add(authority.getAuthority());
		}

		for (Role role : Role.values()) {

			if (!existingRoles.contains(role.toString())) {
				Authority userAuthority = new Authority(role);

				try {
					authorityRepository.saveAndFlush(userAuthority);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}

	@Transactional
	private void createAdminUser() {
		if (authorityRepository.count() > 0) {
			String username = "admin";
			User user = userRepository.findByUsername(username);
			if (user == null) {
				List<Authority> authorities = authorityRepository.findAllByAuthority(Role.ROLE_ADMIN_WRITE.toString());
				user = new User();
				user.setUsername("admin");
				user.setPassword("admin");
				user.setAuthorities(authorities);
				user.setEnabled(true);
				userRepository.save(user);
			}
		}
	}
}
