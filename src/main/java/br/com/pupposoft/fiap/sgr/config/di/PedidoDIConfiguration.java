package br.com.pupposoft.fiap.sgr.config.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import br.com.pupposoft.fiap.sgr.pedido.core.controller.PedidoController;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ClienteGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.EfetuarPagamentoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.NotificarGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PagamentoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ProdutoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCaseImpl;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.CriarPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.CriarPedidoUseCaseImpl;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.ObterPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.ObterPedidoUseCaseImpl;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class PedidoDIConfiguration {

	private PagamentoGateway pagamentoGateway;
	
	private PedidoGateway pedidoGateway;
	
	private ClienteGateway clienteGateway;
	
	private ProdutoGateway produtoGateway;
	
	private NotificarGateway noticarGateway;

	private EfetuarPagamentoGateway efetuarPagamentoGateway;
	
	@Bean
	@Autowired
	@DependsOn("obterPedidoUseCase")
	public AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase(ObterPedidoUseCase obterPedidoUseCase) {
		return new AtualizarStatusPedidoUseCaseImpl(pedidoGateway, obterPedidoUseCase);
	}

	@Bean
	public CriarPedidoUseCase criarPedidoUseCase() {
		return new CriarPedidoUseCaseImpl(clienteGateway, produtoGateway, pedidoGateway, noticarGateway, efetuarPagamentoGateway);
	}
	
	@Bean
	public ObterPedidoUseCase obterPedidoUseCase() {
		return new ObterPedidoUseCaseImpl(pagamentoGateway, pedidoGateway);
	}
	
	@Bean
	@Autowired
	@DependsOn({"atualizarStatusPedidoUseCase", "criarPedidoUseCase", "obterPedidoUseCase"})
	public PedidoController pedidoController(
			ObterPedidoUseCase obterPedidoUseCase,
			CriarPedidoUseCase criarPedidoUseCase,
			AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase) {
		
		return new PedidoController(obterPedidoUseCase, criarPedidoUseCase, atualizarStatusPedidoUseCase);
	}
}
