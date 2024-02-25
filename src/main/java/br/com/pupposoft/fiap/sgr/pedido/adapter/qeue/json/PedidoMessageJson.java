package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class PedidoMessageJson {
	private Long id;
    private Status status;
}
