package br.com.pupposoft.fiap.sgr.pedido.component;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import br.com.pupposoft.fiap.SgrPedidoService;
import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.PedidoEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.PedidoEntityRepository;
import br.com.pupposoft.fiap.sgr.pedido.adapter.web.PedidoApiController;
import br.com.pupposoft.fiap.sgr.pedido.adapter.web.json.PedidoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ClienteGateway;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SgrPedidoService.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WireMockTest
class CriarPedidoComponentTest {

	@Autowired
	private PedidoApiController pedidoApiController;
	
    @Autowired
    private ClienteGateway clienteGateway;

    @Autowired
    private PedidoEntityRepository pedidoEntityRepository;
	
	@Test
	void shouldSucessOnCriar(WireMockRuntimeInfo wmRuntimeInfo) {
		
		setField(clienteGateway, "baseUrl", wmRuntimeInfo.getHttpBaseUrl());
		
		PedidoJson pedidoJson = PedidoJson.builder()
				.observacao(getRandomString())
				.status(Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO)
				.clienteId(1L)
				.build();
		
		final String pedidoResponseBodyStr = "{\n"
				+ "  \"id\": 1,\n"
				+ "  \"nome\": \"Exemplo Cliente\",\n"
				+ "  \"cpf\": \"123.456.789-00\",\n"
				+ "  \"email\": \"cliente@example.com\"\n"
				+ "}"; 
		
		stubFor(get("/sgr/gerencial/clientes/" + pedidoJson.getClienteId()).willReturn(okJson(pedidoResponseBodyStr)));
		
		List<PedidoEntity> pedidosExistentes = pedidoEntityRepository.findAll();
		assertTrue(pedidosExistentes.isEmpty());
		
		Long pedidoId = pedidoApiController.criar(pedidoJson);

		pedidosExistentes = pedidoEntityRepository.findAll();
		
		assertFalse(pedidosExistentes.isEmpty());
		assertEquals(1, pedidosExistentes.size());
		
		PedidoEntity pedidoSalvo = pedidosExistentes.get(0);
		
		assertEquals(pedidoId, pedidoSalvo.getId());
		assertEquals(pedidoJson.getObservacao(), pedidoSalvo.getObservacao());
		assertEquals(0L, pedidoSalvo.getStatusId());
		assertEquals(pedidoJson.getClienteId(), pedidoSalvo.getClienteId());
	}
	
}
