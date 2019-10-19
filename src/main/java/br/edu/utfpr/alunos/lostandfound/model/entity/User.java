package br.edu.utfpr.alunos.lostandfound.model.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;

import java.util.stream.Collectors;

import br.edu.utfpr.alunos.lostandfound.model.dto.UserDTO;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private Date created;
	private Date updated;
	
	@Column(unique = true)
	private String login;
	
	@Column
	private String pwd;
	
	@Column
	private String telefone;
	
	@Column
	private String email;
	
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "profiles_users_tb")
    private Set<Integer> roles = new HashSet<>();
	
	public User() {}
	
	public User(String login,String pwd, String telefone, String email) {
		this.login = login;
		this.pwd = pwd;
		this.telefone = telefone;
		this.email = email;
	}
	
    public void update(UserDTO dto) {
    	this.login = dto.getLogin();
		this.telefone = dto.getTelefone();
		this.email = dto.getEmail();
    }
    
    public Set<Role> getProfiles() {
        return roles.stream().map(Role::toEnum).collect(Collectors.toSet());
    }
    
    public void addProfile(Role role) {
        this.roles.add(role.getCode());
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
        this.addProfile(Role.USER);
    }

	public void updatePwd(@Valid UserDTO dto) {
		this.pwd = dto.getPwd();
	}
}
