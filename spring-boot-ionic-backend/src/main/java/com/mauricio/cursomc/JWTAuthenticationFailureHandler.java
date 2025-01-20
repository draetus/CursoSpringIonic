package com.mauricio.cursomc;

import static java.time.ZoneOffset.UTC;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(401);
		response.setContentType("application/json");
		response.getWriter().append(json());
		
	}
	
	private String json() {
		long date = LocalDateTime.now().toInstant(UTC).toEpochMilli();
		return "{\"timestamp\": " + date + ", "
				+ "\"status\": 401, "
				+ "\"message\": \"Email ou senha inválidos\", "
				+ "\"path\": \"/login\" }";
		
	}

}
