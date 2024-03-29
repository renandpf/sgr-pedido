package br.com.pupposoft.fiap.sgr.pedido.core.exception;

import br.com.pupposoft.fiap.starter.exception.SystemBaseException;
import lombok.Getter;

@Getter
public class CamposObrigatoriosNaoPreechidoException extends SystemBaseException {
	private static final long serialVersionUID = 7983307394018672758L;

	private String code = "sgr.camposObrigatoriosNaoPreenchido";//NOSONAR
    private String message = "";//NOSONAR
    private Integer httpStatus = 400;//NOSONAR
    
    CamposObrigatoriosNaoPreechidoException(String message){
    	this.message = message;
    }
}
