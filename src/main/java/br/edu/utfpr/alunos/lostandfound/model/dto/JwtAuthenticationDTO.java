package br.edu.utfpr.alunos.lostandfound.model.dto;



import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class JwtAuthenticationDTO {
	
	@NotEmpty(message = "O email não pode ser vazio.")
	@Email(message = "O email é inválido.")
	private String email;
	
	@NotEmpty(message = "A senha não pode ser vazia.")
	private String password;

}
