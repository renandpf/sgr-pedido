package br.com.pupposoft.fiap.sgr.pedido.adapter.external;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pupposoft.fiap.sgr.pedido.adapter.external.json.ProdutoJson;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessProdutoServiceException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.ProdutoGateway;
import br.com.pupposoft.fiap.starter.http.HttpConnectGateway;
import br.com.pupposoft.fiap.starter.http.dto.HttpConnectDto;
import br.com.pupposoft.fiap.starter.http.exception.HttpConnectorException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProdutoServiceHttpConnect implements ProdutoGateway {

	@Value("${sgr.produto-service.url}")
	private String baseUrl;

	@NonNull
	private HttpConnectGateway httpConnectGateway;

	@NonNull
	private ObjectMapper mapper;

	@Override
	public Optional<ProdutoDto> obterPorId(Long produtoId) {
		try {

			return process(produtoId);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ErrorToAccessProdutoServiceException();
		}
	}

	private Optional<ProdutoDto> process(Long produtoId) throws Exception {
		Optional<ProdutoDto> produtoDtoOp = Optional.empty();
		try {
			final String url = baseUrl + "/sgr/gerencial/produtos/" + produtoId;

			HttpConnectDto httpConnectDto = HttpConnectDto.builder().url(url).build();

			final String response = httpConnectGateway.get(httpConnectDto);
			log.info("response={}", response);

			ProdutoJson produtoJson = mapper.readValue(response, ProdutoJson.class);
			ProdutoDto pedidoDto = mapJsonToDto(produtoJson);

			produtoDtoOp = Optional.of(pedidoDto);


		} catch (HttpConnectorException e) {
			if(e.getHttpStatus() == 404) {
				log.warn("Produto not found. produtoId={}", produtoId);
				produtoDtoOp = Optional.empty();
			} else {
				throw e;
			}
		}
		return produtoDtoOp;
	}

	private ProdutoDto mapJsonToDto(ProdutoJson produtoJson) {
		return ProdutoDto.builder()
				.id(produtoJson.getId())
				.nome(produtoJson.getNome())
				.descricao(produtoJson.getDescricao())
				.valor(produtoJson.getValor())
				.categoria(produtoJson.getCategoria())
				.build();
	}

}
