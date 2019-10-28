package br.edu.utfpr.alunos.lostandfound.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtTokenUtil {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_CREATED = "created";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	/**
	 * 
	 * Retorna o username contido no token JWT.
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		String username;
		try {
			Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}
	
	/**
	 * 
	 * Retorna a data de expiração de um token JWT
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}
	
	/**
	 * 
	 * Cria um token(refresh)
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try { 
			Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}
	
	/**
	 * 
	 * Retorna um novo token baseado nos dados do usuário
	 * 
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}
	
	/**
	 * 
	 * Verifica se um token é válido
	 * @param token
	 * @return
	 */
	public boolean isValid(String token) {
		return !isExpired(token);
	}
	
	/**
	 * 
	 * Verifica se o token está expirado.
	 * @param token
	 * @return
	 */
	public boolean isExpired(String token) {
		Date date = this.getExpirationDateFromToken(token);
		if(date == null) {
			return false;
		}
		return date.before(new Date());
	}
	
	/**
	 * 
	 * Extrair as informações contidas no token.
	 * 
	 * @param token
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	/**
	 * 
	 * Gera a data de expiração a partir da atual
	 * @return
	 */
	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + expiration + 1000);
	}
	
	/**
	 * 
	 * Gera um token com base dos dados do Claim fornecido.
	 * @param claims
	 * @return
	 */
	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	
	
	
}
