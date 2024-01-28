package br.com.pupposoft.fiap.sgr.pedido.component;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLocalDate;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import br.com.pupposoft.fiap.SgrPedidoService;
import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.ItemEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.PedidoEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.ItemEntityRepository;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.PedidoEntityRepository;
import br.com.pupposoft.fiap.sgr.pedido.adapter.web.PedidoApiController;
import br.com.pupposoft.fiap.sgr.pedido.adapter.web.json.PedidoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PagamentoGateway;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SgrPedidoService.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WireMockTest
class ObterPedidosPorIdentificadorPagamentoComponentTest {

	@Autowired
	private PedidoApiController pedidoApiController;
	
    @Autowired
    private PagamentoGateway pagamentoGateway;

    @Autowired
    private PedidoEntityRepository pedidoEntityRepository;
    
    @Autowired
    private ItemEntityRepository itemEntityRepository;
	
	@Test
	void shouldSucess(WireMockRuntimeInfo wmRuntimeInfo) {
		
		setField(pagamentoGateway, "baseUrl", wmRuntimeInfo.getHttpBaseUrl());
		
		final String identificadorPagamento = getRandomString();
		
		PedidoEntity pedidoEntityA = PedidoEntity.builder()
				.statusId(getRandomLong())
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.observacao(getRandomString())
				.clienteId(getRandomLong())
				.build();
		
		ItemEntity itemEntity = ItemEntity.builder()
				.quantidade(1L)
				.valorUnitario(15D)
				.produtoId(15L)
				.build();
		
		PedidoEntity pedidoEntityB = PedidoEntity.builder()
				.statusId(1L)
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.observacao(getRandomString())
				.clienteId(getRandomLong())
				.itens(Arrays.asList(itemEntity))
				.build();
		
		itemEntity.setPedido(pedidoEntityB);
		
		pedidoEntityRepository.save(pedidoEntityA);
		pedidoEntityRepository.save(pedidoEntityB);
		
		itemEntityRepository.saveAndFlush(itemEntity);
		
		final String pedidoResponseBodyStr = "{\n"
				+ "  \"id\": 1,\n"
				+ "  \"identificadorPagamento\": \" "+ identificadorPagamento +" \",\n"
				+ "  \"pedidoId\": " + pedidoEntityB.getId() + "\n"
				+ "}"; 
		
		stubFor(get("/sgr/pagamentos/identificador-pagamento-externo/" + identificadorPagamento).willReturn(okJson(pedidoResponseBodyStr)));
		

		
		 PedidoJson pedidoJsonFound = pedidoApiController.obterPedidosPorIdentificadorPagamento(identificadorPagamento);
		 
		 assertEquals(pedidoEntityB.getId(), pedidoJsonFound.getId());
	}
	
}
