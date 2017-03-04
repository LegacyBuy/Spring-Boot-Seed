package com.legacybuy.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;

@Configuration
public class SocialConfig {

	@Autowired
	private Environment environment;

	@Autowired
	private TextEncryptor textEncryptor;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private ConnectionSignUp connectionSignup;

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		FacebookConnectionFactory factory = new FacebookConnectionFactory(environment.getProperty("facebook.appId"),
				environment.getProperty("facebook.appSecret"));
		factory.setScope(environment.getProperty("facebook.scope"));
		GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(
				environment.getProperty("spring.social.google.app-id"),
				environment.getProperty("spring.social.google.app-secret"));
		googleConnectionFactory.setScope(environment.getProperty("google.scope"));
		registry.addConnectionFactory(factory);
		registry.addConnectionFactory(googleConnectionFactory);
		return registry;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(authentication.getName());
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JdbcUsersConnectionRepository usersConnectionRepository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator(), textEncryptor);
		usersConnectionRepository.setConnectionSignUp(connectionSignup);
		return usersConnectionRepository;
	}

	@Configuration
	static class Dev {
		@Bean
		public TextEncryptor textEncryptor() {
			return Encryptors.noOpText();
		}
	}

	@PostConstruct
	private void init() {
		try {
			String[] fieldsToMap = { "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices",
					"education", "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown",
					"inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name",
					"link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political",
					"quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings",
					"significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time",
					"verified", "viewer_can_send_gift", "website", "work" };

			Field field = Class.forName("org.springframework.social.facebook.api.UserOperations")
					.getDeclaredField("PROFILE_FIELDS");
			field.setAccessible(true);

			Field modifiers = field.getClass().getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, fieldsToMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}