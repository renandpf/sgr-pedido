package br.com.pupposoft.fiap.sgr.pedido.core.usecase;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Cliente;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Item;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Produto;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ClienteDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ItemDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.NotificarDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToNotifiyException;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ProdutoNotFoundException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ClienteGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.EfetuarPagamentoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.NotificarGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ProdutoGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class CriarPedidoUseCaseImpl implements CriarPedidoUseCase {

	private ClienteGateway clienteGateway;

	private ProdutoGateway produtoGateway;

	private PedidoGateway pedidoGateway;

	private NotificarGateway notificarGateway;

	private EfetuarPagamentoGateway efetuarPagamentoGateway;

	@Override
	public Long criar(PedidoDto pedidoDto) {
		Pedido pedido = mapDtoToDomain(pedidoDto);

		Optional<ClienteDto> clienteDtoOp = clienteGateway.obterPorId(pedido.getCliente().getId());

		this.verificaRemoveClienteInexistente(clienteDtoOp, pedido);
		this.carregaProdutosIntoPedido(pedido);

		pedido.setStatus(Status.RECEBIDO);//Execução de regras dentro do domain

		Long pedidoId = this.pedidoGateway.criar(mapDomainToDto(pedido));

		pedido.setId(pedidoId);

		notificaCliente(pedido, clienteDtoOp, pedidoId);

		efetuarPagamentoGateway.efetuarPagamento(pedido);

		return pedidoId;
	}


	private void notificaCliente(Pedido pedido, Optional<ClienteDto> clienteDtoOp, Long pedidoId) {
		if(clienteDtoOp.isPresent()) {
			ClienteDto clienteDto = clienteDtoOp.get();
			List<String> destinarios = Arrays.asList(clienteDto.getEmail(), clienteDto.getTelefone());

			try {
				notificarGateway.notificar(NotificarDto.builder()
						.assunto("Status pedido: " + pedidoId)
						.conteudo("O status do seu pedido é " + pedido.getStatus().name())
						.destinatarios(destinarios)
						.build());

			} catch (ErrorToNotifiyException e) {
				log.warn("Erro ao notificar o cliente");
			}

		}
	}

	private Pedido mapDtoToDomain(PedidoDto pedidoDto) {

		Cliente cliente = null;
		if(pedidoDto.hasCliente()) {
			cliente = Cliente.builder()
					.id(pedidoDto.getCliente().getId())
					.nome(pedidoDto.getCliente().getNome())
					.email(pedidoDto.getCliente().getEmail())
					.telefone(pedidoDto.getCliente().getTelefone())
					.build();
		}

		return Pedido.builder()
				.cliente(cliente)
				.dataCadastro(LocalDate.now())
				.observacao(pedidoDto.getObservacao())
				.itens(pedidoDto.getItens()
						.stream()
						.map(i -> Item.builder()
								.id(i.getId())
								.quantidade(i.getQuantidade())
								.produto(Produto.builder().id(i.getProduto().getId()).build())
								.build()).toList())
				.build();
	}


	private void verificaRemoveClienteInexistente(Optional<ClienteDto> clienteDtoOp, Pedido pedido) {
		if(clienteDtoOp.isEmpty()) {
			pedido.removerCliente();
		} else {
			ClienteDto clienteDto = clienteDtoOp.get();

			Cliente cliente = Cliente.builder()
					.id(clienteDto.getId())
					.nome(clienteDto.getNome())
					.email(clienteDto.getEmail())
					.telefone(clienteDto.getTelefone())
					.build();

			pedido.setCliente(cliente);
		}
	}

	private void carregaProdutosIntoPedido(Pedido pedido) {
		pedido.getItens().forEach(i -> {
			Produto produto = i.getProduto();
			Optional<ProdutoDto> produtoOp = produtoGateway.obterPorId(produto.getId());
			ProdutoDto pDto = produtoOp.orElseThrow(ProdutoNotFoundException::new);
			i.setProduto(Produto.builder().id(pDto.getId()).build());
			i.setValorUnitario(pDto.getValor());
		});
	}

	private PedidoDto mapDomainToDto(Pedido pedido) {

		ClienteDto clienteDto = null;
		if(pedido.temCliente()) {
			clienteDto = ClienteDto.builder()
					.id(pedido.getCliente().getId())
					.email(pedido.getCliente().getEmail())
					.telefone(pedido.getCliente().getTelefone())
					.nome(pedido.getCliente().getNome())
					.build();
		}

		final List<ItemDto> itens = pedido
				.getItens()
				.stream()
				.map(i -> ItemDto.builder()
						.produto(ProdutoDto.builder().id(i.getProduto().getId()).build())
						.quantidade(i.getQuantidade())
						.valorUnitario(i.getValorUnitario().doubleValue())
						.build())
				.toList();

		return PedidoDto.builder()
				.id(pedido.getId())
				.observacao(pedido.getObservacao())
				.statusId(Status.get(pedido.getStatus()))
				.dataCadastro(pedido.getDataCadastro())
				.cliente(clienteDto)
				.itens(itens)

				.build();
	}
}
