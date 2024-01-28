package br.com.pupposoft.fiap.sgr.pedido.adapter.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.external.json.ProdutoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessProdutoServiceException;
import br.com.pupposoft.fiap.starter.http.HttpConnectGateway;
import br.com.pupposoft.fiap.starter.http.dto.HttpConnectDto;
import br.com.pupposoft.fiap.starter.http.exception.HttpConnectorException;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceHttpConnectUnitTest {
	
	@InjectMocks
	private ProdutoServiceHttpConnect produtoServiceHttpConnect;
	
	@Mock
	private HttpConnectGateway httpConnectGateway;

	@Mock
	private ObjectMapper mapper;

	@Test
	void shouldSucessOnObterPorId() throws Exception {
		
		final String responseBodyStr = getRandomString();
		doReturn(responseBodyStr).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		ProdutoJson produtoJson = ProdutoJson.builder().build();
		doReturn(produtoJson).when(mapper).readValue(responseBodyStr, ProdutoJson.class);
		
		final Long produtoId = getRandomLong();
		
		Optional<ProdutoDto> produtoDtoReturnedOP = produtoServiceHttpConnect.obterPorId(produtoId);
		assertTrue(produtoDtoReturnedOP.isPresent());
		
		ProdutoDto produtoDtoReturned = produtoDtoReturnedOP.get();
		
		assertEquals(produtoJson.getId(), produtoDtoReturned.getId());
		//Fazer demais asserts
	}
	
	@Test
	void shouldNotFoundOnObterPorId() throws Exception {
		
		HttpConnectorException httpConnectorExceptionMock = Mockito.mock(HttpConnectorException.class);
		doReturn(404).when(httpConnectorExceptionMock).getHttpStatus();
		doThrow(httpConnectorExceptionMock).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		final Long produtoId = getRandomLong();
		
		Optional<ProdutoDto> produtoDtoReturnedOP = produtoServiceHttpConnect.obterPorId(produtoId);
		assertFalse(produtoDtoReturnedOP.isPresent());
	}

	@Test
	void shouldErrorToAccessProdutoServiceExceptionOnObterPorId() throws Exception {
		doThrow(new RuntimeException()).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		final Long produtoId = getRandomLong();
		
		assertThrows(ErrorToAccessProdutoServiceException.class, () -> produtoServiceHttpConnect.obterPorId(produtoId));
		
	}
	
}
