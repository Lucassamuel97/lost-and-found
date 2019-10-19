package br.edu.utfpr.alunos.lostandfound.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User save(User entity) {
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

	public User findByLogin(String login) {
		return userRepository.findByLogin(login);
	}
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}
}
