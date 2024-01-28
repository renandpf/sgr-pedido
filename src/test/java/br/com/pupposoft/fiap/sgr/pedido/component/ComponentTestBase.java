package br.com.pupposoft.fiap.sgr.pedido.component;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.ItemEntityRepository;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.PedidoEntityRepository;

public abstract class ComponentTestBase {

	@Autowired
	private PedidoEntityRepository pedidoEntityRepository;
	
	@Autowired
	private ItemEntityRepository itemEntityRepository;
	
	protected void cleanAllDatabase() {
		itemEntityRepository.deleteAll();
		pedidoEntityRepository.deleteAll();
	}
	
}
