package com.mauricio.cursomc.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mauricio.cursomc.domain.ItemPedido;
import com.mauricio.cursomc.domain.PagamentoComBoleto;
import com.mauricio.cursomc.domain.Pedido;
import com.mauricio.cursomc.domain.enums.EstadoPagamento;
import com.mauricio.cursomc.repositories.ItemPedidoRepository;
import com.mauricio.cursomc.repositories.PagamentoRepository;
import com.mauricio.cursomc.repositories.PedidoRepository;
import com.mauricio.cursomc.repositories.ProdutoRepository;
import com.mauricio.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = pedidoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(LocalDateTime.now());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstante());
		}
		pedido = pedidoRepository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0d);
			ip.setProduto(produtoRepository.findById(ip.getProduto().getId()).get());
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		System.out.println(pedido);
		return pedido;
	}

}
