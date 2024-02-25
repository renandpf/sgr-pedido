package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prd")
@AllArgsConstructor
@Slf4j
public class AtualizaStatusPedidoQeueGateway {

	private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

	public void atualizar(Long pedidoId, Status status) {
		try {
			atualizarStatusPedidoUseCase.atualizarStatus(pedidoId, status);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
}
