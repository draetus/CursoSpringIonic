package com.mauricio.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mauricio.cursomc.domain.Cidade;
import com.mauricio.cursomc.domain.Cliente;
import com.mauricio.cursomc.domain.Endereco;
import com.mauricio.cursomc.domain.enums.Perfil;
import com.mauricio.cursomc.domain.enums.TipoCliente;
import com.mauricio.cursomc.dto.ClienteDTO;
import com.mauricio.cursomc.dto.ClienteNewDTO;
import com.mauricio.cursomc.repositories.ClienteRepository;
import com.mauricio.cursomc.repositories.EnderecoRepository;
import com.mauricio.cursomc.security.UserSS;
import com.mauricio.cursomc.services.exceptions.AuthorizationException;
import com.mauricio.cursomc.services.exceptions.DataIntegrityException;
import com.mauricio.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente cliente) {
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
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
			throw new DataIntegrityException("Não é possível excluir um cliente porque há pedidos relacionados");
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
				null,
				null);
	}
	
	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cliente = new Cliente(
				null, 
				clienteDTO.getNome(), 
				clienteDTO.getEmail(), 
				clienteDTO.getCpfOuCnpj(), 
				TipoCliente.toEnum(clienteDTO.getTipo()),
				bCryptPasswordEncoder.encode(clienteDTO.getSenha()));
		
		Endereco endereco = new Endereco(
				null, 
				clienteDTO.getLogradouro(), 
				clienteDTO.getNumero(), 
				clienteDTO.getComplemento(),
				clienteDTO.getBairro(), 
				clienteDTO.getCep(), 
				cliente, 
				new Cidade(clienteDTO.getCidadeId(), null, null));
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteDTO.getTelefone1());
		if (clienteDTO.getTelefone2()!=null) {
			cliente.getTelefones().add(clienteDTO.getTelefone2());
		}
		if (clienteDTO.getTelefone3()!=null) {
			cliente.getTelefones().add(clienteDTO.getTelefone3());
		}
		return cliente;
	}
	
	private void updateData(Cliente clienteNew, Cliente cliente) {
		clienteNew.setNome(cliente.getNome());
		clienteNew.setEmail(cliente.getEmail()); 
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = prefix + user.getId() + ".jpg";
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}

}
