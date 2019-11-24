package br.edu.utfpr.alunos.lostandfound.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.utfpr.alunos.lostandfound.model.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
	@Query("select i from Item i where i.usersrecord.id = :id")
	Page<Item> findMyUserItem(@Param("id") Long id, Pageable pageable);
	
	@Query("select i from Item i where i.usersrecord.id != :id")
	Page<Item> findItemNotMyUser(@Param("id") Long id, Pageable pageable);
}
