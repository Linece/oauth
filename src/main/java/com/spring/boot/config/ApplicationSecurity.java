package com.spring.boot.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;

@Configuration
public class ApplicationSecurity extends AuthorizationServerConfigurerAdapter implements AuthenticationManager {

	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		clientId = clientId.trim();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(clientId,
				clientSecret);

		return this.authenticate(authRequest);

	}

	@Override
	public Authentication authenticate(Authentication arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		return arg0;
	}

}
