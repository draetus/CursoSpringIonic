package com.mauricio.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mauricio.cursomc.domain.Cidade;
import com.mauricio.cursomc.domain.Estado;
import com.mauricio.cursomc.dto.CidadeDTO;
import com.mauricio.cursomc.dto.EstadoDTO;
import com.mauricio.cursomc.services.CidadeService;
import com.mauricio.cursomc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@GetMapping
	public ResponseEntity<List<EstadoDTO>> findAll() {
		
		List<Estado> estados = estadoService.findAll();
		List<EstadoDTO> dtos = estados.stream().map(EstadoDTO::new).toList();
		
		return ResponseEntity.ok().body(dtos);
	}
	
	@GetMapping(value = "/{estadoId}/cidades")
	public ResponseEntity<List<CidadeDTO>> findAllCidades(@PathVariable Integer estadoId) {
		
		List<Cidade> cidades = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> dtos = cidades.stream().map(CidadeDTO::new).toList();
		
		return ResponseEntity.ok().body(dtos);
	}

}
