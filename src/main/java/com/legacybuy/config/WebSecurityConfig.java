package com.legacybuy.config;

import java.security.SecureRandom;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.legacybuy.security.json.JsonAuthenticationProvider;
import com.legacybuy.security.json.JsonAuthenticationSuccessHandler;
import com.legacybuy.security.json.JsonAuthenticationTokenFilter;
import com.legacybuy.security.jwt.JwtAuthenticationEntryPoint;
import com.legacybuy.security.jwt.JwtAuthenticationProvider;
import com.legacybuy.security.jwt.JwtAuthenticationSuccessHandler;
import com.legacybuy.security.jwt.JwtAuthenticationTokenFilter;
import com.legacybuy.security.jwt.JwtTokenGenerator;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private JwtAuthenticationProvider jwtAuthenticationProvider;

	@Autowired
	private JsonAuthenticationProvider jsonAuthenticationProvider;

	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;

	@Value("${passwordencode.securerandom.seed}")
	String seedString;

	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		jsonAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return new ProviderManager(Arrays.asList(jwtAuthenticationProvider, jsonAuthenticationProvider));
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		SecureRandom secureRandom = new SecureRandom(seedString.getBytes());
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11, secureRandom);
		return bCryptPasswordEncoder;
	}

	public JsonAuthenticationTokenFilter jsonAuthenticationTokenFilterBean() throws Exception {
		JsonAuthenticationTokenFilter authenticationTokenFilter = new JsonAuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManager());
		authenticationTokenFilter
				.setAuthenticationSuccessHandler(new JsonAuthenticationSuccessHandler(jwtTokenGenerator));
		return authenticationTokenFilter;
	}

	public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilterBean() throws Exception {
		JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
		authenticationTokenFilter.setAuthenticationManager(authenticationManager());
		authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
		return authenticationTokenFilter;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/v1/login/*");
		web.ignoring().antMatchers("/**/*.html");
		web.ignoring().antMatchers("/**/*.js");
		web.ignoring().antMatchers("/**/*.css");
		web.ignoring().antMatchers("/**/*.png");
		web.ignoring().antMatchers("/swagger-resources");
		web.ignoring().antMatchers("/webjars/**/*");
		web.ignoring().antMatchers("/**/api-docs");
		web.ignoring().antMatchers("/configuration/*");
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();
		httpSecurity.httpBasic().disable();

		httpSecurity.authorizeRequests().anyRequest().authenticated();

		httpSecurity.addFilterAt(jsonAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		// Call our errorHandler if authentication/authorization fails
		httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);

		// enable anonymous access
		httpSecurity.anonymous();

		// don't create session
		// httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Custom JWT based security filter
		httpSecurity.addFilterAfter(jwtAuthenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

		// disable page caching
		httpSecurity.headers().cacheControl();
	}
}
