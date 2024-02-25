package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json.PedidoMessageJson;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prd")
@AllArgsConstructor
@Slf4j
public class AtualizaStatusPedidoQeueGateway {

	private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
	
	private ObjectMapper mapper;

	@JmsListener(destination = "${sgr.queue.atualiza-status-pedido}", containerFactory = "statusPedidoSqsFactory")
	public void atualizar(String pedidoMessageJsonStr) {
		try {
			log.info("Received pedidoMessageJsonStr: {}", pedidoMessageJsonStr);
			
			PedidoMessageJson pedidoMessageJson = mapper.readValue(pedidoMessageJsonStr, PedidoMessageJson.class);
			
			atualizarStatusPedidoUseCase.atualizarStatus(pedidoMessageJson.getId(), pedidoMessageJson.getStatus());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);//NOSONAR
		}
	}
	
}
