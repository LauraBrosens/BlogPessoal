package com.blogpessoal.blogPessoal.repository;

import com.blogpessoal.blogPessoal.model.*;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostagemRepository extends JpaRepository <Postagem, Long> {
	public List<Postagem> findAllByTituloContainingIgnoreCase (String Titulo);
	
}
