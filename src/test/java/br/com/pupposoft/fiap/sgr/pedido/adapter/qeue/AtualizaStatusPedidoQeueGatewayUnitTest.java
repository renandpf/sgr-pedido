package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json.PedidoMessageJson;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.usecase.AtualizarStatusPedidoUseCase;

@ExtendWith(MockitoExtension.class)
class AtualizaStatusPedidoQeueGatewayUnitTest {

	@InjectMocks
	private AtualizaStatusPedidoQeueGateway atualizaStatusPedidoQeueGateway;
	
	@Mock
	private AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
	
	@Mock
	private ObjectMapper mapper;
	
	
	@Test
	void shouldSucessOnAtualizar() throws Exception {
		final String msg = getRandomString();
		
		final Long pedidoId = getRandomLong();
		final Status newStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		PedidoMessageJson pedidoMessageJson = new PedidoMessageJson();
		setField(pedidoMessageJson, "id", pedidoId);
		setField(pedidoMessageJson, "status", newStatus);
		doReturn(pedidoMessageJson).when(mapper).readValue(msg, PedidoMessageJson.class);
		
		atualizaStatusPedidoQeueGateway.atualizar(msg);
		
		verify(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
	}
	
	@Test
	void shouldErrorOnAtualizar() throws Exception {
		final String msg = getRandomString();

		final Long pedidoId = getRandomLong();
		final Status newStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		PedidoMessageJson pedidoMessageJson = new PedidoMessageJson();
		setField(pedidoMessageJson, "id", pedidoId);
		setField(pedidoMessageJson, "status", newStatus);
		doReturn(pedidoMessageJson).when(mapper).readValue(msg, PedidoMessageJson.class);
		
		doThrow(new RuntimeException("any error")).when(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
		
		assertThrows(RuntimeException.class, () -> atualizaStatusPedidoQeueGateway.atualizar(msg)) ;
		
		verify(atualizarStatusPedidoUseCase).atualizarStatus(pedidoId, newStatus);
	}
	
}
