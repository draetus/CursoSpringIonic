package com.mauricio.cursomc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mauricio.cursomc.domain.Categoria;
import com.mauricio.cursomc.domain.Cidade;
import com.mauricio.cursomc.domain.Cliente;
import com.mauricio.cursomc.domain.Endereco;
import com.mauricio.cursomc.domain.Estado;
import com.mauricio.cursomc.domain.ItemPedido;
import com.mauricio.cursomc.domain.Pagamento;
import com.mauricio.cursomc.domain.PagamentoComBoleto;
import com.mauricio.cursomc.domain.PagamentoComCartao;
import com.mauricio.cursomc.domain.Pedido;
import com.mauricio.cursomc.domain.Produto;
import com.mauricio.cursomc.domain.enums.EstadoPagamento;
import com.mauricio.cursomc.domain.enums.TipoCliente;
import com.mauricio.cursomc.repositories.CategoriaRepository;
import com.mauricio.cursomc.repositories.CidadeRepository;
import com.mauricio.cursomc.repositories.ClienteRepository;
import com.mauricio.cursomc.repositories.EnderecoRepository;
import com.mauricio.cursomc.repositories.EstadoRepository;
import com.mauricio.cursomc.repositories.ItemPedidoRepository;
import com.mauricio.cursomc.repositories.PedidoRepository;
import com.mauricio.cursomc.repositories.ProdutoRepository;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		
		Produto p1 = new Produto(null, "Computador", 2000d);
		Produto p2 = new Produto(null, "Impressora", 800d);
		Produto p3 = new Produto(null, "Mouse", 80d);
		
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));
		
		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		
		clienteRepository.save(cli1);
		enderecoRepository.saveAll(Arrays.asList(e1, e2));
		
		Pedido ped1 = new Pedido(null, LocalDateTime.of(2017, 9, 30, 10, 32), cli1, e1);
		Pedido ped2 = new Pedido(null, LocalDateTime.of(2017, 10, 10, 19, 35), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, LocalDate.of(2017, 10, 20), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0d, 1, 2000d);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0d, 2, 80d);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100d, 1, 800d);
		
		ped1.getItems().addAll(Arrays.asList(ip1, ip2));
		ped2.getItems().addAll(Arrays.asList(ip3));
		
		p1.getItems().add(ip1);
		p2.getItems().add(ip3);
		p3.getItems().add(ip2);
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
	}

}
