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


public class UtilsFileWriter {

    /**
     * <p> Cria a pasta {@code database} caso ela não exista. </p>
     */

    public static void criarPasta() {
        String caminho = "./database";

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
            File file = new File("./database/" + arquivo);
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Erro ao escrever o arquivo " + arquivo);
        }
    }

    
    public static void salvarEmpregados(Map <String, Empregado> empregados) {
        StringBuilder empregadosData = new StringBuilder();
        for (Empregado empregado : empregados.values()) {
        	if(empregado instanceof Comissionado) {
        		Comissionado empregado_ = (Comissionado) empregado;
        		Map<String, String> vendas = empregado_.getVendas();
        		ArrayList<String> vendas_lista = new ArrayList<String>();
                for (Map.Entry<String, String> entry : vendas.entrySet()) {
                	vendas_lista.add(entry.getKey() + "-" + entry.getValue());
                }
                Map<String, String> taxas = empregado.getTaxaServico();
        		ArrayList<String> taxas_lista = new ArrayList<String>();
                for (Map.Entry<String, String> entry : taxas.entrySet()) {
                	taxas_lista.add(entry.getKey() + "-" + entry.getValue());
                }
        		empregadosData.append(empregado.getId()).append(":")
                .append(empregado.getNome()).append(":")
                .append(empregado.getEndereco()).append(":")
                .append(empregado.getTipo()).append(":")
                .append(empregado.getSalario()).append(":")
                .append(empregado.getSindicalizado()).append(":")
                .append(empregado.getIdSindicato()).append(":")
                .append(empregado.getTaxaSindical()).append(":")
                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
                .append(empregado.getBanco()).append(":")
                .append(empregado.getAgencia()).append(":")
                .append(empregado.getContaCorrente()).append(":")
                .append(empregado.getMetodoPagamento()).append(":")
                .append(empregado_.getComissao()).append(":")
                .append(UtilsString.formatArrayList(vendas_lista)).append(";");
        	}
        	else if(empregado instanceof Horista) {
        		Horista empregado_ = (Horista) empregado;
        		Map<String, String> cartao = empregado_.getCartao();
        		ArrayList<String> cartao_lista = new ArrayList<String>();
                for (Map.Entry<String, String> entry : cartao.entrySet()) {
                	cartao_lista.add(entry.getKey() + "-" + entry.getValue());
                }
                Map<String, String> taxas = empregado.getTaxaServico();
        		ArrayList<String> taxas_lista = new ArrayList<String>();
                for (Map.Entry<String, String> entry : taxas.entrySet()) {
                	taxas_lista.add(entry.getKey() + "-" + entry.getValue());
                }
        		empregadosData.append(empregado.getId()).append(":")
                .append(empregado.getNome()).append(":")
                .append(empregado.getEndereco()).append(":").append(empregado.getTipo())
                .append(":").append(empregado.getSalario()).append(":")
                .append(empregado.getSindicalizado()).append(":")
                .append(empregado.getIdSindicato()).append(":")
                .append(empregado.getTaxaSindical()).append(":")
                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
                .append(empregado.getBanco()).append(":")
                .append(empregado.getAgencia()).append(":")
                .append(empregado.getContaCorrente()).append(":")
                .append(empregado.getMetodoPagamento()).append(":")
                .append(UtilsString.formatArrayList(cartao_lista)).append(";");
        	}
        	else {
        		Map<String, String> taxas = empregado.getTaxaServico();
        		ArrayList<String> taxas_lista = new ArrayList<String>();
                for (Map.Entry<String, String> entry : taxas.entrySet()) {
                	taxas_lista.add(entry.getKey() + "-" + entry.getValue());
                }
        		empregadosData.append(empregado.getId()).append(":")
                .append(empregado.getNome()).append(":")
                .append(empregado.getEndereco()).append(":").append(empregado.getTipo())
                .append(":").append(empregado.getSalario()).append(":")
                .append(empregado.getSindicalizado()).append(":")
                .append(empregado.getIdSindicato()).append(":")
                .append(empregado.getTaxaSindical()).append(":")
                .append(UtilsString.formatArrayList(taxas_lista)).append(":")
                .append(empregado.getBanco()).append(":")
                .append(empregado.getAgencia()).append(":")
                .append(empregado.getContaCorrente()).append(":")
                .append(empregado.getMetodoPagamento()).append(";");
        	}

        }

        escreverArquivo("empregados.txt", empregadosData.toString());
    }
    
    
    
    public static void persistirDados(Map <String, Empregado> empregados) {
        salvarEmpregados(empregados);
    }

    public static void limparArquivos() throws IOException{
    	File file = new File("./database/empregados.txt"); 
    	if(file.delete()){
    	    file.createNewFile();
    	}else{
    	    throw new IOException();
    	}
	  } 	  
}