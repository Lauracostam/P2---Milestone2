package br.ufal.ic.p2.wepayu.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;


public class UtilsFileWriterFolha {

    /**
     * <p> Cria a pasta {@code database} caso ela não exista. </p>
     */

    public static void criarPasta() {
        String caminho = "./";

        File diretorio = new File(caminho);

        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
    }

    /**
     * <p> Escreve um arquivo genérico com o nome e conteúdo passados. </p>
     *
     * @param arquivo  Nome do arquivo.
     * @param conteudo     Conteúdo do arquivo.
     */

    public static void escreverArquivo(String arquivo, String conteudo) {
        try{
            File file = new File("./" + arquivo);
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
        	System.out.println(e);
            System.out.println("Erro ao escrever o arquivo " + arquivo);
        }
    }

    
    public static void gerarFolha(Map <String, Empregado> empregados, Map <String, Empregado> horistas, String saida, String total, String data, double horasNormais, double horasExtras, double horasTotais, Map<String, Double> horistasHorasNormais, Map<String, Double> horistasHorasExtras) throws ParseException {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdformat = new SimpleDateFormat("d/M/yyyy");
		Date dataPag = sdformat.parse(data);
		
    	StringBuilder folha = new StringBuilder();
		folha.append("FOLHA DE PAGAMENTO DO DIA " + dateFormat.format(dataPag))
        .append("\n").append( "=".repeat(36) + "\n" + "\n")
        .append("=".repeat(127) + "\n");
        int i = 0, j = 0, k = 0;
        int qt_horistas = 0;
        double pg_horistas = 0;
        int qt_com = 0;
        double pg_com = 0;
        double qt_assalariado = 0;
        double pg_assalariado = 0;
        
        for (Empregado emp : empregados.values()) {
        	if(emp.getTipo().equals("horista")) {
        		qt_horistas++;
        		pg_horistas += emp.getPag_bruto();
        	}
        	else if(emp.getTipo().equals("comissionado")) {
        		qt_com++;
        		pg_com += emp.getPag_bruto();
        	}
        	else{
                qt_assalariado++;
        		pg_assalariado += emp.getPag_bruto();
            }
        }
        if(pg_horistas == 0.0){
            for (Map.Entry<String, Double> entry : horistasHorasNormais.entrySet()) {
                horistasHorasNormais.put(entry.getKey(), 0.0);
		    }
		    for (Map.Entry<String, Double> entry : horistasHorasExtras.entrySet()) {
                horistasHorasExtras.put(entry.getKey(), 0.0);
		    }
        }
        ArrayList<Empregado> empregados_ordenados = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
            list.add(entry.getValue().getNome());
        }
        Collections.sort(list, new Comparator<String>() {
            public int compare(String str, String str1) {
                return (str).compareTo(str1);
            }
        });
        for (String str : list) {
            for (Map.Entry<String, Empregado> entry : empregados.entrySet()) {
                if (entry.getValue().getNome().equals(str)) {
                    empregados_ordenados.add(entry.getValue());
                }
            }
        }
        for (Empregado empregado : empregados_ordenados) {
        	if(empregado.getTipo().equals("horista")) {
        		if(i == 0) {
        			folha.append("=".repeat(21) + " " + "HORISTAS" + " " + "=".repeat(96) + "\n" + "=".repeat(127) + "\n")
        			.append("Nome" + " ".repeat(33) + "Horas" + " " + "Extra" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
        			.append("=".repeat(36) + " " + "=".repeat(5) + " " + "=".repeat(5) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
        		}
        		if((i >= 0 && i < qt_horistas) && empregado.getUltimoPagamento().equals(data)){
        			int tamHorasNormais = String.format("%.0f",horistasHorasNormais.get(empregado.getId())).length();
        			int tamPagBruto = Math.abs(String.format("%.2f",empregado.getPag_bruto()).length());
        			int tamDescontos = Math.abs(String.format("%.2f",empregado.getDescontos()).length());
        			int tamLiquido = Math.abs(String.format("%.2f",empregado.getPag_liq()).length());
        			if(tamHorasNormais < 2) {
        				folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 41)));
        			}
        			else {        				
        				folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 40)));
        			}
        			folha.append(String.format("%.0f",horistasHorasNormais.get(empregado.getId()))).append(" ".repeat(5));
        			if(tamPagBruto <= 5) {
        				folha.append(String.format("%.0f",horistasHorasExtras.get(empregado.getId()))).append(" ".repeat(10));
        			}
        			else {
        				folha.append(String.format("%.0f",horistasHorasExtras.get(empregado.getId()))).append(" ".repeat(8));
        			}
        			if(tamDescontos <= 4) {
        				folha.append(String.format("%.2f",empregado.getPag_bruto()))
        				.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_bruto()).length() - 12)));
        			}
        			else {
        				folha.append(String.format("%.2f",empregado.getPag_bruto()))
        				.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_bruto()).length() - 11)));
        			}
        			if(tamLiquido <= 5) {
        				folha.append(String.format("%.2f",empregado.getDescontos()))
                		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getDescontos()).length() - 16)));
        			}
        			else {
        				folha.append(String.format("%.2f",empregado.getDescontos()))
                		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getDescontos()).length() - 14)));
        			}
        			folha.append(String.format("%.2f",empregado.getPag_liq()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_liq()).length()) - 4));
            		
        			if(empregado.getMetodoPagamento().equals("banco")) {
        				folha.append("Banco do Brasil, Ag. " + empregado.getAgencia()).append(" CC " + empregado.getContaCorrente()).append("\n");
        			}
        			else if(empregado.getMetodoPagamento().equals("emMaos")) {
        				folha.append("Em maos").append("\n");
        			}
        			else {
        				folha.append(empregado.getMetodoPagamento()).append("\n");
        			}
        		}
        		if(i == qt_horistas-1 && empregado.getUltimoPagamento().equals(data)){
        			folha.append("\n" + "TOTAL HORISTAS" + " ".repeat(Math.abs(total.length() - 50)))
        			.append(String.format("%.2f",pg_horistas)).append("\n");
        		}
        		i++;
        	}
        }
        for(Empregado empregado: empregados_ordenados) {
        	if(empregado.getTipo().equals("comissionado")) {
                Comissionado com = (Comissionado) empregado;
        		if(j == 0) {
        			folha.append("=".repeat(21) + " " + "COMISSIONADO" + " " + "=".repeat(96) + "\n" + "=".repeat(127) + "\n")
        			.append("Nome" + " ".repeat(33) + "Fixo" + " " + "Vendas" + " " + "Comissao" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
        			.append("=".repeat(36) + " " + "=".repeat(5) + " " + "=".repeat(5) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
        		}
        		if((j >= 0 && j < qt_com) && empregado.getUltimoPagamento().equals(data)){
        			folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 28)))
        			.append(String.format("%.2f",com.getPag_fixo())).append(" ".repeat(5))
        			.append(String.format("%.2f",com.getTotal_vendas())).append(" ".repeat(5))
        			.append(String.format("%.2f",com.getPag_vendas())).append(" ".repeat(5))
            		.append(String.format("%.2f",com.getPag_bruto()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_bruto()).length() - 15)))
            		.append(String.format("%.2f",empregado.getDescontos()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getDescontos()).length() - 13)))
            		.append(String.format("%.2f",empregado.getPag_liq()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_liq()).length() - 15)));
        			if(empregado.getMetodoPagamento().equals("banco")) {
        				folha.append("Banco do Brasil, Ag. " + empregado.getAgencia()).append(" CC " + empregado.getContaCorrente()).append("\n");
        			}
        			else {
        				folha.append(empregado.getMetodoPagamento()).append("\n");
        			}
        		}
        		if(j == qt_com-1 && empregado.getUltimoPagamento().equals(data)){
        			folha.append("\n" + "TOTAL COMISSIONADOS" + " ".repeat(Math.abs(total.length() - 50)))
        			.append(String.format("%.2f",pg_com));
        		}
        		j++;
        	}
        }
        for (Empregado empregado : empregados_ordenados) {
        	if(empregado.getTipo().equals("assalariado")) {
        		if(k == 0) {
        			folha.append("=".repeat(21) + " " + "ASSALARIADOS" + " " + "=".repeat(96) + "\n" + "=".repeat(127) + "\n")
        			.append("Nome" + " ".repeat(33) + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
        			.append("=".repeat(36) + " " + "=".repeat(5) + " " + "=".repeat(5) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
        		}
        		if((k >= 0 && k < qt_assalariado) && empregado.getUltimoPagamento().equals(data)){
        			int tamHorasNormais = String.format("%.0f",horistasHorasNormais.get(empregado.getId())).length();;
        			int tamPagBruto = String.format("%.2f",empregado.getPag_bruto()).length();
        			if(tamHorasNormais < 2) {
        				folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 41)));
        			}
        			else {        				
        				folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 40)));
        			}
        			if(tamPagBruto <=5) {
        				folha.append(String.format("%.2f",empregado.getPag_bruto())).append(" ".repeat(2));
        			}
        			else {
        				folha.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_bruto()).length() - 5)));
        			}
            		folha.append(String.format("%.2f",empregado.getDescontos()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getDescontos()).length() - 14)))
            		.append(String.format("%.2f",empregado.getPag_liq()))
            		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_liq()).length())));
        			if(empregado.getMetodoPagamento().equals("banco")) {
        				folha.append("Banco do Brasil, Ag. " + empregado.getAgencia()).append(" CC " + empregado.getContaCorrente()).append("\n");
        			}
        			else if(empregado.getMetodoPagamento().equals("emMaos")) {
        				folha.append("Em maos").append("\n");
        			}
        			else {
        				folha.append(empregado.getMetodoPagamento()).append("\n");
        			}
        		}
        		if(k == qt_assalariado-1 && empregado.getUltimoPagamento().equals(data)){
        			folha.append("\n" + "TOTAL ASSALARIADOS" + " ".repeat(Math.abs(total.length() - 50)))
        			.append(String.format("%.2f",pg_horistas)).append("\n");
        		}
        		k++;
        	}
        
        }
    		
