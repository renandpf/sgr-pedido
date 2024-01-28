package br.com.pupposoft.fiap.sgr.pedido.core.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.*;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusPedidoUseCaseImplUnitTest {

	@InjectMocks
	private AtualizarStatusPedidoUseCaseImpl atualizarStatusPedidoUseCaseImpl;
	
	@Mock
	private PedidoGateway pedidoGateway;
	
	@Mock
	private ObterPedidoUseCase obterPedidoUseCase;

	@Test
	void atualizarStatus() {
		final Long pedidoId = getRandomLong();
		final Status status = Status.RECEBIDO;
		
		PedidoDto pedidoDtoFound = PedidoDto.builder()
				.id(pedidoId)
				.statusId(0L)
				.build();
		doReturn(pedidoDtoFound).when(obterPedidoUseCase).obterPorId(pedidoId);
		
		atualizarStatusPedidoUseCaseImpl.atualizarStatus(pedidoId, status);
		
		
		
		ArgumentCaptor<PedidoDto> pedidoDtoAC = ArgumentCaptor.forClass(PedidoDto.class);
		verify(pedidoGateway).atualizarStatus(pedidoDtoAC.capture());
		
		
		PedidoDto pedidoDtoSent = pedidoDtoAC.getValue();
		
		assertEquals(pedidoId, pedidoDtoSent.getId());
		assertEquals(pedidoDtoFound.getStatusId(), pedidoDtoSent.getStatusId());
	}
	
}
