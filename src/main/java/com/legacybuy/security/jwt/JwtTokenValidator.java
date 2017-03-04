package com.legacybuy.security.jwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.legacybuy.model.User;
import com.legacybuy.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

/**
 * Class validates a given token by using the secret configured in the
 * application
 *
 */
@Service
public class JwtTokenValidator {

	private final Log log = LogFactory.getLog(getClass());

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	UserRepository userRepository;

	/**
	 * Tries to parse specified String as a JWT token. If successful, returns
	 * User object with username, id and role prefilled (extracted from token).
	 * If unsuccessful (token is invalid or not containing all required user
	 * properties), simply returns null.
	 *
	 * @param token
	 *            the JWT token to parse
	 * @return the User object extracted from specified token or null if a token
	 *         is invalid.
	 */
	public User parseToken(String token) {
		User user = null;

		try {
			Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

			Long id = Long.parseLong((String) body.get("userId"));
			String username = body.getSubject();

			user = userRepository.findByIdAndUsername(id, username);

		} catch (JwtException e) {
			log.error("Failed to parse Token: " + e.getMessage());
		}
		return user;
	}
}
