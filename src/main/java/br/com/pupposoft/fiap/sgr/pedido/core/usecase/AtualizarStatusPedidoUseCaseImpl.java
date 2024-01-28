package br.com.pupposoft.fiap.sgr.pedido.core.usecase;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AtualizarStatusPedidoUseCaseImpl implements AtualizarStatusPedidoUseCase {

	private PedidoGateway pedidoGateway;
	
	private ObterPedidoUseCase obterPedidoUseCase;
	
	@Override
	public void atualizarStatus(Long pedidoId, Status status) {
        PedidoDto pedidoDto = obterPedidoUseCase.obterPorId(pedidoId);
        
        Pedido pedido = Pedido.builder().id(pedidoId).status(Status.get(pedidoDto.getStatusId())).build();
        pedido.setStatus(status);
        
        this.pedidoGateway.atualizarStatus(PedidoDto.builder().id(pedidoId).statusId(Status.get(pedido.getStatus())).build());
	}
	
}
