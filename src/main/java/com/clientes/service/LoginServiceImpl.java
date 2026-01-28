package com.clientes.service;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
	private final WebClient webClient;
	
	@Override
	public String optenerToken(String user, String pass) {
		
		return  webClient
				.post()
				.uri("http://localhost:8080/login?user={u}&pass={p}", user, pass)
				.retrieve()
				.onStatus(HttpStatusCode ::isError,
						response ->response.bodyToMono(String.class)
							.map(msg-> new RuntimeException(
									"Error contactos-ms | status=" 
											+ response.statusCode() +
								            " | body=" + msg)
							)
				)
				.bodyToMono(String.class)
				.block();
	}

}
