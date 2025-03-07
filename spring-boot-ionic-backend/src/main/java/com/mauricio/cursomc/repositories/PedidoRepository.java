package com.mauricio.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mauricio.cursomc.domain.Cliente;
import com.mauricio.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	
	@Transactional(readOnly = true)
	public Page<Pedido> findByCliente(Cliente cliente, Pageable pageable);

}
