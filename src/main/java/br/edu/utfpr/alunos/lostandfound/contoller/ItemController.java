package br.edu.utfpr.alunos.lostandfound.contoller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.alunos.lostandfound.model.dto.ItemDTO;
import br.edu.utfpr.alunos.lostandfound.model.entity.Item;
import br.edu.utfpr.alunos.lostandfound.model.mapper.ItemMapper;
import br.edu.utfpr.alunos.lostandfound.model.service.ItemService;
import br.edu.utfpr.alunos.lostandfound.util.Response;

@RestController
@RequestMapping("/items")
@CrossOrigin(origins = "*")
public class ItemController {
	@Autowired
	ItemService itemService;

	@Autowired
	ItemMapper mapper;

	@Value("${pagina.quantidade}")
	private int paginationAmount;

	@GetMapping
	public ResponseEntity<Response<List<ItemDTO>>> findAll(
			@PageableDefault(page=0, size=5, sort = "updated", direction = Sort.Direction.ASC) Pageable pageable) {

		Response<List<ItemDTO>> response = new Response<>();

		Page<Item> itens = this.itemService.findAll(pageable);
		Page<ItemDTO> itemDTOS = itens.map(i -> new ItemDTO(i));
		if(itemDTOS.getContent().isEmpty()) {
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
		
		dto.setStatus('A');
		
		Item item = mapper.toEntity(dto);
		
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
	
    //Atualiza item
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
        
        Item item = mapper.toEntity(dto);
        try {
			item = itemService.save(item);
		}catch(Exception e) {
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
        
        try {
        	 this.itemService.deleteById(id);
        } catch (Exception e) {
            response.addError("Houve um erro ao deletar o usuario.");
            return ResponseEntity.badRequest().body(response);
        }
        
        response.setData("Item cod. "+ o.get().getId()+" deletado com sucesso");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/{id}/devolucao")
    public ResponseEntity<?> devolution(@PathVariable Long id,@RequestParam("id") Long idUser) {

        Response<ItemDTO> response = new Response<>();
        
        //Retorna item
        Optional<Item> o = itemService.findById(id);
        if (!o.isPresent()) {
            response.addError("Item não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        //Retorna usario
        if (!o.isPresent()) {
            response.addError("Item não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        Item itemresult = o.get();
        itemresult.setStatus('D');
        
        if(itemresult.getUserfound() == null){
        	response.addError("Usuario 2 não selecionado");
            return ResponseEntity.badRequest().body(response);
		}
        
        try {
        	itemresult = itemService.save(itemresult);
		}catch(Exception e) {
			response.addError("Houve um erro ao efetuar a devolucao");
			return ResponseEntity.badRequest().body(response);
		}
        
        ItemDTO dto = mapper.toDto(itemresult);
        response.setData(dto);
        return ResponseEntity.ok(response);
    }
}