//        	if(empregado instanceof Comissionado) {
//        		Comissionado empregado_ = (Comissionado) empregado;
//        		empregadosData.append(empregado.getId()).append(":")
//                .append(empregado.getNome()).append(":")
//                .append(empregado.getSalario()).append(":")
//                .append(empregado.getSindicalizado()).append(":")
//                .append(empregado.getIdSindicato()).append(":")
//                .append(empregado.getTaxaSindical()).append(":")
//                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
//                .append(empregado.getBanco()).append(":")
//                .append(empregado.getAgencia()).append(":")
//                .append(empregado.getContaCorrente()).append(":")
//                .append(empregado.getMetodoPagamento()).append(":")
//                .append(empregado_.getComissao()).append(":")
//                .append(UtilsString.formatArrayList(vendas_lista)).append(";");
//        	}
//        	else if(empregado instanceof Horista) {
//        		Horista empregado_ = (Horista) empregado;
//        		Map<String, String> cartao = empregado_.getCartao();
//        		ArrayList<String> cartao_lista = new ArrayList<String>();
//                for (Map.Entry<String, String> entry : cartao.entrySet()) {
//                	cartao_lista.add(entry.getKey() + "-" + entry.getValue());
//                }
//                Map<String, String> taxa)s = empregado.getTaxaServico();
//        		ArrayList<String> taxas_lista = new ArrayList<String>();
//                for (Map.Entry<String, String> entry : taxas.entrySet()) {
//                	taxas_lista.add(entry.getKey() + "-" + entry.getValue());
//                }
//        		empregadosData.append(empregado.getId()).append(":")
//                .append(empregado.getNome()).append(":")
//                .append(empregado.getEndereco()).append(":").append(empregado.getTipo())
//                .append(":").append(empregado.getSalario()).append(":")
//                .append(empregado.getSindicalizado()).append(":")
//                .append(empregado.getIdSindicato()).append(":")
//                .append(empregado.getTaxaSindical()).append(":")
//                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
//                .append(empregado.getBanco()).append(":")
//                .append(empregado.getAgencia()).append(":")
//                .append(empregado.getContaCorrente()).append(":")
//                .append(empregado.getMetodoPagamento()).append(":")
//                .append(UtilsString.formatArrayList(cartao_lista)).append(";");
//        	}
//        	else {
//        		Map<String, String> taxas = empregado.getTaxaServico();
//        		ArrayList<String> taxas_lista = new ArrayList<String>();
//                for (Map.Entry<String, String> entry : taxas.entrySet()) {
//                	taxas_lista.add(entry.getKey() + "-" + entry.getValue());
//                }
//        		empregadosData.append(empregado.getId()).append(":")
//                .append(empregado.getNome()).append(":")
//                .append(empregado.getEndereco()).append(":").append(empregado.getTipo())
//                .append(":").append(empregado.getSalario()).append(":")
//                .append(empregado.getSindicalizado()).append(":")
//                .append(empregado.getIdSindicato()).append(":")
//                .append(empregado.getTaxaSindical()).append(":")
//                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
//                .append(empregado.getBanco()).append(":")
//                .append(empregado.getAgencia()).append(":")
//                .append(empregado.getContaCorrente()).append(":")
//                .append(empregado.getMetodoPagamento()).append(";");
//        	}
//
//        folha.append("\n" + "TOTAL" + " ".repeat(Math.abs(total.length() - 50)))
//        .append(total);
        escreverArquivo(saida, folha.toString());
    }	  
}