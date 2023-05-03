package com.mtz.vendasapi.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagensConstant {
	
	ERRO_GENERICO("Erro interno identificado. Contate o suporte."),

	ERRO_CLIENTE_NAO_ENCONTRADO("Cliente não encontrado."),
	ERRO_CLIENTE_CADASTRADA_ANTERIORMENTE("Cliente já possui cadastro."),
	ERRO_EXCLUIR_CLIENTE("Esse Cliente possui pedidos, não é possivel excluir."),

	ERRO_PEDIDO_CADASTRADO_ANTERIORMENTE("Pedido já possui cadastro."),
	ERRO_PEDIDO_NAO_ENCONTRADO("Pedido não encontrado."),
	//ERRO_EXCLUIR_PEDIDO("Esse Pedido não pode ser excluido."),

	ERRO_PRODUTO_CADASTRADO_ANTERIORMENTE("Produto já possui cadastro."),
	ERRO_PRODUTO_NAO_ENCONTRADO("Produto não encontrado."),
	ERRO_EXCLUIR_PRODUTO("Esse Produto está em algum pedido, não é possivel excluir."),

	ERRO_USUARIO_CADASTRADO_ANTERIORMENTE("Usuário já possui cadastro."),
	ERRO_USUARIO_NAO_ENCONTRADO("Usuário não encontrado."),
	ERRO_EXCLUIR_USUARIO("Esse Usuário possui pedidos, não é possivel excluir."),

	OK_EXCLUIDO_COM_SUCESSO("Excluido com sucesso."),

	ERRO_ID_INFORMADO("ID não pode ser informado na operação de cadastro.");
	
	private final String valor;
	
}
