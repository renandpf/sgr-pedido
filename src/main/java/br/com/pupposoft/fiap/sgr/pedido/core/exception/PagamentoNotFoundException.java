package br.com.pupposoft.fiap.sgr.pedido.core.exception;

import br.com.pupposoft.fiap.starter.exception.SystemBaseException;
import lombok.Getter;

@Getter
public class PagamentoNotFoundException extends SystemBaseException {
	private static final long serialVersionUID = 6885625333620163032L;
	
	private String code = "sgr.pagamentoNotFound";//NOSONAR
    private String message = "Pagamento não foi encontrado";//NOSONAR
    private Integer httpStatus = 404;//NOSONAR
}
