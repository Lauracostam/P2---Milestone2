package br.ufal.ic.p2.wepayu.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Horista extends Empregado{
	private Map<String, String> cartao = new HashMap<String, String>();
	
	public Horista(String id, String nome, String endereco, String tipo, String salario) {
		super(id, nome, endereco, tipo, salario);
		this.cartao = new HashMap<String, String>();
	}
	
	public double getHorasExtras(String dataInicial, String dataFinal) throws ParseException {
		double horas = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    Date dataAtual = null;
		for (Map.Entry<String,String> itr : cartao.entrySet()) {
			dataAtual = sdformat.parse(itr.getKey());
			if(dataAtual.compareTo(inicio) >= 0 && dataAtual.compareTo(fim) < 0) {
				horas += Double.valueOf(itr.getValue().replace(",", "."))-8;
			}
		}
		return horas;
	}
	public double getHorasNormais(String dataInicial, String dataFinal) throws ParseException{
		double horas = 0;
		double hora = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    Date dataAtual = null;
		for (Map.Entry<String,String> itr : cartao.entrySet()) {
			dataAtual = sdformat.parse(itr.getKey());
			if(dataAtual.compareTo(inicio) >= 0 && dataAtual.compareTo(fim) < 0) {
				hora = Double.valueOf(itr.getValue().replace(",", "."));
				if (hora > 8) {
					horas += 8;
				}
				else {
					horas += hora;
				}
			}
		}
		return horas;
	}
	
	public void lancaCartao(String data, String horas) {
		this.cartao.put(data, horas);
	}

	public Map<String, String> getCartao() {
		return cartao;
	}

	public void setCartao(Map<String, String> cartao) {
		this.cartao = cartao;
	}
	
}
