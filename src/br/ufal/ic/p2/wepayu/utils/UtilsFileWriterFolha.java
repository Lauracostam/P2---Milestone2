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
        double pg_horistas_bruto = 0;
        double pg_horistas_descontos = 0;
        double pg_horistas_liq = 0;
        int qt_com = 0;
        double pg_com = 0;
        double pg_com_liq = 0;
        double pg_com_descontos = 0;
        double pg_com_vendas = 0;
        double pg_com_comissao = 0;
        double pg_com_fixo = 0;
        double qt_assalariado = 0;
        double pg_assalariado = 0;
        double pg_assalariado_descontos = 0;
        double pg_assalariado_liq = 0;
        
        for (Empregado emp : empregados.values()) {
        	if(emp.getTipo().equals("horista")) {
        		qt_horistas++;
        		pg_horistas_bruto += emp.getPag_bruto();
        		pg_horistas_descontos += emp.getDescontos();
        		pg_horistas_liq += emp.getPag_liq();
        	}
        	else if(emp.getTipo().equals("comissionado")) {
                Comissionado com = (Comissionado) emp;
        		qt_com++;
        		pg_com += emp.getPag_bruto();
        		pg_com_liq += emp.getPag_liq();
        		pg_com_descontos += emp.getDescontos();
        		pg_com_vendas += com.getTotal_vendas();
        		pg_com_comissao += com.getPag_vendas();
        		pg_com_fixo += com.getPag_fixo();
        	}
        	else{
                qt_assalariado++;
        		pg_assalariado += emp.getPag_bruto();
        		pg_assalariado_descontos += emp.getDescontos();
        		pg_assalariado_liq += emp.getPag_liq();
            }
        }
        if(pg_horistas_bruto == 0.0){
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
        		if(pg_horistas_bruto != 0 || empregado.getUltimoPagamento().equals(data)) {
        			if(i == 0) {
            			folha.append("=".repeat(21) + " " + "HORISTAS" + " " + "=".repeat(96) + "\n" + "=".repeat(127) + "\n")
            			.append("Nome" + " ".repeat(33) + "Horas" + " " + "Extra" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
            			.append("=".repeat(36) + " " + "=".repeat(5) + " " + "=".repeat(5) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
            		}
            		if((i >= 0 && i < qt_horistas) && empregado.getUltimoPagamento().equals(data)){
            			String metodo = empregado.getMetodoPagamento();
            			if(metodo.equals("emMaos")) {
            				metodo = "Em maos";
            			}
            			else if(metodo.equals("banco")) {
            				metodo = String.format("%s, Ag. %s CC %s", empregado.getBanco(),
                                    empregado.getAgencia(), empregado.getContaCorrente());
            			}
            			else if(metodo.equals("correios")) {
            				metodo = String.format("Correios, %s", empregado.getEndereco());
            			}
            			String linha = String.format("%-36s %5s %5s %13s %9s %15s %s", empregado.getNome(),
            					String.format("%.0f",horistasHorasNormais.get(empregado.getId())), 
            					String.format("%.0f",horistasHorasExtras.get(empregado.getId())), 
            					String.format("%.2f",empregado.getPag_bruto()), 
            					String.format("%.2f",empregado.getDescontos()), 
            					String.format("%.2f",empregado.getPag_liq()), 
            					metodo);
            			folha.append(linha).append("\n");
            			
            		}
            		if(i == qt_horistas-1 && empregado.getUltimoPagamento().equals(data)){
            			String total_horistas = String.format("\n%-36s %5s %5s %13s %9s %15s\n", "TOTAL HORISTAS",
            					String.format("%.0f",horasNormais), 
            					String.format("%.0f",horasExtras),
            					String.format("%.2f",pg_horistas_bruto),
            					String.format("%.2f",pg_horistas_descontos),
            					String.format("%.2f",pg_horistas_liq)                      
                        );
            			folha.append(total_horistas).append("\n");
            		}
            		i++;
            	} else {
            		folha.append("=".repeat(21) + " " + "HORISTAS" + " " + "=".repeat(96) + "\n" + "=".repeat(127) + "\n")
        			.append("Nome" + " ".repeat(33) + "Horas" + " " + "Extra" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
        			.append("=".repeat(36) + " " + "=".repeat(5) + " " + "=".repeat(5) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
            		
            		String total_horistas = String.format("\n%-36s %5s %5s %13s %9s %15s\n", "TOTAL HORISTAS",
        					String.format("%.0f",0.0), 
        					String.format("%.0f",0.0),
        					String.format("%.2f",0.0),
        					String.format("%.2f",0.0),
        					String.format("%.2f",0.0)                      
                    );
            		
        			folha.append(total_horistas).append("\n");
        			break;
            	}
        	}	
        } 
        for (Empregado empregado : empregados_ordenados) {
            if(empregado.getTipo().equals("assalariado")) {
                if(pg_assalariado != 0.0 || empregado.getUltimoPagamento().equals(data)){
                    if(k == 0) {
	        			folha.append("=".repeat(127) + "\n")
	        			.append("=".repeat(21) + " " + "ASSALARIADOS" + " " + "=".repeat(92) + "\n")
	        			.append("=".repeat(127) + "\n")
	        			.append("Nome" + " ".repeat(45) + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
	        			.append("=".repeat(48) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
	        		}
	        		if((k >= 0 && k < qt_assalariado) && empregado.getUltimoPagamento().equals(data)){
	        			String metodo = empregado.getMetodoPagamento();
	        			if(metodo.equals("emMaos")) {
            				metodo = "Em maos";
            			}
            			else if(metodo.equals("banco")) {
            				metodo = String.format("%s, Ag. %s CC %s", empregado.getBanco(),
                                    empregado.getAgencia(), empregado.getContaCorrente());
            			}
            			else if(metodo.equals("correios")) {
            				metodo = String.format("Correios, %s", empregado.getEndereco());
            			}
                        String linha2 = String.format("%-48s %13s %9s %15s %s", empregado.getNome(),
	        					String.format("%.2f",empregado.getPag_bruto()), String.format("%.2f",empregado.getDescontos()), String.format("%.2f",empregado.getPag_liq()), metodo);
	        			folha.append(linha2).append("\n");
	        		}
	        		if(k == qt_assalariado-1 && empregado.getUltimoPagamento().equals(data)){
	        			String total_assalariados = String.format("\n%-48s %13s %9s %15s\n", "TOTAL ASSALARIADOS",
	        					String.format("%.2f",pg_assalariado),
	        					String.format("%.2f",pg_assalariado_descontos),
	        					String.format("%.2f",pg_assalariado_liq)
	                    );
	        			folha.append(total_assalariados).append("\n");
	        		}
	        		k++;
	        	}
                
	        	else{
                    folha.append("=".repeat(127) + "\n")
	        			.append("=".repeat(21) + " " + "ASSALARIADOS" + " " + "=".repeat(92) + "\n")
	        			.append("=".repeat(127) + "\n")
	        			.append("Nome" + " ".repeat(45) + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
	        			.append("=".repeat(48) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
	        		
                   
                    String total_assalariados = String.format("\n%-48s %13s %9s %15s\n", "TOTAL ASSALARIADOS",
	        					String.format("%.2f",0.0),
	        					String.format("%.2f",0.0),
	        					String.format("%.2f",0.0)
	                    );
	        			folha.append(total_assalariados).append("\n");
	        			break;
                }
                    
        	}
        		
        
        }
        for(Empregado empregado: empregados_ordenados) {
        	if(empregado.getTipo().equals("comissionado")) {
                if(pg_com != 0.0 || empregado.getUltimoPagamento().equals(data)){
                    Comissionado com = (Comissionado) empregado;
	        		if(j == 0) {
	        			folha.append("=".repeat(127) + "\n")
	        			.append("=".repeat(21) + " " + "COMISSIONADOS" + " " + "=".repeat(91) + "\n")
	        			.append("=".repeat(127) + "\n")
	        			.append("Nome" + " ".repeat(18) + "Fixo" + " ".repeat(5) + "Vendas" + " ".repeat(3) + "Comissao" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
	        			.append("=".repeat(21) + " " + "=".repeat(8) + " " + "=".repeat(8) + " " + "=".repeat(8) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
	        			
	        		}
	        		if((j >= 0 && j < qt_com) && empregado.getUltimoPagamento().equals(data)){	        			
	        			String metodo = empregado.getMetodoPagamento();
            			if(metodo.equals("emMaos")) {
            				metodo = "Em maos";
            			}
            			else if(metodo.equals("banco")) {
            				metodo = String.format("%s, Ag. %s CC %s", empregado.getBanco(),
                                    empregado.getAgencia(), empregado.getContaCorrente());
            			}
            			else if(metodo.equals("correios")) {
            				metodo = String.format("Correios, %s", empregado.getEndereco());
            			}
            			String linha = String.format("%-21s %8s %8s %8s %13s %9s %15s %s", empregado.getNome(),
                				String.format("%.2f",com.getPag_fixo()),
                				String.format("%.2f",com.getTotal_vendas()),
                				String.format("%.2f",com.getPag_vendas()),
                				String.format("%.2f",com.getPag_bruto()), 
                				String.format("%.2f",empregado.getDescontos()), 
                				String.format("%.2f",empregado.getPag_liq()), 
                				metodo);
            			folha.append(linha).append("\n");
	        		}
	        		if(j == qt_com-1 && empregado.getUltimoPagamento().equals(data)){
	        			String total_com = String.format("\n%-21s %8s %8s %8s %13s %9s %15s\n", "TOTAL COMISSIONADOS",
	                    String.format("%.2f",pg_com_fixo),
	                    String.format("%.2f",pg_com_vendas),
	                    String.format("%.2f",pg_com_comissao),
	                    String.format("%.2f",pg_com),
	                    String.format("%.2f",pg_com_descontos),
	                    String.format("%.2f",pg_com_liq)
	            		);
	            		folha.append(total_com).append("\n");
	        		}
	        		j++;
	        	}
	        	else{
                    folha.append("=".repeat(127) + "\n")
	        			.append("=".repeat(21) + " " + "COMISSIONADOS" + " " + "=".repeat(91) + "\n")
	        			.append("=".repeat(127) + "\n")
	        			.append("Nome" + " ".repeat(18) + "Fixo" + " ".repeat(5) + "Vendas" + " ".repeat(3) + "Comissao" + " " + "Salario Bruto" + " " + "Descontos" + " " + "Salario Liquido" + " " + "Metodo\n")
	        			.append("=".repeat(21) + " " + "=".repeat(8) + " " + "=".repeat(8) + " " + "=".repeat(8) + " " + "=".repeat(13) + " " + "=".repeat(9) + " " + "=".repeat(15) + " " + "=".repeat(38) + "\n");
	        			
	        		String total_com = String.format("\n%-21s %8s %8s %8s %13s %9s %15s\n", "TOTAL COMISSIONADOS",
	                    String.format("%.2f",0.0),
	                    String.format("%.2f",0.0),
	                    String.format("%.2f",0.0),
	                    String.format("%.2f",0.0),
	                    String.format("%.2f",0.0),
	                    String.format("%.2f",0.0)
	            		);
	            	folha.append(total_com).append("\n");
	            	break;
                    
                }
        	}
                
        }
    		
        folha.append("TOTAL FOLHA: ").append(total).append("\n");
        escreverArquivo(saida, folha.toString());
    }	  
}