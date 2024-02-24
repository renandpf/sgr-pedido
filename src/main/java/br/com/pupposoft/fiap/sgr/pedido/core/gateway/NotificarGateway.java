package br.com.pupposoft.fiap.sgr.pedido.core.gateway;

import br.com.pupposoft.fiap.sgr.pedido.core.dto.NotificarDto;

public interface NotificarGateway {

	void notificar(NotificarDto dto);
	
}
