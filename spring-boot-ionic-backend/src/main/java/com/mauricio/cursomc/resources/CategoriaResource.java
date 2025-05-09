package com.mauricio.cursomc.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mauricio.cursomc.domain.Categoria;
import com.mauricio.cursomc.dto.CategoriaDTO;
import com.mauricio.cursomc.services.CategoriaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@ApiOperation(value="Busca por id")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria obj = categoriaService.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Insere categoria")
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO categoriaDTO) {
		Categoria categoria = categoriaService.fromDTO(categoriaDTO);
		categoria = categoriaService.insert(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(categoria.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Atualiza categoria")
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
		Categoria categoria = categoriaService.fromDTO(categoriaDTO);
		categoria.setId(id);
		categoria = categoriaService.update(categoria);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@ApiOperation(value="Remove categoria")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		categoriaService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Retorna todas categorias")
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> categorias = categoriaService.findAll();
		List<CategoriaDTO> dtos = CategoriaDTO.generateList(categorias);
		return ResponseEntity.ok().body(dtos);
	}
	
	@ApiOperation(value="Retorna todas categorias com paginação")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="size", defaultValue="24") Integer size, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Categoria> categorias = categoriaService.findPage(page, size, orderBy, direction);
		Page<CategoriaDTO> dtos = CategoriaDTO.generatePage(categorias);
		return ResponseEntity.ok().body(dtos);
	}

}
