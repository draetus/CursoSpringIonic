package com.mauricio.workshopmongo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mauricio.workshopmongo.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{
	
	//https://www.mongodb.com/pt-br/docs/manual/reference/operator/query/regex/
	@Query("{ 'title': { $regex: ?0, $options: 'i' } } ")
	public List<Post> searchTitle(String text);
	
	public List<Post> findByTitleContainingIgnoreCase(String title);
	
	@Query("{ $and: [ { $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } }, { 'comments.text': { $regex: ?0, $options: 'i' } } ] }, { date: {$gte: ?1} }, { date: {$lte: ?2} } ] }")
	public List<Post> fullSearch(String title, LocalDate minDate, LocalDate maxDate);

}
