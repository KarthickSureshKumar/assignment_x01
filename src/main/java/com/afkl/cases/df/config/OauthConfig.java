package com.afkl.cases.df.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Profile("!test")
@EnableOAuth2Client
@Configuration
public class OauthConfig {

	@Value("${oauth2Config.clientId}")
	private String clientId;

	@Value("${oauth2Config.secret}")
	private String clientSecret;

	@Value("${tokenService}")
	private String tokenUrl;

	@Bean
	protected OAuth2ProtectedResourceDetails oauth2Resource() {
		ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
		clientCredentialsResourceDetails.setAccessTokenUri(tokenUrl);
		clientCredentialsResourceDetails.setClientId(clientId);
		clientCredentialsResourceDetails.setClientSecret(clientSecret);
		clientCredentialsResourceDetails.setGrantType("client_credentials");
		clientCredentialsResourceDetails.setAuthenticationScheme(AuthenticationScheme.header);
		return clientCredentialsResourceDetails;
	}

	@Bean
	public OAuth2RestTemplate oauth2RestTemplate() {
		AccessTokenRequest atr = new DefaultAccessTokenRequest();
		OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(oauth2Resource(),
				new DefaultOAuth2ClientContext(atr));
		oauth2RestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return oauth2RestTemplate;
	}

}
