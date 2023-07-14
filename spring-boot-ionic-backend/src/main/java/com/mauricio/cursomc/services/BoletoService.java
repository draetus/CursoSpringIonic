package com.mauricio.cursomc.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mauricio.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, LocalDateTime instanteDoPedido) {
		pagto.setDataVencimento(instanteDoPedido.plusDays(7l).toLocalDate());
	}

}
