package com.clientes.service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.clientes.model.PersonaBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonaAsynServiceImpl implements PersonaAsynService {
	
	private String urlBase = "http://localhost:8080/";
	private final WebClient webClient;
	@Value("${app.user.user2}")
    private String user2;
    @Value("${app.pass.user2}")
    private String passUser2;
    @Value("${app.user.user1}")
    private String user1;
    @Value("${app.pass.user1}")
    private String passUser1;


	@Async
	public CompletableFuture<List<PersonaBean>> llamadaAsincrona(PersonaBean persona) {
		
		webClient
				.post()
				.uri(urlBase + "/contacto")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(persona)
				.header("Authorization", "Basic " + stringToBase64( user2, passUser2))
				.retrieve()
				.onStatus(HttpStatusCode ::isError,
						response ->response.bodyToMono(String.class)
							.map(msg-> new RuntimeException(
									"Error contactos-ms | status=" 
											+ response.statusCode() +
								            " | body=" + msg
									)
							)
				)
				.bodyToMono(PersonaBean.class)
				.block();
		
		PersonaBean[] personasResponse = webClient
				.get()
				.uri(urlBase + "/contactos")
				.header("Authorization", "Basic " + stringToBase64(user1, passUser1))
				.retrieve()
				.bodyToMono(PersonaBean[].class)
				.block();
		
				
		return CompletableFuture.completedFuture(Arrays.asList(personasResponse));
	}
	
	private String stringToBase64(String usuario, String pass) {
		String cadena = usuario  + ":" + pass;
		return Base64.getEncoder().encodeToString(cadena.getBytes());
		
		
	}

}
