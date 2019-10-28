package br.edu.utfpr.alunos.lostandfound.model.service;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.model.repository.UserRepository;
import br.edu.utfpr.alunos.lostandfound.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User entity) {
		entity.setPassword(PasswordUtil.generateBCrypt(entity.getPassword()));
		return userRepository.save(entity);
	}
	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
	
	public Optional<User> findUser(Long id){
		return userRepository.findById(id);
	}

	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	public Optional<User> findByEmail(String email){
		return Optional.ofNullable(this.userRepository.findByEmail(email));
	}
}
