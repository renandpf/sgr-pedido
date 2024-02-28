package br.com.pupposoft.fiap.sgr.config.di;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pupposoft.fiap.sgr.pedido.core.controller.PedidoController;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ClienteGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.NotificarGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PagamentoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ProdutoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.CriarPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.ObterPedidoUseCase;

@ExtendWith(MockitoExtension.class)
class PedidoDIConfigurationUnitTest {

	@InjectMocks
	private PedidoDIConfiguration pedidoDIConfiguration;
	
	@Mock
	private PagamentoGateway pagamentoGateway;

	@Mock
	private PedidoGateway pedidoGateway;

	@Mock
	private ClienteGateway clienteGateway;

	@Mock
	private ProdutoGateway produtoGateway;
	
	@Mock
	private NotificarGateway notificarGateway;

	@Test
	void atualizarStatusPedidoUseCase() {
		ObterPedidoUseCase obterPedidoUseCase = Mockito.mock(ObterPedidoUseCase.class);
		AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase = pedidoDIConfiguration.atualizarStatusPedidoUseCase(obterPedidoUseCase);
		
		assertEquals(pedidoGateway, getField(atualizarStatusPedidoUseCase, "pedidoGateway"));
		assertEquals(obterPedidoUseCase, getField(atualizarStatusPedidoUseCase, "obterPedidoUseCase"));
	}
	
	@Test
	void criarPedidoUseCase() {
		CriarPedidoUseCase criarPedidoUseCase = pedidoDIConfiguration.criarPedidoUseCase();
		
		assertEquals(pedidoGateway, getField(criarPedidoUseCase, "pedidoGateway"));
		assertEquals(produtoGateway, getField(criarPedidoUseCase, "produtoGateway"));
		assertEquals(pedidoGateway, getField(criarPedidoUseCase, "pedidoGateway"));
		assertEquals(notificarGateway, getField(criarPedidoUseCase, "notificarGateway"));
	}
	
	@Test
	void obterPedidoUseCase() {
		ObterPedidoUseCase obterPedidoUseCase = pedidoDIConfiguration.obterPedidoUseCase();
		
		assertEquals(pagamentoGateway, getField(obterPedidoUseCase, "pagamentoGateway"));
		assertEquals(pedidoGateway, getField(obterPedidoUseCase, "pedidoGateway"));
	}
	
	@Test
	void pedidoController() {
		ObterPedidoUseCase obterPedidoUseCase = Mockito.mock(ObterPedidoUseCase.class);
		CriarPedidoUseCase criarPedidoUseCase = Mockito.mock(CriarPedidoUseCase.class);
		AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase = Mockito.mock(AtualizarStatusPedidoUseCase.class);
		
		PedidoController pedidoController = pedidoDIConfiguration.pedidoController(obterPedidoUseCase, criarPedidoUseCase, atualizarStatusPedidoUseCase);
		
		assertEquals(obterPedidoUseCase, getField(pedidoController, "obterPedidoUseCase"));
		assertEquals(criarPedidoUseCase, getField(pedidoController, "criarPedidoUseCase"));
		assertEquals(atualizarStatusPedidoUseCase, getField(pedidoController, "atualizarStatusPedidoUseCase"));
	}
	
}
