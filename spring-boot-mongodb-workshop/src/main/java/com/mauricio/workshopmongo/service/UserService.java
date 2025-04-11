package com.mauricio.workshopmongo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mauricio.workshopmongo.domain.User;
import com.mauricio.workshopmongo.repository.UserRepository;
import com.mauricio.workshopmongo.service.exception.ObjectNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findById(String id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new ObjectNotFoundException("Objeto não encontrado");
		}
		
		return user.get();
	}

}
