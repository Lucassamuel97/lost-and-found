package br.edu.utfpr.alunos.lostandfound.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.utfpr.alunos.lostandfound.model.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
