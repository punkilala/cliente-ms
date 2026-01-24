package com.clientes.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.clientes.model.PersonaBean;

public interface PersonaAsynService {
	CompletableFuture<List<PersonaBean>> llamadaAsincrona(PersonaBean persona);

}
