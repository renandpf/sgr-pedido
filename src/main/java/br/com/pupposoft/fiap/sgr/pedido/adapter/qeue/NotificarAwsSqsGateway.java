package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.pupposoft.fiap.sgr.pedido.core.dto.NotificarDto;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.NotificarGateway;

@Profile({"prd"})
@Component
public class NotificarAwsSqsGateway implements NotificarGateway{

	@Override
	public void notificar(NotificarDto dto) {
		// TODO Implementar
		
	}

}
