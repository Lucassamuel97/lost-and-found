package br.edu.utfpr.alunos.lostandfound.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.utfpr.alunos.lostandfound.model.entity.Item;
import br.edu.utfpr.alunos.lostandfound.model.repository.ItemRepository;

@Service
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public Optional<Item> findById(Long id) {
		return itemRepository.findById(id);
	}

	public Item save(Item item) {
		return itemRepository.save(item);
	} 
		
	public Page<Item> findAll(long id,Pageable pageable) {
		return itemRepository.findItemNotMyUser(id, pageable);
	}
	
	public Page<Item> findAllMyItem(long id,Pageable pageable) {
		return itemRepository.findMyUserItem(id, pageable);
	}

	public void deleteById(Long id) {
		itemRepository.deleteById(id);
	}
}
