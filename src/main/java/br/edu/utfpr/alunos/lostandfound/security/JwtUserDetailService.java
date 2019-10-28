package br.edu.utfpr.alunos.lostandfound.security;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class JwtUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOptional = userService.findByEmail(username);
		
		if(userOptional.isPresent()) {
			return JwtUserFactory.create(userOptional.get());
		}
		
		throw new UsernameNotFoundException("Email não encontrado!");
	}

}
