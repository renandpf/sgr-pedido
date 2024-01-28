package br.com.pupposoft.fiap.sgr.pedido.adapter.repository;

import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomDouble;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLocalDate;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomLong;
import static br.com.pupposoft.fiap.test.databuilder.DataBuilderBase.getRandomString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.pupposoft.fiap.sgr.config.database.pedido.entity.PedidoEntity;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.ItemEntityRepository;
import br.com.pupposoft.fiap.sgr.config.database.pedido.repository.PedidoEntityRepository;
import br.com.pupposoft.fiap.sgr.pedido.core.domain.Status;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ClienteDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ItemDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.PedidoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.dto.ProdutoDto;
import br.com.pupposoft.fiap.sgr.pedido.core.exception.ErrorToAccessRepositoryException;

@ExtendWith(MockitoExtension.class)
class PedidoMySqlRepositoryUnitTest {
	
	@InjectMocks
	private PedidoMySqlRepository pedidoMySqlRepository;
	
	@Mock
	private PedidoEntityRepository pedidoEntityRepository;
	
	@Mock
	private ItemEntityRepository itemEntityRepository;

	@Test
	void shouldSucessOnCriar() {
		
		ItemDto itemDto = ItemDto.builder()
				.id(getRandomLong())
				.quantidade(getRandomLong())
				.valorUnitario(getRandomDouble())
				.produto(ProdutoDto.builder().id(getRandomLong()).build())
				.build();
		
		PedidoDto pedidoDtoToSave = PedidoDto.builder()
				.id(getRandomLong())
				.statusId(getRandomLong())
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.observacao(getRandomString())
				.cliente(ClienteDto.builder().id(getRandomLong()).build())
				.itens(Arrays.asList(itemDto))
				.build();
		
		PedidoEntity pedidoEntityCreated = PedidoEntity.builder().id(pedidoDtoToSave.getId()).build();
		doReturn(pedidoEntityCreated).when(pedidoEntityRepository).save(any(PedidoEntity.class));
		
		Long pedidoId = pedidoMySqlRepository.criar(pedidoDtoToSave);
		
		assertEquals(pedidoEntityCreated.getId(), pedidoId);
		
		ArgumentCaptor<PedidoEntity> pedidoEntitySavedAC = ArgumentCaptor.forClass(PedidoEntity.class);
		
		verify(pedidoEntityRepository).save(pedidoEntitySavedAC.capture());
		
		PedidoEntity pedidoEntityCaptured = pedidoEntitySavedAC.getValue();
		
		assertEquals(pedidoDtoToSave.getId(), pedidoEntityCaptured.getId());
		assertEquals(pedidoDtoToSave.getStatusId(), pedidoEntityCaptured.getStatusId());
		assertEquals(pedidoDtoToSave.getDataCadastro(), pedidoEntityCaptured.getDataCadastro());
		assertEquals(pedidoDtoToSave.getDataConclusao(), pedidoEntityCaptured.getDataConclusao());
		assertEquals(pedidoDtoToSave.getObservacao(), pedidoEntityCaptured.getObservacao());
		assertEquals(pedidoDtoToSave.getCliente().getId(), pedidoEntityCaptured.getClienteId());
		assertEquals(pedidoDtoToSave.getItens().size(), pedidoEntityCaptured.getItens().size());
		assertEquals(pedidoDtoToSave.getItens().get(0).getId(), pedidoEntityCaptured.getItens().get(0).getId());
		assertEquals(pedidoDtoToSave.getItens().get(0).getQuantidade(), pedidoEntityCaptured.getItens().get(0).getQuantidade());
		assertEquals(pedidoDtoToSave.getItens().get(0).getValorUnitario(), pedidoEntityCaptured.getItens().get(0).getValorUnitario());
		assertEquals(pedidoDtoToSave.getItens().get(0).getProduto().getId(), pedidoEntityCaptured.getItens().get(0).getProdutoId());
	}

	@Test
	void shouldErrorToAccessRepositoryExceptionOnCriar() {
		ItemDto itemDto = ItemDto.builder()
				.id(getRandomLong())
				.quantidade(getRandomLong())
				.valorUnitario(getRandomDouble())
				.produto(ProdutoDto.builder().id(getRandomLong()).build())
				.build();
		
		PedidoDto pedidoDtoToSave = PedidoDto.builder()
				.id(getRandomLong())
				.statusId(getRandomLong())
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.observacao(getRandomString())
				.cliente(ClienteDto.builder().id(getRandomLong()).build())
				.itens(Arrays.asList(itemDto))
				.build();
		
		
		doThrow(new RuntimeException()).when(pedidoEntityRepository).save(any(PedidoEntity.class));
		
		assertThrows(ErrorToAccessRepositoryException.class, () -> pedidoMySqlRepository.criar(pedidoDtoToSave));
	}
	
	@Test
	void shouldSucessOnAtualizarStatus() {
		
		PedidoDto pedidoDtoToSave = PedidoDto.builder()
				.id(getRandomLong())
				.statusId(getRandomLong())
				.build();
		
		PedidoEntity pedidoEntityExistent = PedidoEntity.builder().id(pedidoDtoToSave.getId()).build();
		doReturn(Optional.of(pedidoEntityExistent)).when(pedidoEntityRepository).findById(pedidoDtoToSave.getId());
		
		pedidoMySqlRepository.atualizarStatus(pedidoDtoToSave);
		
		ArgumentCaptor<PedidoEntity> pedidoEntitySavedAC = ArgumentCaptor.forClass(PedidoEntity.class);
		
		verify(pedidoEntityRepository).save(pedidoEntitySavedAC.capture());
		
		PedidoEntity pedidoEntityCaptured = pedidoEntitySavedAC.getValue();
		assertEquals(pedidoEntityExistent, pedidoEntityCaptured);
		
		assertEquals(pedidoDtoToSave.getId(), pedidoEntityCaptured.getId());
		assertEquals(pedidoDtoToSave.getStatusId(), pedidoEntityCaptured.getStatusId());
	}
	
