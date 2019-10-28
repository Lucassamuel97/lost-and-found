package br.edu.utfpr.alunos.lostandfound.contoller;

import br.edu.utfpr.alunos.lostandfound.model.dto.UserDTO;
import br.edu.utfpr.alunos.lostandfound.model.entity.User;
import br.edu.utfpr.alunos.lostandfound.model.mapper.UserMapper;
import br.edu.utfpr.alunos.lostandfound.model.service.UserService;
import br.edu.utfpr.alunos.lostandfound.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
//@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserMapper mapper;
	
    @Value("${pagina.quantidade}")
    private int paginationAmount;
	
    @GetMapping
    public ResponseEntity<Response<List<UserDTO>>> findAll(
            @PageableDefault(page=0, size=5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Response<List<UserDTO>> response = new Response<>();

        Page<User> users = this.userService.findAll(pageable);
        Page<UserDTO> userDTOS = users.map(u -> new UserDTO(u));
        if(userDTOS.getContent().isEmpty()) {
            response.addError("Nenhum item nesta página.");
            return ResponseEntity.badRequest().body(response);
        }
        response.setData(userDTOS.getContent());
        return ResponseEntity.ok(response);
    }
	
	//retorna usuario
	@GetMapping(value = "/{id}")
    public ResponseEntity<Response<UserDTO>> findById(@PathVariable Long id) {
		Response<UserDTO> response = new Response<>();
		
		Optional<User> s = userService.findUser(id);
        
		if (!s.isPresent()) {
            response.addError("Usuario não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        User user = s.get();
        UserDTO dto = mapper.toDto(user);
        response.setData(dto);
        
        return ResponseEntity.ok(response);
    }
	
	//Persiste Usuario
    @PostMapping
    public ResponseEntity<Response<UserDTO>> post(@Valid @RequestBody UserDTO dto, BindingResult result) {

        Response<UserDTO> response = new Response<>();
        if (result.hasErrors()) {
            response.setErrors(result);
            return ResponseEntity.badRequest().body(response);
        }

        // impede usar post para usuario ja cadastrado
        if (dto.getId() != null) {
            Optional<User> o = userService.findById(dto.getId());
            if (o.isPresent()) {
                response.addError("Usuario já cadastrado.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        // tava dando erro
        //User user = mapper.toEntity(dto);

        User user = new User(dto);
        try {
            user = userService.save(user);
        } catch (Exception e) {
            // Login Unico
            response.addError("Houve um erro ao persistir os seus dados.");
            return ResponseEntity.badRequest().body(response);
        }
        dto = new UserDTO(user);
        response.setData(dto);

        return ResponseEntity.ok(response);
    }
    
    //Atualiza Usuario
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @Valid @RequestBody UserDTO dto, BindingResult result) {

        Response<UserDTO> response = new Response<>();
        if (result.hasErrors()) {
            response.setErrors(result);
            return ResponseEntity.badRequest().body(response);
        }

        // busca pelo Usuario
        Optional<User> o = userService.findById(id);
        if (!o.isPresent()) {
            response.addError("Usuario não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        User user = o.get();

        // impede atualizar para login ja cadastrado
        if (!user.getEmail().equals(dto.getEmail())) {
            if (userService.findByEmail(dto.getEmail()) != null) {
                response.addError("Não é possível atualizar para esté Login, ele já está sendo usado.");
                return ResponseEntity.badRequest().body(response);
            }
        }
        
        // Impede atualizar a senha para nulo
        user.update(dto);
        
        user = userService.save(user);

        // prepara a resposta
        dto = mapper.toDto(user);
        response.setData(dto);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping(value = "/pwd/{id}")
    public ResponseEntity<?> putPwp(@PathVariable Long id, @Valid @RequestBody UserDTO dto, BindingResult result) {
    	Response<UserDTO> response = new Response<>();
    	
    	// busca pelo Usuario
        Optional<User> o = userService.findById(id);
        if (!o.isPresent()) {
            response.addError("Usuario não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
        
        User user = o.get();
        //Permite atualizar somente a senha
        user.updatePwd(dto);
        user = userService.save(user);
        
        // prepara a resposta
        dto = mapper.toDto(user);
        response.setData(dto);

        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable Long id) {
    	
        Response<String> response = new Response<>();
        Optional<User> o = userService.findById(id);

        if (!o.isPresent()) {
            response.addError("Erro ao remover, registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
        	 this.userService.deleteById(id);
        } catch (Exception e) {
            // Trata se houver erro ao deletar
            response.addError("Houve um erro ao deletar o usuario.");
            return ResponseEntity.badRequest().body(response);
        }
        
        response.setData("Usuario "+o.get().getEmail()+" deletado com sucesso");
        return ResponseEntity.ok(response);
    }
}
