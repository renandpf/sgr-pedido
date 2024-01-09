package br.com.pupposoft.fiap.sgr.pedido.adapter.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.ItemEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.PedidoEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.ItemEntityRepository;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.PedidoEntityRepository;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ClienteDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ItemDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessRepositoryException;
import br.com.pupposoft.fiap.sgr.pedido.core.gateway.PedidoGateway;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PedidoMySqlRepository implements PedidoGateway {

	@Autowired
	private PedidoEntityRepository pedidoEntityRepository;
	
	@Autowired
	private ItemEntityRepository itemEntityRepository;
	
	@Override
	@Transactional
	public Long criar(PedidoDto pedido) {
        try {
            PedidoEntity pedidoEntity = mapDtoToEntity(pedido);
            pedidoEntityRepository.save(pedidoEntity);
            Long pedidoCreatedId = pedidoEntity.getId();

            pedidoEntity.getItens().forEach(ie -> itemEntityRepository.save(ie));

            return pedidoCreatedId;

        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorToAccessRepositoryException();
        }
	}

	@Override
	public void atualizarStatus(PedidoDto pedido) {
        try {
            log.trace("Start pedido={}", pedido);
            PedidoEntity pedidoEntity = this.pedidoEntityRepository.findById(pedido.getId()).get();
            pedidoEntity.setStatusId(pedido.getStatusId());
            this.pedidoEntityRepository.save(pedidoEntity);
            log.trace("End");
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorToAccessRepositoryException();
        }
	}

	@Override
	public Optional<PedidoDto> obterPorId(Long pedidoId) {
        try {
            log.trace("Start pedidoId={}", pedidoId);
            Optional<PedidoEntity> pedidoEntityOp = this.pedidoEntityRepository.findById(pedidoId);
            
            Optional<PedidoDto> pedidoDtoOp = Optional.empty();
            if(pedidoEntityOp.isPresent()) {
            	PedidoEntity pedidoEntity = pedidoEntityOp.get();
            	
            	PedidoDto pedidoDto = mapEntityToDto(pedidoEntity);
            	
            	pedidoDtoOp = Optional.of(pedidoDto);
            }
            
            
            log.trace("End pedidoDtoOp={}", pedidoDtoOp);
            return pedidoDtoOp;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorToAccessRepositoryException();
        }
	}

	@Override
	public List<PedidoDto> obterPorStatus(List<Status> statusList) {
        try {
            log.trace("Start statusList={}", statusList);
            
            List<Long> statusIdList = statusList.stream().mapToLong(Status::get).boxed().toList();
            
            List<PedidoEntity> pedidoEntityList = this.pedidoEntityRepository.findByStatusIdIn(statusIdList);

            List<PedidoDto> pedidosDtos = pedidoEntityList.stream().map(this::mapEntityToDto).toList();
            
            log.trace("End pedidosDtos={}", pedidosDtos);
            return pedidosDtos;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorToAccessRepositoryException();
        }	
    }

	private PedidoDto mapEntityToDto(PedidoEntity pedidoEntity) {
		ClienteDto clienteDto = 
				pedidoEntity.getClienteId() == null ? null : ClienteDto.builder().id(pedidoEntity.getClienteId()).build();
		
		List<ItemDto> itensDto = pedidoEntity.getItens().stream()
				.map(ie -> ItemDto.builder()
						.id(ie.getId())
						.quantidade(ie.getQuantidade())
						.produto(ProdutoDto.builder()
								.id(ie.getProdutoId())
								.build())
						.valorUnitario(ie.getValorUnitario())
				.build())
			.toList();
		
		return PedidoDto.builder()
			.id(pedidoEntity.getId())
			.observacao(pedidoEntity.getObservacao())
			.statusId(pedidoEntity.getStatusId())
			.dataCadastro(pedidoEntity.getDataCadastro())
			.dataConclusao(pedidoEntity.getDataConclusao())
			.cliente(clienteDto)
			.itens(itensDto)
		.build();
	}
	
	private PedidoEntity mapDtoToEntity(PedidoDto pedidoDto) {
		
		List<ItemEntity> itens = new ArrayList<>();
		if(pedidoDto.hasItens()) {
			itens = pedidoDto.getItens().stream().map(i -> ItemEntity.builder()
						.id(i.getId())
						.quantidade(i.getQuantidade())
						.valorUnitario(i.getValorUnitario())
						.produtoId(i.getProduto().getId())
						.build()).toList();
		}
		
		PedidoEntity pedidoEntity = PedidoEntity.builder()
		.id(pedidoDto.getId())
		.statusId(pedidoDto.getStatusId())
		.dataCadastro(pedidoDto.getDataCadastro())
		.dataConclusao(pedidoDto.getDataConclusao())
		.observacao(pedidoDto.getObservacao())
		.itens(itens)
		.clienteId(pedidoDto.hasCliente() ? pedidoDto.getCliente().getId() : null)
		.build();
		
		itens.forEach(i -> i.setPedido(pedidoEntity));
		
		return pedidoEntity;
	}	
	
}
