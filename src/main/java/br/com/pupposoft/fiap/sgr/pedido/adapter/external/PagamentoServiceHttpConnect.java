package br.com.pupposoft.fiap.sgr.pedido.adapter.external;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.external.json.PagamentoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PagamentoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessProdutoServiceException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PagamentoGateway;
import br.com.pupposoft.fiap.starter.http.HttpConnectGateway;
import br.com.pupposoft.fiap.starter.http.dto.HttpConnectDto;
import br.com.pupposoft.fiap.starter.http.exception.HttpConnectorException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagamentoServiceHttpConnect implements PagamentoGateway {

	@Value("${sgr.pagamento-service.url}")
	private String baseUrl;

	@NonNull
	private HttpConnectGateway httpConnectGateway;

	@NonNull
	private ObjectMapper mapper;

	@Override
	public Optional<PagamentoDto> obterPorIdentificadorPagamento(String identificadorPagamento) {
		try {
			return process(identificadorPagamento);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ErrorToAccessProdutoServiceException();
		}
	}

	private Optional<PagamentoDto> process(String identificadorPagamento) throws Exception{
		try {
			final String url = baseUrl + "/sgr/pagamentos/identificador-pagamento-externo/" + identificadorPagamento;

			HttpConnectDto httpConnectDto = HttpConnectDto.builder().url(url).build();

			final String response = httpConnectGateway.get(httpConnectDto);
			log.info("response={}", response);

			PagamentoJson produtoJson = mapper.readValue(response, PagamentoJson.class);
			PagamentoDto pedidoDto = mapJsonToDto(produtoJson);

			return Optional.of(pedidoDto);


		} catch (HttpConnectorException e) {
			if(e.getHttpStatus() == 404) {
				log.warn("Pagamento not found. identificadorPagamento={}", identificadorPagamento);
				return Optional.empty();
			} else {
				throw e;
			}
		}
	}
	
	private PagamentoDto mapJsonToDto(PagamentoJson pagamentoJson) {
		return PagamentoDto.builder()
				.id(pagamentoJson.getId())
				.identificadorPagamento(pagamentoJson.getIdentificadorPagamento())
				.pedido(PedidoDto.builder().id(pagamentoJson.getPedidoId()).build())
				.build();
	}
	
}
