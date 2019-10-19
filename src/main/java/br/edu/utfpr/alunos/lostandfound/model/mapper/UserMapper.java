package br.edu.utfpr.alunos.lostandfound.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import br.edu.utfpr.alunos.lostandfound.model.dto.UserDTO;
import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import org.modelmapper.ModelMapper;

@Component
public class UserMapper {

	@Autowired
	private ModelMapper mapper;
	
	public UserDTO toDto(User entity) {
        UserDTO dto = mapper.map(entity, UserDTO.class);
        return dto;
    }

	public User toEntity(UserDTO dto) {
        User entity = mapper.map(dto, User.class);
        return entity;
    }
}
