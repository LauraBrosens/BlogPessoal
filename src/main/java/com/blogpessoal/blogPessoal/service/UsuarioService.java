package com.blogpessoal.blogPessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blogpessoal.blogPessoal.model.UserLogin;
import com.blogpessoal.blogPessoal.model.Usuario;
import com.blogpessoal.blogPessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	public Usuario CadastrarUsuario(Usuario usuario) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String senhaEncoder = encoder.encode(usuario.getSenha());
		usuario.setSenha(senhaEncoder);
		return repository.save(usuario);
	}
	
	public Optional<UserLogin> Logar(Optional<UserLogin> user){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());
		//caso o usuário exista
		if (usuario.isPresent()) {
			//caso a senha fornecida coincida com o usuário encontrado:
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
				//gera autenticação
				String auth = user.get().getUsuario() + ":" + user.get().getSenha();
				//gera token autenticador
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String (encodedAuth);
				//coloca o token no usuário temporário da sessão
				user.get().setToken(authHeader);
				user.get().setNome(usuario.get().getNome());
				
				return user;
			}
		}
		//caso o usuário não exista: 
		return null;
	}
}
