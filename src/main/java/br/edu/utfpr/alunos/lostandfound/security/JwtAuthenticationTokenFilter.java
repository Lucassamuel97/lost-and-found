package br.edu.utfpr.alunos.lostandfound.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.utfpr.alunos.lostandfound.util.JwtTokenUtil;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
	
	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		CustomHttpServletRequest req = new CustomHttpServletRequest(request);
		
		req.putHeader("Access-Control-Allow-Origin", "*");
		req.putHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		req.putHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Token");
		req.putHeader("Access-Control-Max-Age", "1728000");
		
		String token = req.getHeader(AUTH_HEADER);
		if(token != null && token.startsWith(BEARER_PREFIX)) {
			token = token.substring(7);
		}
		
		String username = jwtTokenUtil.getUsernameFromToken(token);
		
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if(jwtTokenUtil.isValid(token)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, 
						null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication); 
			}
		}
		
		filterChain.doFilter(req, response);

	}

}
