package com.clientes.bean.token;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenOauthBean {
	@JsonProperty("access_token") //solo si nuestra propiedad la llamos diferente de access_token que es lo que vamos a recibir en el json de respuesta a keycloak
	private String access_token;
}
