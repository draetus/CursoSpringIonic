package com.mauricio.workshopmongo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mauricio.workshopmongo.domain.Post;
import com.mauricio.workshopmongo.repository.PostRepository;
import com.mauricio.workshopmongo.service.exception.ObjectNotFoundException;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	public Post findById(String id) {
		Optional<Post> post = postRepository.findById(id);
		if (post.isEmpty()) {
			throw new ObjectNotFoundException("Objeto não encontrado");
		}
		
		return post.get();
	}
	
	public List<Post> findByTitle(String title) {
		return postRepository.searchTitle(title);
	}
	
	public List<Post> fullSearch(String text, LocalDate minDate, LocalDate maxDate) {
		maxDate = maxDate.plusDays(1l);
		return postRepository.fullSearch(text, minDate, maxDate);
	}

}
