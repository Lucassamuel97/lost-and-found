package br.edu.utfpr.alunos.lostandfound.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.utfpr.alunos.lostandfound.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByLogin(String login);

}
