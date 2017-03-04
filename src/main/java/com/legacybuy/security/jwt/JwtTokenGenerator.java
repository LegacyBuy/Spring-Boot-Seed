package com.legacybuy.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.legacybuy.model.Authority;
import com.legacybuy.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * convenience class to generate a token for testing your requests. Make sure
 * the used secret here matches the on in your application.yml
 *
 */
@Service
public class JwtTokenGenerator {

	private final Log log = LogFactory.getLog(getClass());

	@Value("${jwt.secret}")
	private String secret;

	/**
	 * Generates a JWT token containing username as subject, and userId and role
	 * as additional claims. These properties are taken from the specified User
	 * object. Tokens validity is infinite.
	 *
	 * @param u
	 *            the user for which the token will be generated
	 * @return the JWT token
	 */
	public String generateToken(User u) {
		log.info("User: " + u);
		Claims claims = Jwts.claims().setSubject(u.getUsername());
		claims.put("userId", u.getId() + "");
		claims.put("username", u.getUsername() + "");
		claims.put("roles", u.getAuthorities());

		return Jwts.builder().setClaims(claims)
				// .setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public static void main(String[] args) {
		JwtTokenGenerator ddd = new JwtTokenGenerator();
		ddd.secret = "my-very-secret-key";
		User u = new User();
		u.setId(12L);
		u.setUsername("ram shay");

		List<Authority> authorities = new ArrayList<Authority>(1);
		authorities.add(new Authority("ROLE_CUSTOMER"));

		u.setAuthorities(authorities);
		System.out.println(ddd.generateToken(u));
	}
}
