package br.ufal.ic.p2.wepayu.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.SortedSet;
import java.util.TreeSet;

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
		Date firstDate = sdformat.parse(emp.getUltimoPagamento());
	    Date secondDate = sdformat.parse(dataAtual);
	    
	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS); 
		if(c.get(Calendar.DAY_OF_WEEK) == 6) {
			double deducaoSindicato = (vezes_deducao)*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			descontos = deducaoSindicato;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			if (lancamentos_sind.size() != 0) {
				String chave = null;
				Map<Date, String> lancamentos_mes = new HashMap<Date, String>();
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							lancamentos_mes.put(sdformat.parse(itr.getKey()), itr.getValue());
						}
				}				
				ArrayList<String> lancamentos_ordenados = new ArrayList<>();
				SortedSet<Date> keys = new TreeSet<>(lancamentos_mes.keySet());
		        for (Date date : keys) {
		            lancamentos_ordenados.add(sdformat.format(date));
		        }
		        for (String date : lancamentos_ordenados) {
						dataFinal = date;
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
			if(roda && pagamento != 0){
				emp.setUltimoPagamento_Desconto(dataAtual);
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
			double deducaoSindicato = (ultimoDiaDoMes)*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			descontos = deducaoSindicato;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			String dataInicial = null;
			String dataFinal = null;
			if(lancamentos_sind.size() != 0) {
				String chave = null;
				Map<Date, String> lancamentos_mes = new HashMap<Date, String>();
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							lancamentos_mes.put(sdformat.parse(itr.getKey()), itr.getValue());
						}
				}				
				ArrayList<String> lancamentos_ordenados = new ArrayList<>();
				SortedSet<Date> keys = new TreeSet<>(lancamentos_mes.keySet());
		        for (Date date : keys) {
		            lancamentos_ordenados.add(sdformat.format(date));
		        }
		        for (String date : lancamentos_ordenados) {
						dataFinal = date;
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
			if(roda && pagamento != 0){
				emp.setUltimoPagamento_Desconto(dataAtual);
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
		Date firstDate = sdformat.parse(emp.getUltimoPagamento());
	    Date secondDate = sdformat.parse(dataAtual);
	    
	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if(diff >= 8 && c.get(Calendar.DAY_OF_WEEK) == 6) {
		//if((c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH) == 14 || c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH) == 28) || (c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 11 || c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 25)) {
			double deducaoSindicato = (vezes_deducao)*emp.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			descontos = deducaoSindicato;
			Map<String, String> lancamentos_sind = emp.getTaxaServico();
			if(lancamentos_sind.size() != 0) {
				String chave = null;
				Map<Date, String> lancamentos_mes = new HashMap<Date, String>();
				for (Map.Entry<String,String> itr : lancamentos_sind.entrySet()) {
					chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							lancamentos_mes.put(sdformat.parse(itr.getKey()), itr.getValue());
						}
				}				
				ArrayList<String> lancamentos_ordenados = new ArrayList<>();
				SortedSet<Date> keys = new TreeSet<>(lancamentos_mes.keySet());
		        for (Date date : keys) {
		            lancamentos_ordenados.add(sdformat.format(date));
		        }
		        for (String date : lancamentos_ordenados) {
						dataFinal = date;
						
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
			if(roda && pagamento != 0){
				emp.setUltimoPagamento_Desconto(dataAtual);
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
		//System.out.println(dataPagamento);
		mes = c.get(Calendar.MONTH)+1;
		Date firstDate = sdformat.parse(empregado.getUltimoPagamento_Desconto());
		Date secondDate = sdformat.parse(dataPagamento);
		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		if (c.get(Calendar.MONTH)+1 == 2) {
			diff -=1;	
		}
		
        int diaAtual = c.get(Calendar.DAY_OF_MONTH);
        
		if(empregado.getTipo().equals("horista")) {
			Horista horista = (Horista) empregado;
			String dataInicial = null;
			String dataFinal = null;
			Map<String, String> lancamentos = horista.getCartao();
			if(lancamentos.size() != 0) {
				String chave = null;
				Map<Date, String> lancamentos_mes = new HashMap<Date, String>();
				for (Map.Entry<String,String> itr : lancamentos.entrySet()) {
					chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							lancamentos_mes.put(sdformat.parse(itr.getKey()), itr.getValue());
						}
				}
				ArrayList<String> lancamentos_ordenados = new ArrayList<>();
				SortedSet<Date> keys = new TreeSet<>(lancamentos_mes.keySet());
		        for (Date date : keys) {
		            lancamentos_ordenados.add(sdformat.format(date));
		        }
		        for (String date : lancamentos_ordenados) {
						dataFinal = date;
				}
				dataInicial = empregado.getUltimoPagamento();
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				double horasNormais = horista.getHorasNormais(dataInicial, todayAsString);
				double horasExtras = horista.getHorasExtras(dataInicial, todayAsString);
				horasTrabalhadas = horasNormais + horasExtras+1;
				pagamento = 0;
				pagamento = (horasTrabalhadas)*(horista.getSalario());
				if(horasTrabalhadas == 1){
					horasTrabalhadas = 0;
					pagamento = 0;
				}
				if(pagamento > 0 && horasTrabalhadas == 17) {
					pagamento += horista.getSalario();
				}
				pagamento = calcularSemanal(c, sdformat, horista, diff+1, pagamento, dataInicial, dataFinal, dataPagamento, roda);
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
				pagamento = calcularSemanal(c, sdformat, horista, diff+1, pagamento, dataInicial, dataFinal, dataPagamento, roda);
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
	        pagamento = calcularMensal(c, sdformat, empregado, diff+1, pagamento, diaAtual, ultimoDiaDoMes, dataPagamento, roda);
	        //System.out.println("assalariados: " + pagamento);
		}
		else if(empregado.getTipo().equals("comissionado")) {
			Comissionado comissionado = (Comissionado) empregado;
			String dataInicial = null;
			String dataFinal = null;
			Map<String, String> lancamentos = comissionado.getVendas();
			if(lancamentos.size() != 0) {
				String chave = null;
				Map<Date, String> lancamentos_mes = new HashMap<Date, String>();
				for (Map.Entry<String,String> itr : lancamentos.entrySet()) {
					chave = itr.getKey();
						String[] chave_mes = chave.split("/");
						if(c.get(Calendar.MONTH)+1 == Integer.valueOf(chave_mes[1])) {
							lancamentos_mes.put(sdformat.parse(itr.getKey()), itr.getValue());
						}
				}				
				ArrayList<String> lancamentos_ordenados = new ArrayList<>();
				SortedSet<Date> keys = new TreeSet<>(lancamentos_mes.keySet());
		        for (Date date : keys) {
		            lancamentos_ordenados.add(sdformat.format(date));
		        }
		        for (String date : lancamentos_ordenados) {
						dataFinal = date;
				}
		        //System.out.println("Fim dos Lancamentos");
				dataInicial = empregado.getUltimoPagamento();
				Date date_add = sdformat.parse(dataFinal);
				Date tomorrow = new Date(date_add.getTime() + (1000 * 60 * 60 * 24));
				String todayAsString = sdformat.format(tomorrow);
				
				double vendas = comissionado.getVendasRealizadas(dataInicial, todayAsString);
				double comissao = comissionado.getComissao();

				double salario = comissionado.getSalario()*(24.0/52.0);
				int temp = (int)(salario*100.0);
			    double salario_short = ((double)temp)/100.0;
				
				double total_comissao = (vendas*comissao);
				temp = (int)(total_comissao*100.0);
				double total_comissao_short = ((double)temp)/100.0;
				pagamento = total_comissao_short + (salario_short);
				pagamento = calcularBiSemanal(c, sdformat, comissionado, 14, pagamento, dataInicial, dataFinal, dataPagamento, roda);
				comissionado.setPag_fixo(salario_short);
				comissionado.setPag_vendas(total_comissao_short);
				comissionado.setTotal_vendas(vendas);
			}
			else {
				double salario = comissionado.getSalario()*(24.0/52.0);
				int temp = (int)(salario*100.0);
			    double salario_short = ((double)temp)/100.0;
			    pagamento = salario_short;
				pagamento = calcularBiSemanal(c, sdformat, comissionado, 14, pagamento, dataInicial, dataFinal, dataPagamento, roda);
				comissionado.setPag_fixo(salario_short);
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

	public static void setHoristasHorasNormais(Map<String, Double> horistasHorasNormais) {
		FolhaDePagamento.horistasHorasNormais = horistasHorasNormais;
	}

	public static Map<String, Double> getHoristasHorasExtras() {
		return horistasHorasExtras;
	}

	public static void setHoristasHorasExtras(Map<String, Double> horistasHorasExtras) {
		FolhaDePagamento.horistasHorasExtras = horistasHorasExtras;
	}
	
}
