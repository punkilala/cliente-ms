package com.clientes.model.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.clientes.model.PersonaBean;

@RestController
public class PersonaController {
	@Autowired
	private WebClient webClient;
	private String urlBase = "http://localhost:8080/";
	
	@GetMapping(value = "/persona/{nombre}/{email}/{edad}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaBean> altaPersona(
			@PathVariable("nombre") String nombre,
			@PathVariable("email") String email,
			@PathVariable("edad") int edad){
		
		PersonaBean persona = new PersonaBean(nombre, email, edad);
		
		//alta a contactos-ms
		PersonaBean respuesta = webClient
			.post() //devuelve RequestBodyUriSpec
			.uri(urlBase + "/contacto" ) //devuelve ResquestBodySpec
			.contentType(MediaType.APPLICATION_JSON) //devuelve ResquestBoySpec
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
			.retrieve()//lanza llamada y devuel ve ResponseSpec
			.bodyToMono(PersonaBean[].class)
			.block();
				
		return Arrays.asList(personas);	
	}
	
//	@GetMapping(value = "/persona/{edad1}/{edad2}")
//	public List<PersonaBean> buscarPorRandoEdad (
//			@PathVariable ("edad1") int edad1,
//			@PathVariable ("edad2")int edad2) {
//		PersonaBean[] personas = restTemplate.getForObject(urlBase + "/contactos", PersonaBean[].class);
//		return Arrays.stream(personas).filter(p->p.getEdad() >= edad1 && p.getEdad() <= edad2 ).collect(Collectors.toList());
//		
//	}

}
