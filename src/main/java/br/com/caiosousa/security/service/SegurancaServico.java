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

	public List<Sessao> login(final String email, final String senha)
			throws AcessoNegadoExeption {

		if (email == null || senha == null) {
			throw AcessoNegadoExeption.INSTANCE;
		}

		try {
			return criaSessoes(email, senha);
		} catch (RegistroNaoEncontradoException e) {
			throw AcessoNegadoExeption.INSTANCE;
		}

	}

	public Sessao ativaSessaoPendente(final String token) throws AcessoNegadoExeption {

		final Sessao sessao = recuperaSessaoRedis(token);
		sessao.setPendenteEscolhaTenant(Boolean.FALSE);
		
		redis.opsForValue().set(token, sessao);
		redis.expire(token, Sessao.TEMPO_EXPIRACAO_SESSAO, TimeUnit.MINUTES);

		return sessao;

	}
	
	public Sessao validaSessao(final String token) throws AcessoNegadoExeption {

		final Sessao sessao = recuperaSessaoRedis(token);
		redis.expire(token, Sessao.TEMPO_EXPIRACAO_SESSAO, TimeUnit.MINUTES);
		
		return sessao;

	}

	public void destroiSessao(final String token) {
		redis.delete(token);
	}

	private List<Sessao> criaSessoes(final String email, final String senha)
			throws RegistroNaoEncontradoException {

		final List<Pessoa> pessoas = buscaPessoaAtivaEmTenantsAtivos(email, senha);
		
		if (pessoas.size() == 1) {
			return criaSessaoDefinitiva(pessoas.get(0));
		} else {
			return criaSessoesPendentesEscolhaTenant(pessoas);
		}

	}
	
	private List<Pessoa> buscaPessoaAtivaEmTenantsAtivos(final String email,
			final String senha) throws RegistroNaoEncontradoException {

		final Query pessoaQuery = new Query(Criteria.where("email").is(email)
				.and("senha").is(senha).and("status").is(Status.ATIVO));

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

				final Tenant tenant = buscaTenant(pessoa.getTenant());
				if (tenant.getStatus() == Status.ATIVO) {
					pessoasEmTenantsAtivos.add(pessoa);
				}

			}
		}

		return pessoasEmTenantsAtivos;

	}

	private Tenant buscaTenant(final Long codigoTenant) {
		final Query tenantQuery = new Query(Criteria.where("codigoTenant").is(codigoTenant));
		final Tenant tenant = mongo.findOne(tenantQuery, Tenant.class);
		return tenant;
	}
	
	private List<Sessao> criaSessaoDefinitiva(final Pessoa pessoa) {

		final Sessao sessao = criaSessaoRedis(pessoa, Boolean.FALSE, Sessao.TEMPO_EXPIRACAO_SESSAO, TimeUnit.MINUTES);
		final List<Sessao> sessaoCriada = new ArrayList<>();
		sessaoCriada.add(sessao);
		
		return sessaoCriada;

	}
	
	private List<Sessao> criaSessoesPendentesEscolhaTenant(final List<Pessoa> pessoas) {

		final List<Sessao> sessoesPendentesCriadas = new ArrayList<>();
		
		for (Pessoa pessoa : pessoas) { 
		
			final Sessao sessaoPendente =
					criaSessaoRedis(pessoa, Boolean.TRUE, Sessao.TEMPO_EXPIRACAO_SESSAO_PENDENTE, TimeUnit.MINUTES);
			sessoesPendentesCriadas.add(sessaoPendente);
			
		}
			
		return sessoesPendentesCriadas;

	}
	
	private Sessao criaSessaoRedis(final Pessoa pessoa, final Boolean pendenteEscolhaTenant,
			final Long tempoExpiracao, final TimeUnit unidadeTempoExpiracao) {
		
		final String token = MD5.digest(pessoa.getEmail() + pessoa.getTenant() + System.currentTimeMillis());
		final Tenant tenant = buscaTenant(pessoa.getTenant());
		
		final Sessao sessao = new Sessao(pessoa.getTenant(), tenant.getDescricao(), pessoa.getEmail(),
				pessoa.getGrupos(), pendenteEscolhaTenant, token);

		redis.opsForValue().set(token, sessao);
		redis.expire(token, tempoExpiracao, unidadeTempoExpiracao);
		
		return sessao;
		
	}
	
	private Sessao recuperaSessaoRedis(final String token) throws AcessoNegadoExeption {

		final Sessao sessao = redis.opsForValue().get(token);

		if (sessao == null) {
			throw AcessoNegadoExeption.INSTANCE;
		}
		
		return sessao;
	}

}