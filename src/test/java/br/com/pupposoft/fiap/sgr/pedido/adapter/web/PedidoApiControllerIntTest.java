package br.com.pupposoft.fiap.sgr.pedido.adapter.web;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

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
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ClienteDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ItemDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PagamentoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.PedidoNotFoundException;

@WebMvcTest(PedidoApiController.class)
class PedidoApiControllerIntTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PedidoController pedidoController;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void shouldSucessOnObterEmAndamento() throws Exception {
		
		PedidoDto pedidoDto = PedidoDto.builder()
				.id(1L)
				.observacao("AAA")
				.statusId(2L)
				.dataCadastro(LocalDate.of(2023, Month.JANUARY, 28))
				.dataConclusao(LocalDate.of(2023, Month.JANUARY, 28))
				.cliente(ClienteDto.builder().id(3L).build())
				.itens(Arrays.asList(ItemDto.builder()
						.id(4L)
						.produto(ProdutoDto.builder().id(20L).build())
						.quantidade(5L)
						.valorUnitario(55D)
						.build()))
				.pagamentos(Arrays.asList(PagamentoDto.builder().id(5L).build()))
				.build();
		
		doReturn(Arrays.asList(pedidoDto)).when(pedidoController).obterEmAndamento();
		
		this.mockMvc.perform(get("/sgr/pedidos/andamento"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().json("[{\"id\":1,\"observacao\":\"AAA\",\"status\":\"PAGO\",\"dataCadastro\":\"2023-01-28\",\"dataConclusao\":\"2023-01-28\",\"clienteId\":3,\"itens\":[{\"id\":4,\"quantidade\":5,\"produtoId\":20,\"valorUnitario\":55.0}]}]"));
		
		verify(pedidoController).obterEmAndamento();
	}
	
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
