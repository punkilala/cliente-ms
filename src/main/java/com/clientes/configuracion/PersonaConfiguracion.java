package com.clientes.configuracion;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.reactive.function.client.WebClient;

@EnableAsync
@Configuration
public class PersonaConfiguracion {
	//crear bean para las llamadas con RestTemplate
		@Bean
		public WebClient webClient() {
			return WebClient.create();
		}
		
		//para llamadar asincronas
		@Bean
		public Executor executor() {
			return new ThreadPoolTaskExecutor();
		}
}
