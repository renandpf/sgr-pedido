package br.com.pupposoft.fiap.sgr.pedido.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import br.com.pupposoft.fiap.starter.exception.SystemBaseException;

class ExceptionUnitTest {

	@Test
	void alteracaoStatusPedidoException() {
		SystemBaseException exception = new AlteracaoStatusPedidoException();
		assertEquals("sgr.alteracaoStatusPedido", exception.getCode());
		assertEquals("O status atual do pedido não permite essa alteração.", exception.getMessage());
		assertEquals(422, exception.getHttpStatus());
	}
	
	@Test
	void camposObrigatoriosNaoPreechidoException() {
		String message = "aaa";
		SystemBaseException exception = new CamposObrigatoriosNaoPreechidoException(message);
		assertEquals("sgr.camposObrigatoriosNaoPreenchido", exception.getCode());
		assertEquals(message, exception.getMessage());
		assertEquals(400, exception.getHttpStatus());
	}

	@Test
	void errorToAccessClienteServiceException() {
		SystemBaseException exception = new ErrorToAccessClienteServiceException();
		assertEquals("sgr.errorToAccessClientService", exception.getCode());
		assertEquals("Erro ao acessar o serviço cliente", exception.getMessage());
		assertEquals(500 , exception.getHttpStatus());
	}
	
	@Test
	void errorToAccessProdutoServiceException() {
		SystemBaseException exception = new ErrorToAccessProdutoServiceException();
		assertEquals("sgr.errorToAccessProductService", exception.getCode());
		assertEquals("Erro ao acessar o serviço produto", exception.getMessage());
		assertEquals(500 , exception.getHttpStatus());
	}
	
	@Test
	void errorToAccessRepositoryException() {
		SystemBaseException exception = new ErrorToAccessRepositoryException();
		assertEquals("sgr.errorToAccessRepository", exception.getCode());
		assertEquals("Erro ao acessar repositório de dados", exception.getMessage());
		assertEquals(400 , exception.getHttpStatus());
	}
	
	@Test
	void pagamentoNotFoundException() {
		SystemBaseException exception = new PagamentoNotFoundException();
		assertEquals("sgr.pagamentoNotFound", exception.getCode());
		assertEquals("Pagamento não foi encontrado", exception.getMessage());
		assertEquals(404 , exception.getHttpStatus());
	}
	
	@Test
	void pedidoNotFoundException() {
		SystemBaseException exception = new PedidoNotFoundException();
		assertEquals("sgr.pedidoNotFound", exception.getCode());
		assertEquals("Pedido não foi encontrado", exception.getMessage());
		assertEquals(404 , exception.getHttpStatus());
	}
	
	@Test
	void produtoNotFoundException() {
		SystemBaseException exception = new ProdutoNotFoundException();
		assertEquals("sgr.produtoNotFound", exception.getCode());
		assertEquals("Produto não foi encontrado", exception.getMessage());
		assertEquals(404 , exception.getHttpStatus());
	}
	
}
