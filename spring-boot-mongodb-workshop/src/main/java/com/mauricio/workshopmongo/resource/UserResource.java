package com.mauricio.workshopmongo.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mauricio.workshopmongo.domain.User;
import com.mauricio.workshopmongo.domain.dto.UserDTO;
import com.mauricio.workshopmongo.service.UserService;


@RestController
@RequestMapping(value = "/users")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@GetMapping()
	public ResponseEntity<List<UserDTO>> findAll() {
		List<User> users = userService.findAll();
		List<UserDTO> userDTOs = users.stream().map(UserDTO::new).toList();
		return ResponseEntity.ok(userDTOs);
	}
	

}
