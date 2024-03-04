package br.ufal.ic.p2.wepayu.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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

    
    public static void gerarFolha(Map <String, Empregado> empregados, String saida, String total) {
        StringBuilder folha = new StringBuilder();
        folha.append(saida).append("\n" + "=".repeat(25) + "\n" + "=".repeat(25) + "=".repeat(10))
		.append("=".repeat(10) + "EMPREGADOS" + "=".repeat(64) + "\n" + "=".repeat(119) + "\n")
		.append("Nome  " + " ".repeat(15) + "Salario Bruto  " + "Descontos  " + "Salario Liquido  " + "Metodo\n")
		.append("=".repeat(70) + "\n");
        for (Empregado empregado : empregados.values()) {
        	folha.append(empregado.getNome()).append(" ".repeat(Math.abs(empregado.getNome().length() - 21)))
    		.append(String.format("%.2f",empregado.getPag_bruto()))
    		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_bruto()).length() - 15)))
    		.append(String.format("%.2f",empregado.getDescontos()))
    		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getDescontos()).length() - 13)))
    		.append(String.format("%.2f",empregado.getPag_liq()))
    		.append(" ".repeat(Math.abs(String.format("%.2f",empregado.getPag_liq()).length() - 15)))
    		.append(empregado.getMetodoPagamento()).append("\n");
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
//                Map<String, String> taxas = empregado.getTaxaServico();
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
        }
        folha.append("\n" + "=".repeat(80) + "\n").append("TOTAL" + " ".repeat(Math.abs(total.length() - 50)))
        .append(total);
        escreverArquivo(saida, folha.toString());
    }	  
}