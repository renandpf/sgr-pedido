package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.EfetuarPagamentoGateway;
import lombok.extern.slf4j.Slf4j;

@Profile({"!prd"})
@Slf4j
@Component
public class EfetuarPagamantoMockQeueGateway implements EfetuarPagamentoGateway {

	@Override
	public void efetuarPagamento(Pedido pedido) {
		log.warn("### MOCK ###");
	}

}
