package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.pupposoft.fiap.sgr.pedido.core.dto.NotificarDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToNotifiyException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.NotificarGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile({"prd"})
@Component
@AllArgsConstructor
@Slf4j
public class NotificarAwsSqsGateway implements NotificarGateway{

	
	private JmsTemplate notifyClienteTemplate;
	
	@Async//Para não travar o fluxo de criação de pedido 
	@Override
	public void notificar(NotificarDto dto) {
		
		try {
			
			notifyClienteTemplate.convertAndSend("notificar-qeue", dto);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ErrorToNotifiyException();
		}

		
	}

}
