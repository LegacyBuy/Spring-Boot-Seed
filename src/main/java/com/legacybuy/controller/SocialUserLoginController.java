package com.legacybuy.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectSupport;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.legacybuy.utils.Constants;
import com.legacybuy.utils.ResponseUtil;
import com.wordnik.swagger.annotations.Api;

@RestController
@Api(basePath = "lg/analysis/v1", value = "Social Login", description = "Operations with Social Providers", produces = "application/json")
@RequestMapping(value = Constants.LOGIN_V1_URL)
public class SocialUserLoginController implements InitializingBean {

	private final static Log logger = LogFactory.getLog(ProviderSignInController.class);

	private final ConnectionFactoryLocator connectionFactoryLocator;

	private final UsersConnectionRepository usersConnectionRepository;

	private final SignInAdapter signInAdapter;

	@Value("${application.url}")
	private String applicationUrl;

	private ConnectSupport connectSupport;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	@Autowired
	public SocialUserLoginController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.usersConnectionRepository = usersConnectionRepository;
		this.signInAdapter = signInAdapter;
	}

	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET)
	public void signIn(@PathVariable String providerId, NativeWebRequest request) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) request.getNativeResponse();
		System.out.println(providerId);
		try {
			ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
			ResponseUtil.respondSuccess(httpServletResponse, connectSupport.buildOAuthUrl(connectionFactory, request));
		} catch (Exception e) {
			logger.error("Exception while building authorization URL: ", e);
			ResponseUtil.respondFailure(httpServletResponse, HttpStatus.UNAUTHORIZED, e);
		}
	}

	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "oauth_token")
	public void oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) request.getNativeResponse();
		try {
			OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator
					.getConnectionFactory(providerId);
			Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
			handleSignIn(connection, connectionFactory, request);
		} catch (Exception e) {
			logger.error("Exception while completing OAuth 1.0(a) connection: ", e);
			ResponseUtil.respondFailure(httpServletResponse, HttpStatus.UNAUTHORIZED, e);
		}
	}

	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "code")
	public void oauth2Callback(@PathVariable String providerId, @RequestParam("code") String code,
			NativeWebRequest request) {
		HttpServletResponse httpServletResponse = (HttpServletResponse) request.getNativeResponse();
		try {
			OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator
					.getConnectionFactory(providerId);
			Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
			handleSignIn(connection, connectionFactory, request);
		} catch (Exception e) {
			logger.error("Exception while completing OAuth 2 connection: ", e);
			ResponseUtil.respondFailure(httpServletResponse, HttpStatus.UNAUTHORIZED, e);
		}
	}

	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET, params = "error")
	public void oauth2ErrorCallback(@PathVariable String providerId, @RequestParam("error") String error,
			@RequestParam(value = "error_description", required = false) String errorDescription,
			@RequestParam(value = "error_uri", required = false) String errorUri, NativeWebRequest request) {
		logger.warn("Error during authorization: " + error);

		HttpServletResponse response = request.getNativeResponse(HttpServletResponse.class);
		ResponseUtil.respondFailure(response, HttpStatus.UNAUTHORIZED, error);
	}

	// From InitializingBean
	public void afterPropertiesSet() throws Exception {
		this.connectSupport = new ConnectSupport(sessionStrategy);
		this.connectSupport.setUseAuthenticateUrl(true);
		if (this.applicationUrl != null) {
			this.connectSupport.setApplicationUrl(applicationUrl);
		}
	};

	// internal helpers

	private void handleSignIn(Connection<?> connection, ConnectionFactory<?> connectionFactory,
			NativeWebRequest request) {
		List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
		if (userIds.size() == 0) {
			throw new RuntimeException("Could not create user");
		} else if (userIds.size() == 1) {
			usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(connection);
			signInAdapter.signIn(userIds.get(0), connection, request);
		} else {
			throw new RuntimeException("Multiple users exist");
		}
	}
}
