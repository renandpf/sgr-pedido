package br.com.pupposoft.fiap.sgr.pedido.core.usecase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PagamentoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.PagamentoNotFoundException;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.PedidoNotFoundException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PagamentoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ObterPedidoUseCaseImpl implements ObterPedidoUseCase {

	private PagamentoGateway pagamentoGateway;
	
	private PedidoGateway pedidoGateway;
	
	@Override
	public PedidoDto obterPorId(Long id) {
        Optional<PedidoDto> pedidoOp = this.pedidoGateway.obterPorId(id);
        if (pedidoOp.isEmpty()) {
            log.warn("Pedido não encontrado. id={}", id);
            throw new PedidoNotFoundException();
        }

        return pedidoOp.get();
	}

	@Override
	public List<PedidoDto> obterEmAndamento() {
        return this.pedidoGateway.obterPorStatus(Arrays.asList(
        		Status.PAGO, 
        		Status.EM_PREPARACAO));
	}

	@Override
	public PedidoDto obterPorIdentificadorPagamento(String identificadorPagamento) {
        Optional<PagamentoDto> pagamentoOp = this.pagamentoGateway.obterPorIdentificadorPagamento(identificadorPagamento);

        if (pagamentoOp.isEmpty()) {
            log.warn("Pagamento não encontrado com identificadorPagamento: {}", identificadorPagamento);
            throw new PagamentoNotFoundException();
        }
        
        PagamentoDto pagamentoDto = pagamentoOp.get();
        return obterPorId(pagamentoDto.getPedido().getId());
	}

}
