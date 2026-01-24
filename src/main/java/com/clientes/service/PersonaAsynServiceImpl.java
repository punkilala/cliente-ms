package com.clientes.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Async
	@Override
	public CompletableFuture<List<PersonaBean>> llamadaAsincrona(PersonaBean persona) {
		
		webClient
				.post()
				.uri(urlBase + "/contacto")
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(persona)
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
				.uri(urlBase + "/contacto")
				.retrieve()
				.bodyToMono(PersonaBean[].class)
				.block();
		
				
		return CompletableFuture.completedFuture(Arrays.asList(personasResponse));
	}

}
