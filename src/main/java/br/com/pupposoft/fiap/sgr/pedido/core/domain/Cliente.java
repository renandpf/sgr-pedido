package br.com.pupposoft.fiap.sgr.pedido.core.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class Cliente {
	private Long id;
	private String nome;
	private String email;
	private String telefone;
}
