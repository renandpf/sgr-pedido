package br.com.pupposoft.fiap.sgr.pedido.core.gateway;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;

public interface EfetuarPagamentoGateway {
	
	void efetuarPagamento(Pedido pedido);

}
