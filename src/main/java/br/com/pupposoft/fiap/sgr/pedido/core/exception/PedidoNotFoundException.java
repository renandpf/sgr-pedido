package br.com.pupposoft.fiap.sgr.pedido.core.exception;

import br.com.pupposoft.fiap.starter.exception.SystemBaseException;
import lombok.Getter;

@Getter
public class PedidoNotFoundException extends SystemBaseException {
	private static final long serialVersionUID = -950524946944182966L;
	
	private String code = "sgr.pedidoNotFound";//NOSONAR
    private String message = "Pedido não foi encontrado";//NOSONAR
    private Integer httpStatus = 404;//NOSONAR

	
}
