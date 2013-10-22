package br.com.caiosousa.security.vo;

import java.util.Set;

import br.com.caiosousa.pessoa.enumeration.Grupo;

public class Sessao {

	private Long tenant;
	private String email;
	private Set<Grupo> grupos;

	public Sessao(Long tenant, String email, Set<Grupo> grupos) {
		this.tenant = tenant;
		this.email = email;
		this.grupos = grupos;
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

}
