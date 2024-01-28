package br.com.pupposoft.fiap.sgr.pedido.adapter.web;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.web.json.PedidoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.controller.PedidoController;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.PedidoNotFoundException;

@WebMvcTest(PedidoApiController.class)
class PedidoApiControllerIntTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PedidoController pedidoController;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void shouldSucessOnAtualizarStatus() throws Exception {
		
		final Long pedidoId = getRandomLong();
		final PedidoJson requestBody = PedidoJson.builder()
				.status(Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO)
				.build();
		
		this.mockMvc.perform(
				patch("/sgr/pedidos/ " + pedidoId + " /status")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(""));
		
		verify(pedidoController).atualizarStatus(pedidoId, requestBody.getStatus());
	}
	
	@Test
	void shouldPedidoNotFoundExceptionOnAtualizarStatus() throws Exception {
		
		final Long pedidoId = getRandomLong();
		final PedidoJson requestBody = PedidoJson.builder()
				.status(Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO)
				.build();
		
		doThrow(new PedidoNotFoundException()).when(pedidoController).atualizarStatus(pedidoId, requestBody.getStatus());
		
		this.mockMvc.perform(
				patch("/sgr/pedidos/ " + pedidoId + " /status")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isNotFound())
		.andExpect(content().json("{\"code\":\"sgr.pedidoNotFound\",\"message\":\"Pedido não foi encontrado\"}"));
		
		verify(pedidoController).atualizarStatus(pedidoId, requestBody.getStatus());
	}
	
	@Test
	void shouldUnespectedErrorOnAtualizarStatus() throws Exception {
		
		final Long pedidoId = getRandomLong();
		final PedidoJson requestBody = PedidoJson.builder()
				.status(Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO)
				.build();
		
		doThrow(new RuntimeException()).when(pedidoController).atualizarStatus(pedidoId, requestBody.getStatus());
		
		this.mockMvc.perform(
				patch("/sgr/pedidos/ " + pedidoId + " /status")
				.content(objectMapper.writeValueAsString(requestBody))
				.contentType(MediaType.APPLICATION_JSON))
		.andDo(print())
		.andExpect(status().isInternalServerError())
		.andExpect(content().json("{\"code\":\"INTERNAL_SERVER_ERROR\",\"message\":\"Internal Server Error\"}"));
		
		verify(pedidoController).atualizarStatus(pedidoId, requestBody.getStatus());
	}
	
}
