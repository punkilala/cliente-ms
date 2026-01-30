package com.clientes.service;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.clientes.bean.token.TokenOauthBean;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	private final WebClient.Builder webClient;
	
	String urlService="http://localhost:8000";
	String urlKeycloak="http://localhost:8070/realms/ContactosRealm/protocol/openid-connect/token";
	private String USERNAME="admin";
	private String PASSWORD="1234";
	private String CLIENT_ID="loginContactos";
	private String GRANT_TYPE="password";
	
	@Override
	public String optenerToken() {
		
		TokenOauthBean token =  webClient.build()
				.post()
				.uri(urlKeycloak)
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(BodyInserters.fromFormData("grant_type", GRANT_TYPE)
		                .with("client_id", CLIENT_ID)
		                .with("username", USERNAME)
		                .with("password", PASSWORD)
		        )
				.retrieve()
				.onStatus(HttpStatusCode ::isError,
						response ->response.bodyToMono(String.class)
							.map(msg-> new RuntimeException(
									"Error contactos-ms | status=" 
											+ response.statusCode() +
								            " | body=" + msg)
							)
				)
				.bodyToMono(TokenOauthBean.class)
				.block();
		
		return token.getAccess_token();
	}

}
