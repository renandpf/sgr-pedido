package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomDouble;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json.PedidoMessageJson;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Cliente;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToNotifiyException;

@ExtendWith(MockitoExtension.class)
class EfetuarPagamentoAwsSqsGatewayUnitTest {

	@InjectMocks
	private EfetuarPagamentoAwsSqsGateway efetuarPagamentoAwsSqsGateway;
	
	@Mock
	private JmsTemplate efetuarPagamentoTemplate;
	
	@Mock
	private ObjectMapper mapper;
	
	@Test
	void ShouldSucessOnNotificar() throws Exception {
		
		Cliente cliente = Cliente.builder()
				.id(getRandomLong())
				.nome(getRandomString())
				.email(getRandomString())
				.telefone(getRandomString())
				.build();
		
		
		Pedido pedido = Mockito.mock(Pedido.class);
		doReturn(getRandomLong()).when(pedido).getId();
		doReturn(new BigDecimal(getRandomDouble())).when(pedido).getValorTotal();
		doReturn(cliente).when(pedido).getCliente();

		String dtoJson = getRandomString();
		doReturn(dtoJson).when(mapper).writeValueAsString(any(PedidoMessageJson.class));
		
		efetuarPagamentoAwsSqsGateway.efetuarPagamento(pedido);
		
		ArgumentCaptor<PedidoMessageJson> pedidoMessageJsonAC = ArgumentCaptor.forClass(PedidoMessageJson.class);
		verify(mapper).writeValueAsString(pedidoMessageJsonAC.capture());
		
		PedidoMessageJson pedidoMessageJson = pedidoMessageJsonAC.getValue();
		
		assertEquals(pedido.getId(), pedidoMessageJson.getId());
		assertEquals(pedido.getStatus(), pedidoMessageJson.getStatus());
		assertEquals(pedido.getValorTotal().doubleValue(), pedidoMessageJson.getValor());
		assertEquals(pedido.getCliente().getId(), pedidoMessageJson.getCliente().getId());
		assertEquals(pedido.getCliente().getNome(), pedidoMessageJson.getCliente().getNome());
		assertEquals(pedido.getCliente().getEmail(), pedidoMessageJson.getCliente().getEmail());
		assertEquals(pedido.getCliente().getTelefone(), pedidoMessageJson.getCliente().getTelefone());
		
		verify(efetuarPagamentoTemplate).convertAndSend("efetuar-pagamento-qeue", dtoJson);
	}
	
	@Test
	void ShouldErrorOnefEtuarPagamento() throws Exception {
		
		Cliente cliente = Cliente.builder()
				.id(getRandomLong())
				.nome(getRandomString())
				.email(getRandomString())
				.telefone(getRandomString())
				.build();
		
		Pedido pedido = Mockito.mock(Pedido.class);
		doReturn(getRandomLong()).when(pedido).getId();
		doReturn(new BigDecimal(getRandomDouble())).when(pedido).getValorTotal();
		doReturn(cliente).when(pedido).getCliente();

	
		String dtoJson = getRandomString();
		doReturn(dtoJson).when(mapper).writeValueAsString(any(PedidoMessageJson.class));

		doThrow(new RuntimeException()).when(efetuarPagamentoTemplate).convertAndSend("efetuar-pagamento-qeue", dtoJson);
		
		assertThrows(ErrorToNotifiyException.class, () -> efetuarPagamentoAwsSqsGateway.efetuarPagamento(pedido));
		
		verify(efetuarPagamentoTemplate).convertAndSend("efetuar-pagamento-qeue", dtoJson);
	}
	
}
