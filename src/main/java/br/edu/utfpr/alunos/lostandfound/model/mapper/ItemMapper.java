package br.edu.utfpr.alunos.lostandfound.model.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.utfpr.alunos.lostandfound.model.dto.ItemDTO;
import br.edu.utfpr.alunos.lostandfound.model.entity.Item;

@Component
public class ItemMapper {
	@Autowired
	private ModelMapper mapper;
	
	public ItemDTO toDto(Item entity) {
		ItemDTO dto = mapper.map(entity, ItemDTO.class);
        return dto;
    }

	public Item toEntity(ItemDTO dto) {
		Item entity = mapper.map(dto, Item.class);
        return entity;
    }
}
