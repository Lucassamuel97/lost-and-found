package br.edu.utfpr.alunos.lostandfound;

import br.edu.utfpr.alunos.lostandfound.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LostAndFoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(LostAndFoundApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
