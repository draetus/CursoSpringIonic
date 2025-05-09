package com.mauricio.workshopmongo.resource;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mauricio.workshopmongo.domain.Post;
import com.mauricio.workshopmongo.resource.util.URL;
import com.mauricio.workshopmongo.service.PostService;


@RestController
@RequestMapping(value = "/posts")
public class PostResource {
	
	@Autowired
	private PostService postService;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Post> findById(@PathVariable String id) {
		Post post = postService.findById(id);
		return ResponseEntity.ok(post);
	}
	
	@GetMapping(value = "/titlesearch")
	public ResponseEntity<List<Post>> findByTitle(@RequestParam(value = "text", defaultValue = "") String title) {
		title = URL.decodeParam(title);
		List<Post> posts = postService.findByTitle(title);
		return ResponseEntity.ok(posts);
	}
	
	@GetMapping(value = "/fullsearch")
	public ResponseEntity<List<Post>> fullSearch(
			@RequestParam(value = "text", defaultValue = "") String title,
			@RequestParam(defaultValue = "") String minDate,
			@RequestParam(defaultValue = "") String maxDate) {
		title = URL.decodeParam(title);
		LocalDate dateMin = URL.convertDate(minDate, LocalDate.now().minusYears(100));
		LocalDate dateMax = URL.convertDate(maxDate, LocalDate.now());
		List<Post> posts = postService.fullSearch(title, dateMin, dateMax);
		return ResponseEntity.ok(posts);
	}
	

}
