package br.com.caiosousa.security.service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import br.com.caiosousa.digest.MD5;
import br.com.caiosousa.enumeration.Status;
import br.com.caiosousa.exception.AcessoNegadoExeption;
import br.com.caiosousa.exception.RegistroNaoEncontradoException;
import br.com.caiosousa.pessoa.model.Pessoa;
import br.com.caiosousa.security.vo.Sessao;
import br.com.caiosousa.tenant.model.Tenant;

@Component
public class SegurancaServico {

	@Autowired
	MongoOperations mongo;
	@Autowired
	RedisTemplate<String, Sessao> redis;
	
	public List<Pessoa> login(final String email, final String senha) throws AcessoNegadoExeption {
		
		if (email == null || senha == null) {
			throw AcessoNegadoExeption.INSTANCE;
		}
		
		try {
			return buscaPessoaTenantAtivo(email, senha);
		} catch (RegistroNaoEncontradoException e) {
			throw AcessoNegadoExeption.INSTANCE;
		}
		
	}
	
	public String criaSessao(final Pessoa pessoa) {
		
		final String token = MD5.digest(pessoa.getEmail() + pessoa.getTenant() + System.currentTimeMillis());
		final Sessao sessao = new Sessao(pessoa.getTenant(), pessoa.getEmail(), pessoa.getGrupos());
		
		redis.opsForValue().set(token, sessao);
		redis.expire(token, 30, TimeUnit.MINUTES);
		
		return token;
		
	}
	
	public Sessao validaSessao(final String token) throws AcessoNegadoExeption {
		
		final Sessao sessao = redis.opsForValue().get(token);
		
		if (sessao == null) {
			throw AcessoNegadoExeption.INSTANCE;
		}
		
		redis.expire(token, 30, TimeUnit.MINUTES);
		
		return sessao;
		
	}
	
	public void destroiSessao(final String token) {
		
		redis.delete(token);
		
	}
	
	private List<Pessoa> buscaPessoaTenantAtivo(final String email, final String senha) throws RegistroNaoEncontradoException {
		
		final Query pessoaQuery = new Query(
					Criteria.where("email").is(email).and("senha").is(senha).and("status").is(Status.ATIVO));
		
		List<Pessoa> pessoas = mongo.find(pessoaQuery, Pessoa.class);
		pessoas = filtraTenantsAtivos(pessoas);
		
		if (pessoas == null || pessoas.isEmpty()) {
			throw RegistroNaoEncontradoException.DEFAULT;
		}
		
		return pessoas;
		
	}

	private List<Pessoa> filtraTenantsAtivos(List<Pessoa> pessoas) {
		
		final List<Pessoa> pessoasEmTenantsAtivos = new ArrayList<>();
		
		if (pessoas != null) {
			for (Pessoa pessoa : pessoas) {
				
				final Query tenantQuery = new Query(Criteria.where("codigoTenant").is(pessoa.getTenant()));
				final Tenant tenant = mongo.findOne(tenantQuery, Tenant.class);
				
				if (tenant.getStatus() == Status.ATIVO) {
					pessoasEmTenantsAtivos.add(pessoa);
				}
				
			}
		}
		
		return pessoasEmTenantsAtivos;
		
	}
	
}
