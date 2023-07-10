package com.mauricio.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mauricio.cursomc.domain.Cliente;
import com.mauricio.cursomc.dto.ClienteDTO;
import com.mauricio.cursomc.repositories.ClienteRepository;
import com.mauricio.cursomc.services.exceptions.DataIntegrityException;
import com.mauricio.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newCliente = find(cliente.getId());
		updateData(newCliente, cliente);
		return clienteRepository.save(cliente);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente porque há entidades relacionadas");
		}
		
	}
	
	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer size, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, size, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(
				clienteDTO.getId(), 
				clienteDTO.getNome(), 
				clienteDTO.getEmail(), 
				null, 
				null);
	}
	
	private void updateData(Cliente clienteNew, Cliente cliente) {
		clienteNew.setNome(cliente.getNome());
		clienteNew.setEmail(cliente.getEmail()); 
	}

}
