package br.edu.utfpr.alunos.lostandfound.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.edu.utfpr.alunos.lostandfound.model.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
	@Transactional(readOnly = true)
	
	// ITEMS 
	@Query("select i from Item i where i.usersrecord.id != :id and i.status like 'A' order by i.id desc")
	Page<Item> findItemNotMyUser(@Param("id") Long id, Pageable pageable);

	// ATIVOS
	@Query("select i from Item i where i.usersrecord.id = :id and i.status like 'A' order by i.id desc")
	Page<Item> findItemAtivos(@Param("id") Long id, Pageable pageable);

	// PENDENTE
	@Query("select i from Item i where i.usersrecord.id = :id and i.status like 'P' order by i.id desc")
	Page<Item> findItemPendentes(@Param("id") Long id, Pageable pageable);

	// FINALIZADO / EXPIRADO
	@Query("select i from Item i where i.usersrecord.id = :id and i.status in ('F','E') order by i.id desc")
	Page<Item> findItemFinalizados(@Param("id") Long id, Pageable pageable);
	
}
