package br.com.pupposoft.fiap.sgr.pedido.core.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class DomainsUnitTest {
	
	@Test
	void cliente() {
		Cliente domain = Cliente.builder()
		.id(1L)
		.build();
		
		assertEquals(1L, domain.getId());
		
		domain.toString();
	}
	
	@Test
	void item() {
		Pedido pedido = Pedido.builder().build();
		Produto produto = Produto.builder().build();
		
		Item domain = Item.builder()
				.id(1L)
				.pedido(pedido)
				.produto(produto)
				.quantidade(10L)
				.valorUnitario(new BigDecimal("15"))
				.build();
		
		assertEquals(1L, domain.getId());
		assertEquals(pedido, domain.getPedido());
		assertEquals(produto, domain.getProduto());
		assertEquals(10L, domain.getQuantidade());
		assertEquals(new BigDecimal("15"), domain.getValorUnitario());
		
		domain.toString();
	}

	@Test
	void pagamento() {
		Pagamento domain = Pagamento.builder()
		.id(1L)
		.build();
		
		assertEquals(1L, domain.getId());
		
		domain.toString();
	}
	
	@Test
	void pedido() {
		Cliente cliente = Cliente.builder().build();
		List<Item> itens = Arrays.asList();
		List<Pagamento> pagamentos = Arrays.asList();
		Status status = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		Pedido domain = Pedido.builder()
				.id(1L)
				.cliente(cliente)
				.observacao("aaa")
				.status(status)
				.dataCadastro(LocalDate.of(2024, Month.JANUARY, 27))
				.dataConclusao(LocalDate.of(2024, Month.JANUARY, 28))
				.itens(itens)
				.pagamentos(pagamentos)
				.build();
		
		assertEquals(1L, domain.getId());
		assertEquals(cliente, domain.getCliente());
		assertEquals("aaa", domain.getObservacao());
		assertEquals(status, domain.getStatus());
		assertEquals(LocalDate.of(2024, Month.JANUARY, 27), domain.getDataCadastro());
		assertEquals(LocalDate.of(2024, Month.JANUARY, 28), domain.getDataConclusao());
		assertEquals(itens, domain.getItens());
		assertEquals(pagamentos, domain.getPagamentos());
		
		domain.toString();
	}
	
	@Test
	void produto() {
		Produto domain = Produto.builder()
		.id(1L)
		.build();
		
		assertEquals(1L, domain.getId());
		
		domain.toString();
	}
	
	@Test
	void usuario() {
		Usuario domain = Usuario.builder()
				.email("AAA")
				.password("BBB")
				.build();
		
		assertEquals("AAA", domain.getEmail());
		assertEquals("BBB", domain.getPassword());
		
		domain.toString();
	}
	
}
