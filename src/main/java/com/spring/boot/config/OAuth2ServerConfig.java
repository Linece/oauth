package com.spring.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuth2ServerConfig {

	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId("order").stateless(true);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().requestMatchers()
					.anyRequest().and().anonymous().and().authorizeRequests().antMatchers("/order/**").authenticated();// 配置order访问控制，必须认证过后才可以访问
		}

	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		AuthenticationManager authenticationManager;

		@Autowired
		RedisConnectionFactory redisConnectionFactory;

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			// 配置两个客户端,一个用于password认证一个用于client认证
			oauthServer.allowFormAuthenticationForClients();

		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient("client_1").resourceIds("order")
					.authorizedGrantTypes("client_credentials", "refresh_token").scopes("select").authorities("client")
					.secret("123456").and().withClient("client_2").resourceIds("order")
					.authorizedGrantTypes("password", "refresh_token").scopes("select").authorities("client")
					.secret("123456");
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.tokenStore(new InMemoryTokenStore()).allowedTokenEndpointRequestMethods(HttpMethod.GET,
					HttpMethod.POST);
			// endpoints.tokenStore(new
			// RedisTokenStore(redisConnectionFactory)).authenticationManager(
			// authenticationManager);
		}
	}
}
