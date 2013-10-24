package br.com.caiosousa.security.vo;

import java.util.List;


public class LoginsDisponiveisJSON {

	private List<Sessao> loginsDisponiveis;
	
	public LoginsDisponiveisJSON(List<Sessao> loginsDisponiveis) {
		this.loginsDisponiveis = loginsDisponiveis;
	}

	public List<Sessao> getLoginsDisponiveis() {
		return loginsDisponiveis;
	}

	public void setLoginsDisponiveis(List<Sessao> loginsDisponiveis) {
		this.loginsDisponiveis = loginsDisponiveis;
	}
	
	
}
