package br.edu.utfpr.alunos.lostandfound.model.entity;

import br.edu.utfpr.alunos.lostandfound.model.dto.UserDTO;
import br.edu.utfpr.alunos.lostandfound.security.ProfileEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private Date created;
	private Date updated;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Column
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private ProfileEnum profile;
	
	public User() {}

	public User(UserDTO userDTO) {
		this.email = userDTO.getEmail();
		this.password = userDTO.getPassword();
		this.telefone = userDTO.getTelefone();
		this.id = userDTO.getId();
		this.profile = userDTO.getProfile();
	}
	
	public User(String login,String pwd, String telefone) {
		this.email = login;
		this.password = pwd;
		this.telefone = telefone;
	}
	
    public void update(UserDTO dto) {
    	this.email = dto.getEmail();
		this.telefone = dto.getTelefone();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated = new Date();
    }

    @PrePersist
    public void onSave(){
        final Date now = new Date();
        this.created = now;
        this.updated = now;
        this.setProfile(ProfileEnum.ROLE_USER);
    }

	public void updatePwd(@Valid UserDTO dto) {
		this.password = dto.getPassword();
	}
}
