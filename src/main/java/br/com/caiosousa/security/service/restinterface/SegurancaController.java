package br.com.caiosousa.security.service.restinterface;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.caiosousa.exception.AcessoNegadoExeption;
import br.com.caiosousa.security.service.SegurancaServico;
import br.com.caiosousa.security.vo.Login;
import br.com.caiosousa.security.vo.LoginsDisponiveisJSON;
import br.com.caiosousa.security.vo.Sessao;

@Controller
public class SegurancaController {

	@Autowired
	SegurancaServico segurancaServico;
	
	@ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/login")
    @ResponseStatus(value = HttpStatus.CREATED)
    public HttpEntity<LoginsDisponiveisJSON> login(@RequestBody Login login)
    		throws AcessoNegadoExeption {

    	final List<Sessao> sessoesCriadas = segurancaServico.login(login.getEmail(), login.digestedSenha());
    	final LoginsDisponiveisJSON loginsDisponiveis = new LoginsDisponiveisJSON(sessoesCriadas);
    	
    	return new ResponseEntity<LoginsDisponiveisJSON>(loginsDisponiveis, HttpStatus.CREATED);
    	
    }

	@ResponseBody
    @RequestMapping(method = RequestMethod.POST, value="/ativaSessao")
    @ResponseStatus(value = HttpStatus.OK)
    public HttpEntity<Sessao> ativaSessaoPendente(@RequestBody String token)
    		throws AcessoNegadoExeption {

    	final Sessao sessaoAtivada = segurancaServico.ativaSessaoPendente(token);
    	return new ResponseEntity<Sessao>(sessaoAtivada, HttpStatus.OK);
    	
    }
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/validaSessao")
	@ResponseStatus(value = HttpStatus.OK)
	public HttpEntity<Sessao> validaSessao(@RequestParam(value = "token", required = true) String token) 
			throws AcessoNegadoExeption  {

		final Sessao sessao = segurancaServico.validaSessao(token);
		return new ResponseEntity<Sessao>(sessao, HttpStatus.OK);

	}
	
	@ResponseBody
	@ExceptionHandler(value = { AcessoNegadoExeption.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public String acessoNegadoPrevisto(Exception ex) {
		return "ACESSO NEGADO";
	}
	
	@ResponseBody
	@ExceptionHandler(value = { Throwable.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public String acessoNegadoContingencia(Exception ex) {
		return "ACESSO NEGADO";
	}

}