	@Test
	void shouldErrorOnAtuAlizarStatus() {
		
		PedidoDto pedidoDtoToSave = PedidoDto.builder()
				.id(getRandomLong())
				.statusId(getRandomLong())
				.build();
		
		doThrow(new RuntimeException()).when(pedidoEntityRepository).findById(pedidoDtoToSave.getId());
		
		assertThrows(ErrorToAccessRepositoryException.class, () -> pedidoMySqlRepository.atualizarStatus(pedidoDtoToSave));
	}
	
	@Test
	void shouldSucessOnObterPorId() {
		final Long pedidoId = getRandomLong();

		PedidoEntity pedidoEntityExistent = PedidoEntity.builder().id(pedidoId)
				.observacao(getRandomString())
				.statusId(getRandomLong())
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.itens(Arrays.asList())
				.build();
		doReturn(Optional.of(pedidoEntityExistent)).when(pedidoEntityRepository).findById(pedidoId);

		Optional<PedidoDto> pedidoReturnedOP = pedidoMySqlRepository.obterPorId(pedidoId);
		
		assertTrue(pedidoReturnedOP.isPresent());
		
		PedidoDto pedidoDtoReturned = pedidoReturnedOP.get();
		
		assertEquals(pedidoEntityExistent.getId(), pedidoDtoReturned.getId());
		assertEquals(pedidoEntityExistent.getObservacao(), pedidoDtoReturned.getObservacao());
		assertEquals(pedidoEntityExistent.getStatusId(), pedidoDtoReturned.getStatusId());
		assertEquals(pedidoEntityExistent.getDataCadastro(), pedidoDtoReturned.getDataCadastro());
		assertEquals(pedidoEntityExistent.getDataConclusao(), pedidoDtoReturned.getDataConclusao());
		
		verify(pedidoEntityRepository).findById(pedidoId);
	}
	
	@Test
	void shouldSucessNotFoundOnObterPorId() {
		final Long pedidoId = getRandomLong();

		doReturn(Optional.empty()).when(pedidoEntityRepository).findById(pedidoId);

		Optional<PedidoDto> pedidoReturnedOP = pedidoMySqlRepository.obterPorId(pedidoId);
		
		assertTrue(pedidoReturnedOP.isEmpty());
		
		verify(pedidoEntityRepository).findById(pedidoId);
	}
	
	@Test
	void shouldErrorToAccessRepositoryExceptionOnObterPorId() {
		final Long pedidoId = getRandomLong();

		doThrow(new RuntimeException()).when(pedidoEntityRepository).findById(pedidoId);

		assertThrows(ErrorToAccessRepositoryException.class, () -> pedidoMySqlRepository.obterPorId(pedidoId));
		
		verify(pedidoEntityRepository).findById(pedidoId);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void shouldSucessOnObterPorStatus() {
		final Status pedidoStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;

		PedidoEntity pedidoEntityExistent = PedidoEntity.builder().id(getRandomLong())
				.observacao(getRandomString())
				.statusId(getRandomLong())
				.dataCadastro(getRandomLocalDate())
				.dataConclusao(getRandomLocalDate())
				.itens(Arrays.asList())
				.build();
		doReturn(Arrays.asList(pedidoEntityExistent)).when(pedidoEntityRepository).findByStatusIdIn(anyList());

		List<PedidoDto> pedidosFound = pedidoMySqlRepository.obterPorStatus(Arrays.asList(pedidoStatus));
		
		assertFalse(pedidosFound.isEmpty());
		
		PedidoDto pedidoDtoReturned = pedidosFound.get(0);
		
		assertEquals(pedidoEntityExistent.getId(), pedidoDtoReturned.getId());
		assertEquals(pedidoEntityExistent.getObservacao(), pedidoDtoReturned.getObservacao());
		assertEquals(pedidoEntityExistent.getStatusId(), pedidoDtoReturned.getStatusId());
		assertEquals(pedidoEntityExistent.getDataCadastro(), pedidoDtoReturned.getDataCadastro());
		assertEquals(pedidoEntityExistent.getDataConclusao(), pedidoDtoReturned.getDataConclusao());
		
		ArgumentCaptor<List<Long>> statusArgCaptor = ArgumentCaptor.forClass(List.class);
		
		verify(pedidoEntityRepository).findByStatusIdIn(statusArgCaptor.capture());
		
		List<Long> statusList = statusArgCaptor.getValue();
		
		assertEquals(1, statusList.size());
		assertEquals(pedidoStatus.ordinal(), statusList.get(0));
	}
	
	@Test
	void shouldErrorToAccessRepositoryExceptionOnObterPorStatus() {
		final Status pedidoStatus = Status.AGUARDANDO_CONFIRMACAO_PAGAMENTO;
		
		doThrow(new RuntimeException()).when(pedidoEntityRepository).findByStatusIdIn(anyList());
		
		List<Status> statusList = Arrays.asList(pedidoStatus);
		assertThrows(ErrorToAccessRepositoryException.class, () -> pedidoMySqlRepository.obterPorStatus(statusList));
	}
	
}
