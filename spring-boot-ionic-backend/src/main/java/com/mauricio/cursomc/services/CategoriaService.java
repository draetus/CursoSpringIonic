package com.mauricio.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mauricio.cursomc.domain.Categoria;
import com.mauricio.cursomc.repositories.CategoriaRepository;
import com.mauricio.cursomc.services.exceptions.DataIntegrityException;
import com.mauricio.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = categoriaRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}
	
	public Categoria update(Categoria categoria) {
		find(categoria.getId());
		return categoriaRepository.save(categoria);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
		
	}
	
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

}
