package br.edu.utfpr.alunos.lostandfound.model.dto;

import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.security.ProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDTO {
	private Long id;
	
    @NotEmpty(message = "O nome não pode ser vazio")
    @Length(min = 2, max = 100, message = "O Login deve conter no mínimo 2 e máximo 40 caracteres.")
	private String email;
    
    @NotEmpty(message = "A senha não pode ser nula")
    @Length(min = 2, max = 100, message = "A senha deve conter no mínimo 2 e máximo 40 caracteres.")
	private String password;
    
	private String telefone;

	private String created;
	private String updated;
	private String profile;

	public UserDTO(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.telefone = user.getTelefone();
		this.created = user.getCreated().toString();
		this.updated = user.getUpdated().toString();
		this.profile = user.getProfile().toString();
	}
}
