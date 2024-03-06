package br.ufal.ic.p2.wepayu.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FolhaDePagamento extends Empregado{
	
	private static double totalHorasNormais = 0;
	private static double totalHorasExtras = 0;
	private static Map<String, Double> horistasHorasNormais = new HashMap<String, Double>();
	private static Map<String, Double> horistasHorasExtras = new HashMap<String, Double>();
	private static int mes = 0;

	public FolhaDePagamento(String id, String nome, String endereco, String tipo, String salario) {
		super(id, nome, endereco, tipo, salario);
	}
	
	public static double calcularSemanal(Calendar c, SimpleDateFormat sdformat, Empregado emp, double vezes_deducao, double pagamento, String dataInicial, String dataFinal, String dataAtual, boolean roda) throws ParseException{
		double pagamento_deduzido = pagamento;
		double taxaServico = 0;
		double descontos = 0;
		String chave = null;
		if(c.get(Calendar.DAY_OF_WEEK) == 6) {
			double deducaoSindicato = vezes_deducao*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			int j = 0;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			if (lancamentos_sind.size() != 0) {
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					if(j == 0) {
						chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							dataFinal = itr.getKey();
							j++;
						}
					}
					else {
						dataInicial = itr.getKey();
					}
				}
				dataInicial = emp.getUltimoPagamento();
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				taxaServico = emp.getTaxasServico(dataInicial, todayAsString);
				if(taxaServico != 0) {
					pagamento_deduzido -= taxaServico;
				}
				descontos = deducaoSindicato+taxaServico;
			}
			else{
				descontos = deducaoSindicato;
			}
			if(pagamento <= 0) {
				pagamento = 0;
			}
			if(roda) {
				emp.setUltimoPagamento(dataAtual);
			}
		}
		else {
			pagamento = 0;
		}
		if(pagamento_deduzido < 0) {
			pagamento_deduzido = 0;
		}
		if(descontos < 0 || pagamento == 0) {
			descontos = 0;
		}
		emp.setPag_bruto(pagamento);
		emp.setPag_liq(pagamento_deduzido);
		emp.setDescontos(descontos);
		return pagamento;
	}
	
	public static double calcularMensal(Calendar c, SimpleDateFormat sdformat, Empregado emp, double vezes_deducao, double pagamento, int diaAtual, int ultimoDiaDoMes, String dataAtual, boolean roda) throws ParseException{
		double pagamento_deduzido = pagamento;
		double taxaServico = 0;
		double descontos = 0;
		if(diaAtual == ultimoDiaDoMes) {
			double deducaoSindicato = Double.valueOf(ultimoDiaDoMes)*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			int j = 0;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			String chave = null;
			String dataInicial = null;
			String dataFinal = null;
			if(lancamentos_sind.size() != 0) {
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					if(j == 0) {
						chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							dataFinal = itr.getKey();
							j++;
						}	
					}
					else {
						dataInicial = itr.getKey();
					}
						
				}
				dataInicial = emp.getUltimoPagamento();
				if(dataFinal != null && dataInicial != null) {
					Date date_add = sdformat.parse(dataFinal);
					Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
					String todayAsString = sdformat.format(tomorrow);
					taxaServico = emp.getTaxasServico(dataInicial, todayAsString);
					if(taxaServico != 0) {
						pagamento_deduzido -= taxaServico;
					}
					descontos = deducaoSindicato+taxaServico;
				}
			}
			else{
				descontos = deducaoSindicato;
			}
			if(pagamento <= 0) {
				pagamento = 0;
			}
			if(roda){
				emp.setUltimoPagamento(dataAtual);
			}
		}
		else {
			pagamento = 0;
		}
		if(pagamento_deduzido < 0) {
			pagamento_deduzido = 0;
		}
		if(descontos < 0 || pagamento == 0) {
			descontos = 0;
		}
		emp.setPag_bruto(pagamento);
		emp.setPag_liq(pagamento_deduzido);
		emp.setDescontos(descontos);
		return pagamento;
	}
	
	public static double calcularBiSemanal(Calendar c, SimpleDateFormat sdformat, Empregado emp, double vezes_deducao, double pagamento, String dataInicial, String dataFinal, String dataAtual, boolean roda) throws ParseException{
		double pagamento_deduzido = pagamento;
		double taxaServico = 0;
		double descontos = 0;
		String chave = null;
		Date firstDate = sdformat.parse(emp.getUltimoPagamento());
	    Date secondDate = sdformat.parse(dataAtual);
	    
	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if(diff >= 13 && c.get(Calendar.DAY_OF_WEEK) == 6) {
		//if((c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH) == 14 || c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH) == 28) || (c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 11 || c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 25)) {
			double deducaoSindicato = vezes_deducao*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			int j = 0;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			if(lancamentos_sind.size() != 0) {
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					if(j == 0) {
						chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							dataFinal = itr.getKey();
							j++;
						}	
					}
					else {
						dataInicial = itr.getKey();
					}
				}
				dataInicial = emp.getUltimoPagamento();
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				taxaServico = emp.getTaxasServico(dataInicial, todayAsString);
				if(taxaServico != 0) {
					pagamento_deduzido -= taxaServico;
				}
				descontos = deducaoSindicato+taxaServico;	
			}
			else{
				descontos = deducaoSindicato;
			}
			if(pagamento <= 0) {
				pagamento = 0;
			}
			if(roda){
				emp.setUltimoPagamento(dataAtual);
			}
		}
		else {
			pagamento = 0;
		}
		if(pagamento_deduzido < 0) {
			pagamento_deduzido = 0;
		}
		if(descontos < 0 || pagamento == 0) {
			descontos = 0;
		}
		emp.setPag_bruto(pagamento);
		emp.setPag_liq(pagamento_deduzido);
		emp.setDescontos(descontos);
		return pagamento;
	}
	
	
	public static double calcularPagamento(String dataPagamento, Empregado empregado, boolean roda) throws ParseException {
		double pagamento = 0;
		double horasTrabalhadas = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("d/M/yyyy");
		Calendar c = Calendar.getInstance();
		Date dataPag = sdformat.parse(dataPagamento);
		c.setTime(dataPag);
		if (mes != c.get(Calendar.MONTH)+1) {
			mes = c.get(Calendar.MONTH)+1;
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(dataPag);
			empregado.setUltimoPagamento(sdformat.format(cal.getTime()));		
		}
		//System.out.println(dataPag);
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
		if(empregado.getTipo().equals("horista")) {
			Horista horista = (Horista) empregado;
			String dataInicial = null;
			String dataFinal = null;
			Map<String, String> lancamentos = horista.getCartao();
			if(lancamentos.size() != 0) {
				int i = 0;
				String chave = null;
				for (Map.Entry<String,String> itr : lancamentos.entrySet()) {
					if(i == 0) {
						chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							dataFinal = itr.getKey();
							i++;
						}
						
					}
					else {
						dataInicial = itr.getKey();
					}
					
				}
				dataInicial = horista.getUltimoPagamento();
				//String[] break_str = dataFinal.split("/");
				//String[] break_str2 = dataInicial.split("/");
				//if(break_str[1].equals(break_str2[1])){
					//System.out.println(dataFinal);
					//System.out.println(dataInicial);
					
				//}
				//System.out.println(dataInicial + " " + dataFinal);
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				double horasNormais = horista.getHorasNormais(dataInicial, todayAsString);
				double horasExtras = horista.getHorasExtras(dataInicial, todayAsString);
				horasTrabalhadas = horasNormais + horasExtras+1;
//				System.out.println(empregado.getNome());
//				System.out.println(empregado.getTipo());
//				System.out.println(horasNormais);
//				System.out.println(horasExtras);
//				System.out.println(horasTrabalhadas);
				pagamento = 0;
				pagamento = (horasTrabalhadas)*(horista.getSalario());
				if(horasTrabalhadas == 1){
					horasTrabalhadas = 0;
					pagamento = 0;
				}
				if(pagamento > 0 && horasTrabalhadas == 17) {
					pagamento += horista.getSalario();
				}
				pagamento = calcularSemanal(c, sdformat, horista, Double.valueOf(lancamentos.size()+1), pagamento, dataInicial, dataFinal, dataPagamento, roda);
				if(pagamento != 0) {
					setTotalHorasNormais(horasNormais+getTotalHorasNormais());
					if(horasExtras != 0) {
						horasExtras += 1;
					}
					setTotalHorasExtras(horasExtras+getTotalHorasExtras());
					
					horistasHorasNormais.put(horista.getId(), horasNormais);
					horistasHorasExtras.put(horista.getId(), horasExtras);
				}
			} else {
				pagamento = 0;
				pagamento = calcularSemanal(c, sdformat, horista, Double.valueOf(lancamentos.size()+1), pagamento, dataInicial, dataFinal, dataPagamento, roda);
				if(horasTrabalhadas == 1){
					horasTrabalhadas = 0;
					pagamento = 0;
				}
				horistasHorasNormais.put(horista.getId(), 0.0);
				horistasHorasExtras.put(horista.getId(), 0.0);
			}
			//System.out.println("horistas: " + pagamento + " Horas: " + horasTrabalhadas);
		}
		else if(empregado.getTipo().equals("assalariado")){
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	        int ultimoDiaDoMes = c.get(Calendar.DAY_OF_MONTH);
	        pagamento = empregado.getSalario();
	        pagamento = calcularMensal(c, sdformat, empregado, Double.valueOf(ultimoDiaDoMes), pagamento, diaAtual, ultimoDiaDoMes, dataPagamento, roda);
	        //System.out.println("assalariados: " + pagamento);
		}
		else if(empregado.getTipo().equals("comissionado")) {
			Comissionado comissionado = (Comissionado) empregado;
			String dataInicial = null;
			String dataFinal = null;
			Map<String, String> lancamentos = comissionado.getVendas();
			if(lancamentos.size() != 0) {
				int i = 0;
				String chave = null;
				for (Map.Entry<String,String> itr : lancamentos.entrySet()) {
					if(i == 0) {
						chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							dataFinal = itr.getKey();
							i++;
						}
						
					}
					else {
						dataInicial = itr.getKey();
					}
					
				}
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				double vendas = comissionado.getVendasRealizadas(dataInicial, todayAsString);
				double comissao = comissionado.getComissao();
				pagamento = (vendas*comissao) + (comissionado.getSalario()*(24.0/52.0));
				pagamento = calcularBiSemanal(c, sdformat, comissionado, Double.valueOf(lancamentos.size()+1), pagamento, dataInicial, dataFinal, dataPagamento, roda);
			}
			else {
				pagamento = comissionado.getSalario()*(24.0/52.0);
				pagamento = calcularBiSemanal(c, sdformat, comissionado, Double.valueOf(lancamentos.size()+1), pagamento, dataInicial, dataFinal, dataPagamento, roda);
			}
			//System.out.println("comissionados: " + pagamento);
		}
		if(pagamento != 0) {
			empregado.setPag_bruto(pagamento);
		}
		return pagamento;
	}

	public static double getTotalHorasNormais() {
		return totalHorasNormais;
	}

	public static void setTotalHorasNormais(double totalHorasNormais) {
		FolhaDePagamento.totalHorasNormais = totalHorasNormais;
	}

	public static double getTotalHorasExtras() {
		return totalHorasExtras;
	}

	public static void setTotalHorasExtras(double totalHorasExtras) {
		FolhaDePagamento.totalHorasExtras = totalHorasExtras;
	}

	public static Map<String, Double> getHoristasHorasNormais() {
		return horistasHorasNormais;
	}

	public void setHoristasHorasNormais(Map<String, Double> horistasHorasNormais) {
		FolhaDePagamento.horistasHorasNormais = horistasHorasNormais;
	}

	public static Map<String, Double> getHoristasHorasExtras() {
		return horistasHorasExtras;
	}

	public void setHoristasHorasExtras(Map<String, Double> horistasHorasExtras) {
		FolhaDePagamento.horistasHorasExtras = horistasHorasExtras;
	}

	public static int getMes() {
		return mes;
	}

	public static void setMes(int mes) {
		FolhaDePagamento.mes = mes;
	}
	
}
