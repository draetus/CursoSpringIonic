package com.mauricio.cursomc.dto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.mauricio.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	
	public CategoriaDTO() {}
	
	public CategoriaDTO(Categoria categoria) {
		this.id = categoria.getId();
		this.nome = categoria.getNome();
	}
	
	public static List<CategoriaDTO> generateList(List<Categoria> categorias) {
		return categorias.stream().map(CategoriaDTO::new).collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
