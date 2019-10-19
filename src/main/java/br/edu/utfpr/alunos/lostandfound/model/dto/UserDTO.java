package br.edu.utfpr.alunos.lostandfound.model.dto;

import javax.validation.constraints.NotEmpty;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class UserDTO {
	private Long id;
	
    @NotEmpty(message = "O nome não pode ser vazio")
    @Length(min = 2, max = 100, message = "O Login deve conter no mínimo 2 e máximo 40 caracteres.")
	private String login;
    
    @NotEmpty(message = "A senha não pode ser nula")
    @Length(min = 2, max = 100, message = "A senha deve conter no mínimo 2 e máximo 40 caracteres.")
	private String pwd;
    
	private String telefone;
	private String email;

	private String created;
	private String updated;

	public UserDTO(User user) {
		this.id = user.getId();
		this.login = user.getLogin();
		this.pwd = user.getPwd();
		this.email = user.getEmail();
		this.telefone = user.getTelefone();
		this.created = user.getCreated().toString();
		this.updated = user.getUpdated().toString();
	}
}
