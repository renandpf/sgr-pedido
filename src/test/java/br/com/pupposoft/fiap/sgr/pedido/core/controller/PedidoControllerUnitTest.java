package br.com.pupposoft.fiap.sgr.pedido.core.controller;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.CriarPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.ObterPedidoUseCase;

@ExtendWith(MockitoExtension.class)
class PedidoControllerUnitTest {
	
	@InjectMocks
	private PedidoController pedidoController;
	
	@Mock
    private ObterPedidoUseCase obterPedidoUseCase;
	
	@Mock
    private CriarPedidoUseCase criarPedidoUseCase;

	@Mock
    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;

	@Test
	void shouldSucessOnObterEmAndamento() {
		
		PedidoDto pedidoDto = PedidoDto.builder().build();
		doReturn(Arrays.asList(pedidoDto)).when(obterPedidoUseCase).obterEmAndamento();
		List<PedidoDto> obterEmAndamento = pedidoController.obterEmAndamento();
		
		assertEquals(pedidoDto, obterEmAndamento.get(0));
		
		verify(obterPedidoUseCase).obterEmAndamento();
	}
	
	@Test
	void shouldSucessOnObterPorId() {
		final Long pedidoId = getRandomLong();
		
		PedidoDto pedidoDto = PedidoDto.builder().build();
		doReturn(pedidoDto).when(obterPedidoUseCase).obterPorId(pedidoId);
		PedidoDto pedidoDtoFound = pedidoController.obterPorId(pedidoId);
		
		assertEquals(pedidoDto, pedidoDtoFound);
		
		verify(obterPedidoUseCase).obterPorId(pedidoId);
	}
	
	@Test
	void shouldSucessOnCriar() {
		final PedidoDto pedidoDto = PedidoDto.builder().build();
		
		Long pedidoId = getRandomLong();
		doReturn(pedidoId).when(criarPedidoUseCase).criar(pedidoDto);
		Long pedidoIdretuned = pedidoController.criar(pedidoDto);
		
		assertEquals(pedidoId, pedidoIdretuned);
		
		verify(criarPedidoUseCase).criar(pedidoDto);
	}
	
	@Test
	void shouldSucessOnAtualizarStatus() {
		final Long id = getRandomLong();
		final Status status = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		pedidoController.atualizarStatus(id, status);
		
		verify(atualizarStatusPedidoUseCase).atualizarStatus(id, status);
	}
	
	@Test
	void shouldSucessOnObterPorIdentificadorPagamento() {
		final String identificadorPagamento = getRandomString();
		
		PedidoDto pedidoDtoFound = PedidoDto.builder().build();
		doReturn(pedidoDtoFound).when(obterPedidoUseCase).obterPorIdentificadorPagamento(identificadorPagamento);
		
		PedidoDto pedidoDtoReturned = pedidoController.obterPorIdentificadorPagamento(identificadorPagamento);
		
		assertEquals(pedidoDtoFound, pedidoDtoReturned);
		
		verify(obterPedidoUseCase).obterPorIdentificadorPagamento(identificadorPagamento);
	}
	
}
