package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.*;

@ExtendWith(MockitoExtension.class)
class AtualizaStatusPedidoQeueGatewayUnitTest {

	@InjectMocks
	private AtualizaStatusPedidoQeueGateway atualizaStatusPedidoQeueGateway;
	
	@Mock
	private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
	
	@Test
	void shouldSucessOnAtualizar() {
		final Long pedidoId = getRandomLong();
		final Status newStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		atualizaStatusPedidoQeueGateway.atualizar(pedidoId, newStatus);
		
		verify(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
	}
	
	@Test
	void shouldErrorOnAtualizar() {
		final Long pedidoId = getRandomLong();
		final Status newStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		doThrow(new RuntimeException("any error")).when(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
		
		assertThrows(RuntimeException.class, () -> atualizaStatusPedidoQeueGateway.atualizar(pedidoId, newStatus)) ;
		
		verify(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
	}
	
}
