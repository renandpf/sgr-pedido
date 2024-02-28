package br.com.pupposoft.fiap.sgr.pedido.adapter.external;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.*;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.external.json.ClienteJson;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ClienteDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessProdutoServiceException;
import br.com.pupposoft.fiap.starter.http.HttpConnectGateway;
import br.com.pupposoft.fiap.starter.http.dto.HttpConnectDto;
import br.com.pupposoft.fiap.starter.http.exception.HttpConnectorException;

@ExtendWith(MockitoExtension.class)
class ClienteServiceHttpConnectUnitTest {
	
	@InjectMocks
	private ClienteServiceHttpConnect clienteServiceHttpConnect;
	
	@Mock
	private HttpConnectGateway httpConnectGateway;

	@Mock
	private ObjectMapper mapper;

	@Test
	void shouldSucessOnObterPorId() throws Exception {
		
		final String responseBodyStr = getRandomString();
		doReturn(responseBodyStr).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		ClienteJson clienteJson = ClienteJson.builder()
				.id(getRandomLong())
				.nome(getRandomString())
				.cpf(getRandomString())
				.email(getRandomString())
				.telefone(getRandomString())
				.build();
		doReturn(clienteJson).when(mapper).readValue(responseBodyStr, ClienteJson.class);
		
		final Long clienteId = getRandomLong();
		
		Optional<ClienteDto> clienteDtoReturnedOP = clienteServiceHttpConnect.obterPorId(clienteId);
		assertTrue(clienteDtoReturnedOP.isPresent());
		
		ClienteDto clienteDtoReturned = clienteDtoReturnedOP.get();
		
		assertEquals(clienteJson.getId(), clienteDtoReturned.getId());
		assertEquals(clienteJson.getNome(), clienteDtoReturned.getNome());
		assertEquals(clienteJson.getCpf(), clienteDtoReturned.getCpf());
		assertEquals(clienteJson.getEmail(), clienteDtoReturned.getEmail());
		assertEquals(clienteJson.getTelefone(), clienteDtoReturned.getTelefone());
	}
	
	@Test
	void shouldNotFoundOnObterPorId() throws Exception {
		
		HttpConnectorException httpConnectorExceptionMock = Mockito.mock(HttpConnectorException.class);
		doReturn(404).when(httpConnectorExceptionMock).getHttpStatus();
		doThrow(httpConnectorExceptionMock).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		final Long clienteId = getRandomLong();
		
		Optional<ClienteDto> clienteDtoReturnedOP = clienteServiceHttpConnect.obterPorId(clienteId);
		assertFalse(clienteDtoReturnedOP.isPresent());
	}

	@Test
	void shouldErrorToAccessProdutoServiceExceptionOnObterPorId() throws Exception {
		doThrow(new RuntimeException()).when(httpConnectGateway).get(any(HttpConnectDto.class));
		
		final Long clienteId = getRandomLong();
		
		assertThrows(ErrorToAccessProdutoServiceException.class, () -> clienteServiceHttpConnect.obterPorId(clienteId));
		
	}
	
}
