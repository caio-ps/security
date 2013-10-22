package br.com.caiosousa.security.service.restinterface;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.caiosousa.exception.CamposInvalidosException;
import br.com.caiosousa.exception.OperacaoNaoPermitidaException;
import br.com.caiosousa.exception.RegistroNaoEncontradoException;
import br.com.caiosousa.pessoa.model.Pessoa;

@Controller
public class SegurancaController {

//	@ResponseBody
//    @ExceptionHandler(value = {RegistroNaoEncontradoException.class})
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public String retornaRegistroNaoEncontrado(Exception ex) {
//        return ex.toString();
//    }
//	
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.OK)
//    public HttpEntity<ListaPessoaJSON> getPessoas() throws OperacaoNaoPermitidaException, RegistroNaoEncontradoException {
//
//    	final ListaPessoaJSON pessoasJSON = converteListaParaJson(pessoaServico.buscaTodos());
//    	return new ResponseEntity<ListaPessoaJSON>(adicionaListaLinksPermitidos(pessoasJSON), HttpStatus.OK);
//        
//    }
//    
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.POST, value="/pessoa")
//    @ResponseStatus(value = HttpStatus.CREATED)
//    public HttpEntity<PessoaJSON> criaPessoa(@RequestBody Pessoa pessoa)
//    		throws OperacaoNaoPermitidaException, CamposInvalidosException {
//
//    	pessoaServico.cria(pessoa);
//		return new ResponseEntity<PessoaJSON>(adicionaLinksPermitidos(new PessoaJSON(pessoa)), HttpStatus.CREATED);
//    	
//    }

}
