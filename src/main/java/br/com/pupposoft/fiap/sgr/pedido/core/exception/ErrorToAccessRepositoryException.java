package br.com.pupposoft.fiap.sgr.pedido.core.exception;

import br.com.pupposoft.fiap.starter.exception.SystemBaseException;
import lombok.Getter;

@Getter
public class ErrorToAccessRepositoryException extends SystemBaseException {
	private static final long serialVersionUID = 6729291256372725161L;
	
	private final String code = "sgr.errorToAccessRepository";//NOSONAR
	private final String message = "Erro ao acessar repositório de dados";//NOSONAR
	private final Integer httpStatus = 400;//NOSONAR
}
