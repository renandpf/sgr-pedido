package br.com.pupposoft.fiap.sgr.pedido.adapter.qeue.json;

import br.com.pupposoft.fiap.sgr.pedido.core.domain.Cliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ClienteJson {
	private Long id;
	private String nome;
	private String email;
	private String telefone;
	
	public ClienteJson(Cliente cliente) {
		id = cliente.getId();
		nome = cliente.getNome();
		email = cliente.getEmail();
		telefone = cliente.getTelefone();
	}

}
