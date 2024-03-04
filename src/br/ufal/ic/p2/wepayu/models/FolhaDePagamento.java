package br.ufal.ic.p2.wepayu.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FolhaDePagamento extends Empregado{

	public FolhaDePagamento(String id, String nome, String endereco, String tipo, String salario) {
		super(id, nome, endereco, tipo, salario);
	}
	
	public static double calcularPagamento(String dataPagamento, Empregado empregado) throws ParseException {
		double pagamento = 0;
		double pagamento_deduzido = 0;
		double descontos = 0;
		double taxa = 0;
		double taxaServico = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = Calendar.getInstance();
		Date dataPag = sdformat.parse(dataPagamento);
		c.setTime(dataPag);
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
				double horasNormais = horista.getHorasNormais(dataInicial, dataFinal);
				double horasExtras = horista.getHorasExtras(dataInicial, dataFinal);
				double horasTrabalhadas = horasNormais + horasExtras;
				if(c.get(Calendar.DAY_OF_WEEK) == 6) {
					pagamento = 0;
					pagamento = (horasTrabalhadas)*(horista.getSalario());
					if(horasTrabalhadas == 17) {
						pagamento += horista.getSalario();
					}
					double deducaoSindicato = Double.valueOf(lancamentos.size()+1)*horista.getTaxaSindical();
					pagamento_deduzido -= deducaoSindicato;
					int j = 0;
					Map<String, String> lancamentos_sind = horista.getTaxaServico();
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
					taxaServico = empregado.getTaxasServico(dataInicial, dataFinal);
					if(taxaServico != 0) {
						pagamento_deduzido -= taxaServico;
					}
					descontos = deducaoSindicato;
				}
				else {
					return 0;
				}
			} else {
				pagamento = 0;
			}
		}
		else if(empregado.getTipo().equals("assalariado")){
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	        int ultimoDiaDoMes = c.get(Calendar.DAY_OF_MONTH);
			if(diaAtual == ultimoDiaDoMes) {
				pagamento = empregado.getSalario();
			}
			else {
				return 0;
			}
			double deducaoSindicato = ultimoDiaDoMes*empregado.getTaxaSindical();
			pagamento_deduzido -= deducaoSindicato;
			int j = 0;
			Map<String, String> lancamentos_sind = empregado.getTaxaServico();
			String chave = null;
			String dataInicial = null;
			String dataFinal = null;
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
			taxaServico = empregado.getTaxasServico(dataInicial, dataFinal);
			if(taxaServico != 0) {
				pagamento_deduzido -= taxaServico;
			}
			descontos = deducaoSindicato;
		}
		else if(empregado.getTipo().equals("comissionado")) {
			Comissionado comissionado = (Comissionado) empregado;
//			int dataQuinzeDias = 14;
//	        int dataAtual = c.get(Calendar.DAY_OF_MONTH);
//	        System.out.println("day of month: " + " " + c.get(Calendar.DAY_OF_MONTH));
//	        c.add(Calendar.DAY_OF_MONTH, 14);
//			System.out.println("quinze dias: " + " " + dataQuinzeDias);
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
				if((c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH)+1 == 14 || c.get(Calendar.MONTH)+1 == 1 && c.get(Calendar.DAY_OF_MONTH) == 28) || (c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 11 || c.get(Calendar.MONTH)+1 == 2 && c.get(Calendar.DAY_OF_MONTH) == 25)) {
					double vendas = comissionado.getVendasRealizadas(dataInicial, dataFinal);
					double comissao = comissionado.getComissao();
					pagamento = (vendas*comissao) + comissionado.getSalario();
					double deducaoSindicato = Double.valueOf(lancamentos.size()+1)*comissionado.getTaxaSindical();
					pagamento_deduzido -= deducaoSindicato;
					int j = 0;
					Map<String, String> lancamentos_sind = comissionado.getTaxaServico();
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
					taxaServico = empregado.getTaxasServico(dataInicial, dataFinal);
					if(taxaServico != 0) {
						pagamento_deduzido -= taxaServico;
					}
					descontos = deducaoSindicato;
				}
				}
				else {
					return 0;
				}
		}
		if(pagamento < 0) {
			pagamento = 0;
		}
		empregado.setPag_bruto(pagamento);
		empregado.setPag_liq(pagamento_deduzido);
		empregado.setDescontos(descontos);
		
		return pagamento;
	}
	
}
