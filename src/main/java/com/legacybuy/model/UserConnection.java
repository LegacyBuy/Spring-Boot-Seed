package com.legacybuy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "UserConnection", indexes = {
		@Index(name = "UserConnectionRank", columnList = "userId,providerId,rank", unique = true) })
public class UserConnection {

	@EmbeddedId
	private UserConnectionId UserConnectionId;

	@Column(name = "rank", nullable = false)
	private Integer rank;

	@Column(name = "displayName", length = 255)
	private String displayName;

	@Column(name = "profileUrl", length = 512)
	private String profileUrl;

	@Column(name = "imageUrl", length = 512)
	private String imageUrl;

	@Column(name = "accessToken", nullable = false, length = 512)
	private String accessToken;

	@Column(name = "secret", length = 512)
	private String secret;

	@Column(name = "refreshToken", length = 512)
	private String refreshToken;

	@Column(name = "expireTime")
	private Long expireTime;

	@Embeddable
	static class UserConnectionId implements Serializable {

		private static final long serialVersionUID = 3449273539456511676L;

		@Column(name = "userId", nullable = false, length = 255)
		private String userId;

		@Column(name = "providerId", nullable = false, length = 255)
		private String providerId;

		@Column(name = "providerUserId", length = 255, nullable = true)
		private String providerUserId;
	}

}
