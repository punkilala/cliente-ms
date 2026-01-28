package com.clientes.model.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.clientes.model.PersonaBean;
import com.clientes.service.LoginService;
import com.clientes.service.PersonaAsynService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PersonaController {
	private final PersonaAsynService personaAsynService;
	private final LoginService loginService;
	private final WebClient webClient;
	private String urlBase = "http://localhost:8080/";
	private String user = "admin";
	private String pass = "1234";
			
	
	@GetMapping(value = "/persona/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PersonaBean>> altaPersona(
			@PathVariable("nombre") String nombre,
			@PathVariable("email") String email,
			@PathVariable("edad") int edad){
		
		PersonaBean persona = new PersonaBean(nombre, email, edad);
		
		String token = loginService.optenerToken(user, pass);
		
		//alta a contactos-ms
		PersonaBean respuesta = webClient
			.post() //devuelve RequestBodyUriSpec
			.uri(urlBase + "/contacto" ) //devuelve ResquestBodySpec
			.contentType(MediaType.APPLICATION_JSON) //devuelve ResquestBoySpec
			 .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.bodyValue(persona)//devuelve RequestHeadersSpec
			.retrieve() //lanza llamada y devuel ve ResponseSpec
			.onStatus(HttpStatusCode:: isError,
						response -> response.bodyToMono(String.class)
							.map(msg-> new RuntimeException(
											"Error contactos-ms | status=" 
											+ response.statusCode() +
								            " | body=" + msg
								       )
							)
					)//en caso de error del servicio
			.bodyToMono(PersonaBean.class)
			.block(); //esperamos a recibir datos porque este servicio no es reactivo
			
		//recuperar lista contactos
		PersonaBean[] personas = webClient
			.get()//RequestHeadersUriSpec
			.uri(urlBase + "/contactos"
					+ "" ) //devuelve ResquestHeaderSpec
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
			.retrieve()//lanza llamada y devuel ve ResponseSpec
			.bodyToMono(PersonaBean[].class)
			.block();
		
		return new ResponseEntity<List<PersonaBean>>( Arrays.asList(personas) ,HttpStatusCode.valueOf(200));

	}
	
	//alta asincrona 
	@GetMapping(value = "/personaAsyn/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public CompletableFuture<List<PersonaBean>> altaPersonaAsyn (@PathVariable("nombre") String nombre, @PathVariable("email") String email, @PathVariable("edad") int edad){
		PersonaBean persona = new PersonaBean(nombre, email, edad);
		return personaAsynService.llamadaAsincrona(persona);
		
	}
	

}
