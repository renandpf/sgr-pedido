package br.com.pupposoft.fiap.starter.http;

import br.com.pupposoft.fiap.starter.http.dto.HttpConnectDto;

public interface HttpConnectGateway {
	public String get(HttpConnectDto dto);
}
