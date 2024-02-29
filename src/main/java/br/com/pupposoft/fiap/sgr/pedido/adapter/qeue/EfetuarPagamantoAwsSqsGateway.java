package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json.PedidoMessageJson;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToNotifiyException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.EfetuarPagamentoGateway;
import lombok.extern.slf4j.Slf4j;

@Profile({"prd"})
@Slf4j
@Component
public class EfetuarPagamantoAwsSqsGateway implements EfetuarPagamentoGateway {

	@Autowired//NOSONAR
	private JmsTemplate efetuarPagamentoTemplate;
	
	@Autowired//NOSONAR
	private ObjectMapper mapper;
	
	@Async//Para não travar o fluxo de criação de pedido 
	@Override
	public void efetuarPagamento(Pedido pedido) {
		try {
			
			String message = mapper.writeValueAsString(new PedidoMessageJson(pedido));
			
			efetuarPagamentoTemplate.convertAndSend("efetuar-pagamento-qeue", message);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ErrorToNotifiyException();
		}
	}

}
