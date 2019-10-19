package br.edu.utfpr.alunos.lostandfound.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Parâmetro(s) inválido(s).")
public class InvalidParamsException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private BindingResult result;

	public InvalidParamsException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidParamsException(String message) {
		this.title = message;
	}

	public InvalidParamsException(BindingResult result) {
		this.result = result;
	}

	public InvalidParamsException(String title, BindingResult result) {
		super();
		this.title = title;
		this.result = result;
	}

	public List<String> errors() {
		List<String> e = new ArrayList<>();
		result.getAllErrors().forEach(error -> e.add(error.getDefaultMessage()));
		return e;
	}

	public String getTitle() {
		return this.title;
	}
}
