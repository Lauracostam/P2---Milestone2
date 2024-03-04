package br.ufal.ic.p2.wepayu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.wepayu.Sistema;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.Exception.*;

public class UtilsFileReader {
	public static void lerArquivos(Sistema sistema) throws TaxaSindicalDeveSerNumericaException, TaxaSindicalDeveSerNaoNegativaException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
		
        lerArquivo("empregados", sistema);
	}
	public static void lerArquivo(String arquivo, Sistema sistema) throws TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
        File file = new File("./database/" + arquivo + ".txt");

        if (!file.exists()) return;

        String[] dados;
        String linha;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
             while ((linha = br.readLine()) != null) {
                dados = linha.split(";");

                if(arquivo.equals("empregados")) lerEmpregados(sistema, dados);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo);
        }
    }
	
	private static void lerEmpregados(Sistema sistema, String[] dados) throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
        String[] dados_atuais = null;
        for (String dado : dados){
			dados_atuais = dado.split(":");
			String id = dados_atuais[0];
			String nome = dados_atuais[1];
	        String endereco = dados_atuais[2];
	        String tipo = dados_atuais[3];
	        String salario = dados_atuais[4];
	        String sindicalizado = dados_atuais[5];
	        String idSind = dados_atuais[6];
	        String txSind = dados_atuais[7];
	        
	        String banco = dados_atuais[9];
	        String agencia = dados_atuais[10];
	        String conta = dados_atuais[11];
	        String metodopag = dados_atuais[12];

	        if (tipo.equals("horista")){
	        	Map<String, String> taxas = new HashMap<String, String>();
	        	dados_atuais[8] = dados_atuais[8].replace("{", "");
	        	dados_atuais[8] = dados_atuais[8].replace("}", "");
	        	String[] taxas_lista = dados_atuais[8].split("@");
	        	taxas_lista = Arrays.stream(taxas_lista)
	                     .filter(s -> (s != null && s.length() > 0))
	                     .toArray(String[]::new); 
	        	if(taxas_lista.length > 0) {
	        		String[] item = null;
		        	for (String dia : taxas_lista) {
		        		item = dia.split("-");
		        		taxas.put(item[0], item[1]);
		        	}
					sistema.setEmpregado(id, nome, endereco, tipo, salario);
					Empregado empregado = sistema.getEmpregado(id);
					empregado.setTaxas(taxas);
	        	}
	        	else {
	        		sistema.setEmpregado(id, nome, endereco, tipo, salario);
	        	}
	        	Map<String, String> cartao = new HashMap<String, String>();
	        	dados_atuais[13] = dados_atuais[13].replace("{", "");
	        	dados_atuais[13] = dados_atuais[13].replace("}", "");
	        	String[] cartao_lista = dados_atuais[13].split("@");
	        	cartao_lista = Arrays.stream(cartao_lista)
	                     .filter(s -> (s != null && s.length() > 0))
	                     .toArray(String[]::new); 
	        	if(cartao_lista.length > 0) {
	        		String[] item = null;
		        	for (String dia : cartao_lista) {
		        		item = dia.split("-");
		        		cartao.put(item[0], item[1]);
		        	}
					Empregado empregado = sistema.getEmpregado(id);
					Horista horista = (Horista)empregado;
					horista.setCartao(cartao);
	        	}
	        	
			}
	        else if (tipo.equals("comissionado")){
	        	String comissao = dados_atuais[13];
	        	Map<String, String> taxas = new HashMap<String, String>();
	        	dados_atuais[8] = dados_atuais[8].replace("{", "");
	        	dados_atuais[8] = dados_atuais[8].replace("}", "");
	        	String[] taxas_lista = dados_atuais[8].split("@");
	        	taxas_lista = Arrays.stream(taxas_lista)
	                     .filter(s -> (s != null && s.length() > 0))
	                     .toArray(String[]::new); 
	        	if(taxas_lista.length > 0) {
	        		String[] item = null;
		        	for (String dia : taxas_lista) {
		        		item = dia.split("-");
		        		taxas.put(item[0], item[1]);
		        	}
					sistema.setEmpregado(id, nome, endereco, tipo, salario, comissao);
					Empregado empregado = sistema.getEmpregado(id);
					empregado.setTaxas(taxas);
	        	}
	        	else {
	        		sistema.setEmpregado(id, nome, endereco, tipo, salario, comissao);
	        	}
	        	Map<String, String> vendas = new HashMap<String, String>();
	        	dados_atuais[14] = dados_atuais[14].replace("{", "");
	        	dados_atuais[14] = dados_atuais[14].replace("}", "");
	        	String[] vendas_lista = dados_atuais[14].split("@");
	        	vendas_lista = Arrays.stream(vendas_lista)
	                     .filter(s -> (s != null && s.length() > 0))
	                     .toArray(String[]::new); 
	        	if(vendas_lista.length > 0) {
	        		String[] item = null;
		        	for (String dia : vendas_lista) {
		        		item = dia.split("-");
		        		vendas.put(item[0], item[1]);
		        	}
					Empregado empregado = sistema.getEmpregado(id);
					Comissionado comissionado = (Comissionado)empregado;
					comissionado.setVendas(vendas);
	        	}
			}
	        else{
	        	Map<String, String> taxas = new HashMap<String, String>();
	        	dados_atuais[8] = dados_atuais[8].replace("{", "");
	        	dados_atuais[8] = dados_atuais[8].replace("}", "");
	        	String[] taxas_lista = dados_atuais[8].split("@");
	        	taxas_lista = Arrays.stream(taxas_lista)
	                     .filter(s -> (s != null && s.length() > 0))
	                     .toArray(String[]::new); 
	        	if(taxas_lista.length > 0) {
	        		String[] item = null;
		        	for (String dia : taxas_lista) {
		        		item = dia.split("-");
		        		taxas.put(item[0], item[1]);
		        	}
					sistema.setEmpregado(id, nome, endereco, tipo, salario);
					Empregado empregado = sistema.getEmpregado(id);
					empregado.setTaxas(taxas);
	        	}
	        	else {
	        		sistema.setEmpregado(id, nome, endereco, tipo, salario);
	        	}
			}
	        Empregado empregado = sistema.getEmpregado(id);
			empregado.setSindicalizado(Boolean.valueOf(sindicalizado));
			empregado.setIdSindicato(idSind);
			empregado.setTaxaSindical(txSind);
			if(!conta.equals("")) {
				empregado.setContaCorrente(conta);
			}
			if(!banco.equals("")) {
				empregado.setBanco(banco);
			}
			if(!metodopag.equals("")) {
				empregado.setMetodoPagamento(metodopag);
			}
			if(!agencia.equals("")) {
				empregado.setAgencia(agencia);
			}
		}
    }

}
