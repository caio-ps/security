package br.com.caiosousa.security.vo;

import java.io.Serializable;
import java.util.Set;

import br.com.caiosousa.pessoa.enumeration.Grupo;

public class Sessao implements Serializable {

	private static final long serialVersionUID = -8429320250864083850L;
	
	public static final long TEMPO_EXPIRACAO_SESSAO = 30L;
	public static final long TEMPO_EXPIRACAO_SESSAO_PENDENTE = 1L;

	private String token;
	private Long tenant;
	private String descricaoTenant;
	private String email;
	private Set<Grupo> grupos;
	private Boolean pendenteEscolhaTenant;

	public Sessao() {
	}

	public Sessao(Long tenant, String descricaoTenant, String email, Set<Grupo> grupos, Boolean pendenteEscolhaTenant,
			String token) {
		this.tenant = tenant;
		this.descricaoTenant = descricaoTenant;
		this.email = email;
		this.grupos = grupos;
		this.pendenteEscolhaTenant = pendenteEscolhaTenant;
		this.token = token;
	}

	public Long getTenant() {
		return tenant;
	}

	public void setTenant(Long tenant) {
		this.tenant = tenant;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Boolean getPendenteEscolhaTenant() {
		return pendenteEscolhaTenant;
	}

	public void setPendenteEscolhaTenant(Boolean pendenteEscolhaTenant) {
		this.pendenteEscolhaTenant = pendenteEscolhaTenant;
	}

	public String getDescricaoTenant() {
		return descricaoTenant;
	}

	public void setDescricaoTenant(String descricaoTenant) {
		this.descricaoTenant = descricaoTenant;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
