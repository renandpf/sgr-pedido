package br.com.pupposoft.fiap.sgr.pedido.component;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLocalDate;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
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
import br.com.pupposoft.fiap.sgr.pedido.core.exception.PedidoNotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SgrPedidoService.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WireMockTest
class ObterPedidosPorPedidoIdComponentTest extends ComponentTestBase {

	@Autowired
	private PedidoApiController pedidoApiController;
	
    @Autowired
    private PedidoEntityRepository pedidoEntityRepository;
    
    @Autowired
    private ItemEntityRepository itemEntityRepository;
	
    @BeforeEach
    public void initEach(){
    	cleanAllDatabase();
    }
    
	@Test
	void shouldSucess(WireMockRuntimeInfo wmRuntimeInfo) {
		
		PedidoEntity pedidoEntityA = PedidoEntity.builder()
				.statusId(1L)
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
				.statusId(2L)
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
		
		PedidoJson pedidoJsonFound = pedidoApiController.obterPorId(pedidoEntityA.getId());
		 
		assertEquals(pedidoEntityA.getId(), pedidoJsonFound.getId());
	}
	
	@Test
	void shouldPedidoNotFoundException(WireMockRuntimeInfo wmRuntimeInfo) {
		
		PedidoEntity pedidoEntityA = PedidoEntity.builder()
				.statusId(1L)
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
				.statusId(2L)
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

		Long pedidoId = 15L;
		assertThrows(PedidoNotFoundException.class, () -> pedidoApiController.obterPorId(pedidoId));
	}
	
}
