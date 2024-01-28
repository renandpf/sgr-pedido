package br.com.pupposoft.fiap.sgr.pedido.core.controller;

import java.util.List;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.CriarPedidoUseCase;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.ObterPedidoUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PedidoController {

    private ObterPedidoUseCase obterPedidoUseCase;
	
    private CriarPedidoUseCase criarPedidoUseCase;

    private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
	
    public List<PedidoDto> obterEmAndamento() {
        return obterPedidoUseCase.obterEmAndamento();
    }
    
    public PedidoDto obterPorId(Long pedidoId) {
        return obterPedidoUseCase.obterPorId(pedidoId);
    }

    public Long criar(PedidoDto pedidoDto) {
        return this.criarPedidoUseCase.criar(pedidoDto);
    }

    public void atualizarStatus(Long id, Status status) {
        this.atualizarStatusPedidoUseCase.atualizarStatus(id, status);
    }
    
    public PedidoDto obterPorIdentificadorPagamento(String identificadorPagamento) {
        return this.obterPedidoUseCase.obterPorIdentificadorPagamento(identificadorPagamento);
    }    
}
