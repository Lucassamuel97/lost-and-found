package br.edu.utfpr.alunos.lostandfound.contoller;

import br.edu.utfpr.alunos.lostandfound.model.dto.ItemDTO;
import br.edu.utfpr.alunos.lostandfound.model.entity.Item;
import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.model.mapper.ItemMapper;
import br.edu.utfpr.alunos.lostandfound.model.service.ItemService;
import br.edu.utfpr.alunos.lostandfound.model.service.UserService;
import br.edu.utfpr.alunos.lostandfound.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ItemController {

	@Autowired
	ItemService itemService;

	@Autowired
	UserService userService;

	@Autowired
	ItemMapper mapper;

	@Value("${pagina.quantidade}")
	private int paginationAmount;

	@GetMapping
	public ResponseEntity<Response<List<ItemDTO>>> findAll(
			@PageableDefault(page = 0, size = 5, sort = "updated", direction = Sort.Direction.ASC) Pageable pageable) {

		Response<List<ItemDTO>> response = new Response<>();
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> user = userService.findByEmail(securityContext.getAuthentication().getName());
		
		Page<Item> itens = this.itemService.findAll( user.get().getId() , pageable);
		Page<ItemDTO> itemDTOS = itens.map(i -> new ItemDTO(i));
		if (itemDTOS.getContent().isEmpty()) {
			response.addError("Nenhum item nesta página.");
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(itemDTOS.getContent());
		return ResponseEntity.ok(response);
	}
	
	// retorna os items do usuario
    @GetMapping(value = "/myitems")
	public ResponseEntity<Response<List<ItemDTO>>> findAllMyItem(
			@PageableDefault(page = 0, size = 5, sort = "updated", direction = Sort.Direction.ASC) Pageable pageable) {

		Response<List<ItemDTO>> response = new Response<>();
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> user = userService.findByEmail(securityContext.getAuthentication().getName());
		
		Page<Item> itens = this.itemService.findAllMyItem( user.get().getId() , pageable);
		Page<ItemDTO> itemDTOS = itens.map(i -> new ItemDTO(i));
		if (itemDTOS.getContent().isEmpty()) {
			response.addError("Nenhum item nesta página.");
			return ResponseEntity.badRequest().body(response);
		}
		response.setData(itemDTOS.getContent());
		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Response<ItemDTO>> post(@Valid @RequestBody ItemDTO dto, BindingResult result) {

		Response<ItemDTO> response = new Response<>();

		if (result.hasErrors()) {
			response.setErrors(result);
			return ResponseEntity.badRequest().body(response);
		}

		// impede usar post para item ja cadastrado
		if (dto.getId() != null) {
			Optional<Item> o = itemService.findById(dto.getId());
			if (o.isPresent()) {
				response.addError("Item já cadastrado.");
				return ResponseEntity.badRequest().body(response);
			}
		}

		// Pegar o usuario logado
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> o = userService.findByEmail(securityContext.getAuthentication().getName());

		Item item = mapper.toEntity(dto);

		item.setStatus('A');
		item.setUsersrecord(o.get());

		try {
			item = itemService.save(item);
		} catch (Exception e) {
			response.addError("Houve um erro ao persistir o item");
			return ResponseEntity.badRequest().body(response);
		}

		dto = mapper.toDto(item);
		response.setData(dto);
		return ResponseEntity.ok(response);
	}

	// retorna Item
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<ItemDTO>> findByItem(@PathVariable Long id) {

		Response<ItemDTO> response = new Response<>();

		Optional<Item> opItem = itemService.findById(id);

		if (!opItem.isPresent()) {
			response.addError("Item não encontrado.");
			return ResponseEntity.badRequest().body(response);
		}

		Item item = opItem.get();
		ItemDTO dto = mapper.toDto(item);
		response.setData(dto);

		return ResponseEntity.ok(response);
	}

	// Atualiza item
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody ItemDTO dto, BindingResult result) {

		Response<ItemDTO> response = new Response<>();
		if (result.hasErrors()) {
			response.setErrors(result);
			return ResponseEntity.badRequest().body(response);
		}

		Optional<Item> o = itemService.findById(id);
		if (!o.isPresent()) {
			response.addError("Item não encontrado");
			return ResponseEntity.badRequest().body(response);
		}
		Item item = o.get();
		item.update(dto);

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> user = userService.findByEmail(securityContext.getAuthentication().getName());

		// Verifica se é o usuario que cadastrou que está atualizando o item
		if (item.getUsersrecord().getId() != user.get().getId()) {
			response.addError("O usuario não tem permissão para atualizar o item");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			item = itemService.save(item);
		} catch (Exception e) {
			response.addError("Houve um erro ao persistir o item");
			return ResponseEntity.badRequest().body(response);
		}

		dto = mapper.toDto(item);
		response.setData(dto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> delete(@PathVariable Long id) {

		Response<String> response = new Response<>();
		Optional<Item> o = itemService.findById(id);

		if (!o.isPresent()) {
			response.addError("Erro ao remover, registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> user = userService.findByEmail(securityContext.getAuthentication().getName());

		// Verifica se é o usuario que cadastrou que está deletando o item
		if (o.get().getUsersrecord().getId() != user.get().getId()) {
			response.addError("O usuario não tem permissão para deletar o item");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			this.itemService.deleteById(id);
		} catch (Exception e) {
			response.addError("Houve um erro ao deletar o usuario.");
			return ResponseEntity.badRequest().body(response);
		}

		response.setData("Item cod. " + o.get().getId() + " deletado com sucesso");
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/{id}/devolucao")
	public ResponseEntity<?> devolution(@PathVariable Long id) {

		Response<ItemDTO> response = new Response<>();

		// Retorna item
		Optional<Item> o = itemService.findById(id);
		if (!o.isPresent()) {
			response.addError("Item não encontrado");
			return ResponseEntity.badRequest().body(response);
		}

		SecurityContext securityContext = SecurityContextHolder.getContext();
		Optional<User> user = userService.findByEmail(securityContext.getAuthentication().getName());

		Item itemresult = o.get();
		itemresult.setStatus('D');
		itemresult.setUserfound(user.get());

		if (itemresult.getUserfound() == null) {
			response.addError("Usuario 2 não selecionado");
			return ResponseEntity.badRequest().body(response);
		}

		try {
			itemresult = itemService.save(itemresult);
		} catch (Exception e) {
			response.addError("Houve um erro ao efetuar a devolucao");
			return ResponseEntity.badRequest().body(response);
		}

		ItemDTO dto = mapper.toDto(itemresult);
		response.setData(dto);
		return ResponseEntity.ok(response);
	}
}
