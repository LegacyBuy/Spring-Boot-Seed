package com.legacybuy.model;

import java.util.Date;

import com.legacybuy.enums.TokenType;

/**
 * 
 * @author deepak
 *
 *         Multiple SESSION_TOKEN's are allowed. But FORGET_PASSWORD and
 *         ACCOUNT_ACTIVATION should be one. For now, there's no expire date for
 *         tokens
 *
 */

public class AuthenticationToken {

	Date expiry;
	TokenType type;
	String token;
	User user;

}
