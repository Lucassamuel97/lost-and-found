package br.edu.utfpr.alunos.lostandfound.contoller;

import br.edu.utfpr.alunos.lostandfound.model.dto.JwtAuthenticationDTO;
import br.edu.utfpr.alunos.lostandfound.model.dto.TokenDTO;
import br.edu.utfpr.alunos.lostandfound.security.JwtUserDetailService;
import br.edu.utfpr.alunos.lostandfound.util.JwtTokenUtil;
import br.edu.utfpr.alunos.lostandfound.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	private static final String TOKEN_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	
	@Autowired
	private AuthenticationManager authenticationManager;
		
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailService userDetailsService;
	
	@PostMapping
	public ResponseEntity<Response<TokenDTO>> generateToken(@Valid @RequestBody JwtAuthenticationDTO authenticationDTO, BindingResult result)  throws AuthenticationException{
		System.out.println(authenticationDTO.toString());
		System.out.println(result);
		Response<TokenDTO> response = new Response<>();
		
		if(result.hasErrors()) {
			log.error("Erro {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		System.out.println("Parte 1");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
		System.out.println("Parte 2");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		System.out.println("Parte 3");
		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDTO.getEmail());
		System.out.println("Parte 4");
		String token = jwtTokenUtil.generateToken(userDetails);
		System.out.println("Parte 5");
		response.setData(new TokenDTO(token));
		System.out.println("Parte 6");
		return ResponseEntity.ok(response);
	}
	
	
}
