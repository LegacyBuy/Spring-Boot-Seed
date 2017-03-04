package com.legacybuy.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationScopeBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2 // Enable swagger 2.0 spec
@Configuration
@Profile("dev")
public class Swagger2Config {

	@Bean
	public Docket legacyBuyAll() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("lb-all").apiInfo(apiInfo()).select()
				.paths(allLBPaths()).build();
	}

	private Predicate<String> allLBPaths() {
		return regex("/.*");
	}

	@Bean
	public Docket userApi() {
		AuthorizationScope[] authScopes = new AuthorizationScope[1];
		authScopes[0] = new AuthorizationScopeBuilder().scope("read").description("read access").build();
		SecurityReference securityReference = SecurityReference.builder().reference("test").scopes(authScopes).build();

		ArrayList<SecurityContext> securityContexts = com.google.common.collect.Lists.newArrayList(SecurityContext
				.builder().securityReferences(com.google.common.collect.Lists.newArrayList(securityReference)).build());
		return new Docket(DocumentationType.SWAGGER_2)
				.securitySchemes(com.google.common.collect.Lists.newArrayList(new BasicAuth("test")))
				.securityContexts(securityContexts).groupName("user-api").apiInfo(apiInfo()).select()
				.paths(userOnlyEndpoints()).build();
	}

	private Predicate<String> userOnlyEndpoints() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return input.contains("user");
			}
		};
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Legacy Buy")
				.description("This API is powered by legacybuy for analyzing text and managing information.")
				.termsOfServiceUrl("www.legacybuy.com").contact("legacy").version("1.0").build();
	}

}
