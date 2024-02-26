package br.com.pupposoft.fiap.sgr.pedido.core.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificarDto implements Serializable {
	private static final long serialVersionUID = 7106282608704706864L;
	
	private List<String> destinatarios;
	private String assunto;
	private String conteudo;
}
