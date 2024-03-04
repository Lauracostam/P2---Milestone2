package br.ufal.ic.p2.wepayu;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.ufal.ic.p2.wepayu.Exception.*;
import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.FolhaDePagamento;
import br.ufal.ic.p2.wepayu.models.Horista;
import br.ufal.ic.p2.wepayu.utils.UtilsFileReader;
import br.ufal.ic.p2.wepayu.utils.UtilsFileWriter;

public class Sistema {
	private Map<String, Empregado> empregados = new HashMap<String, Empregado>();
 
	public Sistema() throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, TaxaSindicalDeveSerNumericaException, TaxaSindicalDeveSerNaoNegativaException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
		UtilsFileWriter.criarPasta();
		UtilsFileReader.lerArquivos(this);
	}
	
	public void zerarSistema() throws IOException {
        this.empregados = new HashMap<String, Empregado>();

        UtilsFileWriter.limparArquivos();
    }
	
	public void encerrarSistema() {
		UtilsFileWriter.criarPasta();
		UtilsFileWriter.persistirDados(this.empregados);
	}
	
	public Empregado getEmpregado(String emp_) throws EmpregadoNaoExisteException {
		if(empregados.containsKey(emp_)){
			return empregados.get(emp_);
		}
        else {
            throw new EmpregadoNaoExisteException();
        }
    }
	
	public String setEmpregado(String id, String nome, String  endereco, String tipo, String salario) throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException{
		if (nome.isEmpty()){
			throw new NomeNaoPodeSerNuloException();
		}
		if (endereco.equals("")){
			throw new EnderecoNaoPodeSerNuloException();
		}
		if(tipo.equals("comissionado")){;
			throw new TipoNaoAplicavelException();
		}
		else if(!tipo.equals("horista") && !tipo.equals("assalariado")){
			throw new TipoInvalidoException();
		}
		if(salario.equals("")) {
			throw new SalarioNaoPodeSerNuloException();
		}
		else if(salario.charAt(0) == '-') {
			throw new SalarioDeveSerNaoNegativoException();
		}
		else if(!Character.isDigit(salario.charAt(0))) {
			throw new SalarioDeveSerNumericoException();
		}
		if(tipo.equals("horista")) {
			if(empregados.size() > 0) {
				Map.Entry<String,Empregado> lastEntry = null;
				lastEntry = empregados.entrySet().stream().reduce((first,second) -> second).get();
				id = lastEntry.getKey();
				int id_int = Integer.valueOf(id);
				id_int+=1;
				id = Integer.toString(id_int);
			}
			Horista empregado = new Horista(id, nome, endereco, tipo, salario);
			this.empregados.put(id, empregado);
		}
		else if(tipo.equals("assalariado")){
			if(empregados.size() > 0) {
				Map.Entry<String,Empregado> lastEntry = null;
				lastEntry = empregados.entrySet().stream().reduce((first,second) -> second).get();
				id = lastEntry.getKey();
				int id_int = Integer.valueOf(id);
				id_int+=1;
				id = Integer.toString(id_int);
			}
			Empregado empregado = new Empregado(id, nome, endereco, tipo, salario);
			this.empregados.put(id, empregado);
		}
		return id;

    }
	public String setEmpregado(String id, String nome, String  endereco, String tipo, String salario, String comissao) throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException {
		if (nome.equals("")){
			throw new NomeNaoPodeSerNuloException();
		}
		if (endereco.equals("")){
			throw new EnderecoNaoPodeSerNuloException();
		}
		if(tipo.equals("horista") || tipo.equals("assalariado")){
			throw new TipoNaoAplicavelException();
		}
		else if(!tipo.equals("comissionado")){
			throw new TipoInvalidoException();
		}
		if(comissao.equals("")) {
			throw new ComissaoNaoPodeSerNulaException();
		}
		else if(comissao.charAt(0) == '-') {
			throw new ComissaoDeveSerNaoNegativaException();
		}
		else if(!Character.isDigit(comissao.charAt(0))){
			throw new ComissaoDeveSerNumericaException();
		}
		if(salario.equals("")) {
			throw new SalarioNaoPodeSerNuloException();
		}
		else if(salario.charAt(0) == '-') {
			throw new SalarioDeveSerNaoNegativoException();
		}
		else if(!Character.isDigit(salario.charAt(0))){
			throw new SalarioDeveSerNumericoException();
		}
		else if (tipo.equals("comissionado")){
			if(empregados.size() > 0) {
				Map.Entry<String,Empregado> lastEntry = null;
				lastEntry = empregados.entrySet().stream().reduce((first,second) -> second).get();
				id = lastEntry.getKey();
				int id_int = Integer.valueOf(id);
				id_int+=1;
				id = Integer.toString(id_int);
			}
			Comissionado empregado = new Comissionado(id, nome, endereco, tipo, salario, comissao);
			this.empregados.put(id, empregado);
		}
		return id;
    }
    
    public String getAtributo(String emp_, String atributo)throws EmpregadoNaoExisteException, AtributoNaoExisteException, AtributoNaoPreenchidoException, IdentificacaoException, EmpregadoNaoEComissionadoException, EmpregadoNaoRecebeEmBancoException, EmpregadoNaoSindicalizadoException {
    	if(emp_.isEmpty()){
			throw new IdentificacaoException();
		}
    	Empregado empregado = getEmpregado(emp_);

		if (atributo.equals("comissao")) {
			if(empregado instanceof Comissionado comissionado) {
                return String.format("%.2f", comissionado.getComissao());
			}
			else {
				throw new EmpregadoNaoEComissionadoException();
			}
		}

    	return empregado.getAtributo(atributo);
	}
	
	public String getEmpregadoPorNome(String nome, int indice)throws NaoHaEmpregadoComEsseNomeException{
		ArrayList<Empregado> empregados_com_nome = new ArrayList<Empregado>();
		boolean found = false;
		for (Empregado itemdalista : empregados.values()) {
            if (itemdalista.getNome().equals(nome)) {
            	empregados_com_nome.add(itemdalista);
            	found = true;
            }
        }
        if (!found) {
            throw new NaoHaEmpregadoComEsseNomeException();
        }
        return empregados_com_nome.get(indice-1).getId();
	}
	
	public void removerEmpregado(String emp_) throws EmpregadoNaoExisteException, IdentificacaoException{
		String chaveRemover = emp_;

        if (empregados.containsKey(chaveRemover)) {
            empregados.remove(chaveRemover);
        }
        else if (emp_.equals("")){
        	throw new IdentificacaoException();
        }
        else {
            throw new EmpregadoNaoExisteException();
        }

	}
	public void empHorista(String emp_, String data, String horas) throws IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, HorasDevemSerPositivasException, DataInvalidaException {
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		if(horas.charAt(0) == '-' || horas.equals("0")) {
			throw new HorasDevemSerPositivasException();
		}
		String[] datas_atuais = data.split("/");
		if (Integer.valueOf(datas_atuais[0]) > 31 || Integer.valueOf(datas_atuais[1]) > 12 || (Integer.valueOf(datas_atuais[1]) == 2 && Integer.valueOf(datas_atuais[0]) > 29)) {
			throw new DataInvalidaException();
		}
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(empregado instanceof Horista) {
				Horista horista = (Horista)empregado;
				horista.lancaCartao(data, horas);
			}
			else {
				throw new EmpregadoNaoEHoristaException();
			}
		}
		else {
			throw new EmpregadoNaoExisteException();
		}
		
	}
	
	public String getHorasExtras(String emp_, String dataInicial, String dataFinal) throws ParseException, IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException {
		double horas = 0;
		String horas_extras = null;
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		String[] datas_atuais_iniciais = dataInicial.split("/");
		if (Integer.valueOf(datas_atuais_iniciais[0]) > 31 || Integer.valueOf(datas_atuais_iniciais[1]) > 12 || (Integer.valueOf(datas_atuais_iniciais[1]) == 2 && Integer.valueOf(datas_atuais_iniciais[0]) > 29)) {
			throw new DataInicialInvalidaException();
		}
		String[] datas_atuais_finais = dataFinal.split("/");
		if (Integer.valueOf(datas_atuais_finais[0]) > 31 || Integer.valueOf(datas_atuais_finais[1]) > 12 || (Integer.valueOf(datas_atuais_finais[1]) == 2 && Integer.valueOf(datas_atuais_finais[0]) > 29)) {
			throw new DataFinalInvalidaException();
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    if(inicio.compareTo(fim) > 0) {
	    	throw new CompararDatasException();
	    }
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(empregado instanceof Horista) {
				Horista horista = (Horista)empregado;
				horas = horista.getHorasExtras(dataInicial, dataFinal);
				
			}
			else {
				throw new EmpregadoNaoEHoristaException();
			}
		}
		else {
			throw new EmpregadoNaoExisteException();
		}
		horas_extras = String.format("%.1f", horas);
		String[] formato_de_horas = horas_extras.split(",");
		int size = formato_de_horas.length;
		if(formato_de_horas[size-1].equals("0")) {
			horas_extras = formato_de_horas[0];
		}
		return horas_extras;
	}
	public String getHorasNormais(String emp_, String dataInicial, String dataFinal) throws ParseException, EmpregadoNaoEHoristaException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException{
		double horas = 0;
		String horas_trabalhadas = null;
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		String[] datas_atuais_iniciais = dataInicial.split("/");
		if (Integer.valueOf(datas_atuais_iniciais[0]) > 31 || Integer.valueOf(datas_atuais_iniciais[1]) > 12 || (Integer.valueOf(datas_atuais_iniciais[1]) == 2 && Integer.valueOf(datas_atuais_iniciais[0]) > 29)) {
			throw new DataInicialInvalidaException();
		}
		String[] datas_atuais_finais = dataFinal.split("/");
		if (Integer.valueOf(datas_atuais_finais[0]) > 31 || Integer.valueOf(datas_atuais_finais[1]) > 12 || (Integer.valueOf(datas_atuais_finais[1]) == 2 && Integer.valueOf(datas_atuais_finais[0]) > 29)) {
			throw new DataFinalInvalidaException();
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    if(inicio.compareTo(fim) > 0) {
	    	throw new CompararDatasException();
	    }
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(empregado instanceof Horista) {
				Horista horista = (Horista)empregado;
				horas = horista.getHorasNormais(dataInicial, dataFinal);
				
			}
			else {
				throw new EmpregadoNaoEHoristaException();
			}
		}
		horas_trabalhadas = String.format("%.1f", horas);
		String[] formato_de_horas = horas_trabalhadas.split(",");
		int size = formato_de_horas.length;
		if(formato_de_horas[size-1].equals("0")) {
			horas_trabalhadas = formato_de_horas[0];
		}
		return horas_trabalhadas;
	}
	
	
	public void empComissionado(String emp_, String data, String valor) throws IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, HorasDevemSerPositivasException, DataInvalidaException, ValorDeveSerPositivoException, EmpregadoNaoEComissionadoException {
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		if(valor.charAt(0) == '-' || valor.equals("0")) {
			throw new ValorDeveSerPositivoException();
		}
		String[] datas_atuais = data.split("/");
		if (Integer.valueOf(datas_atuais[0]) > 31 || Integer.valueOf(datas_atuais[1]) > 12 || (Integer.valueOf(datas_atuais[1]) == 2 && Integer.valueOf(datas_atuais[0]) > 29)) {
			throw new DataInvalidaException();
		}
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(empregado instanceof Comissionado) {
				Comissionado comissionado = (Comissionado)empregado;
				comissionado.lancaVenda(data, valor);
			}
			else {
				throw new EmpregadoNaoEComissionadoException();
			}
		}
		else {
			throw new EmpregadoNaoExisteException();
		}
		
	}

	public void validaEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoException{
		if(emp.isEmpty()){
			throw new IdentificacaoException();
		}
		if(!empregados.containsKey(emp)){
			throw new EmpregadoNaoExisteException();
		}
	}
	
	public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws HaOutroEmpregadoComEsteIDSindicatoException, TaxaSindicalNaoPodeSerNulaException, EmpregadoNaoExisteException, IdentificacaoException, ValorDeveSerTrueOuFalseException, IdentificacaoDoSindicatoNaoPodeSerNulaException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
   		validaEmpregado(emp);
		if(empregados.containsKey(emp)) {
			Empregado empregado = empregados.get(emp);
			if(atributo.equals("sindicalizado")){
				if (!(valor.equals("true") || valor.equals("false"))){
					throw new ValorDeveSerTrueOuFalseException();
				}

				if (valor.equals("true")){
                    if (idSindicato.isEmpty()){
                        throw new IdentificacaoDoSindicatoNaoPodeSerNulaException();
                    }

                    if (taxaSindical.isEmpty()){
                        throw new TaxaSindicalNaoPodeSerNulaException();
                    }
                }

				for (Empregado empregado_existente : empregados.values()) {
					if(idSindicato.equals(empregado_existente.getIdSindicato())){
						throw new HaOutroEmpregadoComEsteIDSindicatoException();
					}
				}
				if(valor.equals("true")){
					validaTaxaSindical(taxaSindical);
					validaIdSindicato(idSindicato);
					if(taxaSindical.equals("0.0")){
						throw new TaxaSindicalNaoPodeSerNulaException();
					}

					empregado.setSindicalizado(Boolean.valueOf(valor));
					empregado.setIdSindicato(idSindicato);
					empregado.setTaxaSindical(taxaSindical);
					
				}
				else {
					empregado.setSindicalizado(Boolean.valueOf(valor));
				}
			}
		}
	}

	public void validaTaxaSindical(String taxaSindical) throws TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
		try {
			double taxa = Double.parseDouble(taxaSindical.replace(",", "."));
			if (taxa < 0){
				throw new TaxaSindicalDeveSerNaoNegativaException();
			}
		} catch (NumberFormatException e) {
			throw new TaxaSindicalDeveSerNumericaException();
		}
	}

	public void validaIdSindicato(String idSindicato) throws IdentificacaoDoSindicatoNaoPodeSerNulaException {
		if (idSindicato.isEmpty()){
			throw new IdentificacaoDoSindicatoNaoPodeSerNulaException();
		}
	}

	public void alteraEmpregado(String emp, String atributo, String valor, String banco, String agencia, String contaCorrente) throws EmpregadoNaoExisteException, IdentificacaoException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
		validaEmpregado(emp);
		if (empregados.containsKey(emp)) {
			Empregado empregado = empregados.get(emp);
			if (atributo.equals("metodoPagamento")) {
				if(banco.equals("")) {
					throw new BancoNaoPodeSerNuloException();
				}
				if(agencia.equals("")) {
					throw new AgenciaNaoPodeSerNuloException();
				}
				if(contaCorrente.equals("")) {
					throw new ContaCorrenteNaoPodeSerNuloException();
				}
				empregado.setMetodoPagamento(valor);
				empregado.setBanco(banco);
				empregado.setAgencia(agencia);
				empregado.setContaCorrente(contaCorrente);
			}
		}
	}

	public void alteraEmpregado(String emp, String atributo, String valor, String comissao) throws ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException, SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, TipoNaoAplicavelException, TipoInvalidoException, EmpregadoNaoExisteException, IdentificacaoException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
   		validaEmpregado(emp);
		if(empregados.containsKey(emp)) {
			Empregado empregado = empregados.get(emp);
			if(atributo.equals("tipo")){
				if (valor.equals("comissionado")){
					if (comissao.isEmpty()){
						throw new ComissaoNaoPodeSerNulaException();
					}
					else if (!comissao.matches("^[0-9]+(,[0-9]{1,2})?$")){
						throw new ComissaoDeveSerNumericaException();
					}
					double comissaoEmp = Double.parseDouble(comissao.replace(",", "."));

					if (comissaoEmp < 0){
						throw new ComissaoDeveSerNaoNegativaException();
					}

					Comissionado comissionado = getComissionado(valor, comissao, empregado);
					empregados.put(emp, comissionado);
				}
				else if (valor.equals("horista")){
					if (empregado instanceof Horista horista) {
						horista.setTipo(valor);
					}
					else {
						Horista horista = new Horista(
								empregado.getId(),
								empregado.getNome(),
								empregado.getEndereco(),
								valor,
								comissao
						);
						horista.setMetodoPagamento(empregado.getMetodoPagamento());
						horista.setBanco(empregado.getBanco());
						horista.setAgencia(empregado.getAgencia());
						horista.setContaCorrente(empregado.getContaCorrente());
						horista.setSindicalizado(empregado.getSindicalizado());
						if (empregado.getSindicalizado()){
							horista.setIdSindicato(empregado.getIdSindicato());
							horista.setTaxaSindical(String.valueOf(empregado.getTaxaSindical()));
						}
						empregados.put(emp, horista);
					}
				}
			}
		}
	}

	private static Comissionado getComissionado(String valor, String comissao, Empregado empregado) throws MetodoPagamentoInvalidoException, ContaCorrenteNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, BancoNaoPodeSerNuloException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
		Comissionado comissionado = new Comissionado(
				empregado.getId(),
				empregado.getNome(),
				empregado.getEndereco(),
				valor,
				String.valueOf(empregado.getSalario()),
				comissao
		);
		comissionado.setMetodoPagamento(empregado.getMetodoPagamento());
		comissionado.setBanco(empregado.getBanco());
		comissionado.setAgencia(empregado.getAgencia());
		comissionado.setContaCorrente(empregado.getContaCorrente());
		comissionado.setSindicalizado(empregado.getSindicalizado());
		if (empregado.getSindicalizado()){
			comissionado.setIdSindicato(empregado.getIdSindicato());
			comissionado.setTaxaSindical(String.valueOf(empregado.getTaxaSindical())); 
		}
		return comissionado;
	}


	public void alteraEmpregado(String emp, String atributo, String valor) throws ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException, EmpregadoNaoEComissionadoException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, IdentificacaoException, AtributoNaoExisteException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException, ValorDeveSerTrueOuFalseException {
   		validaEmpregado(emp);
		if(empregados.containsKey(emp)) {
			Empregado empregado = empregados.get(emp);
			if(atributo.equals("sindicalizado")){
				if (!(valor.equals("true") || valor.equals("false"))){
					throw new ValorDeveSerTrueOuFalseException();
				}

				empregado.setSindicalizado(Boolean.valueOf(valor));
			}
			else if(atributo.equals(("comissao"))){
				if (empregado instanceof Comissionado comissionado) {
					comissionado.setComissao(valor);
				}
				else {
					throw new EmpregadoNaoEComissionadoException();
				}
			}
			else if (atributo.equals("nome")) {
				empregado.setNome(valor);
			}
			else if (atributo.equals("endereco")) {
				empregado.setEndereco(valor);
			}
			else if (atributo.equals("salario")) {
				empregado.setSalario(valor);
			}
			else if (atributo.equals("tipo")) {
				empregado.setTipo(valor);
			}
			else if (atributo.equals("metodoPagamento")){
				empregado.setMetodoPagamento(valor);
				if (!valor.equals("banco")){
					empregado.setBanco("");
					empregado.setAgencia("");
					empregado.setContaCorrente("");
				}			
			}
			else {
				throw new AtributoNaoExisteException();
			}

		}
	}
	
	
	public String getVendasRealizadas(String emp_, String dataInicial, String dataFinal) throws ParseException, EmpregadoNaoEComissionadoException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException{
		double valor = 0;
		String vendas_realizadas = null;
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		String[] datas_atuais_iniciais = dataInicial.split("/");
		if (Integer.valueOf(datas_atuais_iniciais[0]) > 31 || Integer.valueOf(datas_atuais_iniciais[1]) > 12 || (Integer.valueOf(datas_atuais_iniciais[1]) == 2 && Integer.valueOf(datas_atuais_iniciais[0]) > 29)) {
			throw new DataInicialInvalidaException();
		}
		String[] datas_atuais_finais = dataFinal.split("/");
		if (Integer.valueOf(datas_atuais_finais[0]) > 31 || Integer.valueOf(datas_atuais_finais[1]) > 12 || (Integer.valueOf(datas_atuais_finais[1]) == 2 && Integer.valueOf(datas_atuais_finais[0]) > 29)) {
			throw new DataFinalInvalidaException();
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    if(inicio.compareTo(fim) > 0) {
	    	throw new CompararDatasException();
	    }
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(empregado instanceof Comissionado) {
				Comissionado comissionado = (Comissionado)empregado;
				valor = comissionado.getVendasRealizadas(dataInicial, dataFinal);
				
			}
			else {
				throw new EmpregadoNaoEComissionadoException();
			}
		}
		vendas_realizadas = String.format("%.2f", valor);
		return vendas_realizadas;
	}

	
	public void lancaTaxaServico(String membro, String data, String valor) throws IdentificacaoException, ValorDeveSerPositivoException, DataInvalidaException, EmpregadoNaoExisteException, IdentificacaoMembroException, MembroNaoExisteException{
		if(membro.equals("")){
			throw new IdentificacaoMembroException();
		}
		Empregado empregado_atual = null;		
		for (Empregado empregado: empregados.values()) {
			if(empregado.getIdSindicato().equals(membro)) {
				empregado_atual = empregado;
				break;
			}
		}
		if(valor.charAt(0) == '-' || valor.equals("0")) {
			throw new ValorDeveSerPositivoException();
		}
		String[] datas_atuais = data.split("/");
		if (Integer.valueOf(datas_atuais[0]) > 31 || Integer.valueOf(datas_atuais[1]) > 12 || (Integer.valueOf(datas_atuais[1]) == 2 && Integer.valueOf(datas_atuais[0]) > 29)) {
			throw new DataInvalidaException();
		}
		if(empregado_atual != null) {
			empregado_atual.lancaTaxaServico(data, valor);
		}
		else {
			throw new MembroNaoExisteException();
		}
	}
	
	public String getTaxaServico(String emp_, String dataInicial, String dataFinal) throws ParseException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException, NaoESindicalizadoException{
		double valor = 0;
		String taxas = null;
		if(emp_.equals("")){
			throw new IdentificacaoException();
		}
		String[] datas_atuais_iniciais = dataInicial.split("/");
		if (Integer.valueOf(datas_atuais_iniciais[0]) > 31 || Integer.valueOf(datas_atuais_iniciais[1]) > 12 || (Integer.valueOf(datas_atuais_iniciais[1]) == 2 && Integer.valueOf(datas_atuais_iniciais[0]) > 29)) {
			throw new DataInicialInvalidaException();
		}
		String[] datas_atuais_finais = dataFinal.split("/");
		if (Integer.valueOf(datas_atuais_finais[0]) > 31 || Integer.valueOf(datas_atuais_finais[1]) > 12 || (Integer.valueOf(datas_atuais_finais[1]) == 2 && Integer.valueOf(datas_atuais_finais[0]) > 29)) {
			throw new DataFinalInvalidaException();
		}
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    if(inicio.compareTo(fim) > 0) {
	    	throw new CompararDatasException();
	    }
		if(empregados.containsKey(emp_)) {
			Empregado empregado = empregados.get(emp_);
			if(!empregado.getSindicalizado()) {
				throw new NaoESindicalizadoException();
			}
			valor = empregado.getTaxasServico(dataInicial, dataFinal);
		}
		taxas = String.format("%.2f", valor);
		return taxas;
	}
	
	
	public String calcularTotalFolha(String dataPagamento) throws ParseException {
//		System.out.println(dataPagamento);
        double totalFolha = 0;
        String totalFolhaStr = null;
        for (Empregado empregado : empregados.values()) {
        	double pagamento = FolhaDePagamento.calcularPagamento(dataPagamento, empregado);
            totalFolha += pagamento;
//            System.out.println("emp = " + empregado.getNome());
//            System.out.println("folha" + " " + pagamento);
//            System.out.println("tipo = " + empregado.getTipo());
        }
        totalFolhaStr = String.format("%.2f",totalFolha);
        return totalFolhaStr;
    }
	
	
	
	
}