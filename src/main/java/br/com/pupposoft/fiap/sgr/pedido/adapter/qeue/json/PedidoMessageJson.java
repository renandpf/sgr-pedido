package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Pedido;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PedidoMessageJson {
	private Long id;
	private ClienteJson cliente;
    private Status status;
    private Double valor;
    
    public PedidoMessageJson(Pedido pedido) {
    	id = pedido.getId();
    	cliente = new ClienteJson(pedido.getCliente());
    	status = pedido.getStatus();
    	valor = pedido.getValorTotal().doubleValue();
    }
}
