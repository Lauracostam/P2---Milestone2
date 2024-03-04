package br.ufal.ic.p2.wepayu;
 
import java.io.IOException;
import java.text.ParseException;

import br.ufal.ic.p2.wepayu.Exception.*;
	

public class Facade {
	private final Sistema sistema;
 
	public Facade() throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, EmpregadoNaoExisteException, TaxaSindicalDeveSerNumericaException, TaxaSindicalDeveSerNaoNegativaException, MetodoPagamentoInvalidoException, BancoNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, ContaCorrenteNaoPodeSerNuloException {
		this.sistema = new Sistema();
	}
 
	public String criarEmpregado(String nome, String endereco, String tipo, String salario)throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException  {
		return this.sistema.setEmpregado("1", nome, endereco, tipo, salario);
    }
	
	public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)throws NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, SalarioNaoPodeSerNuloException, SalarioDeveSerNaoNegativoException, SalarioDeveSerNumericoException {
		return this.sistema.setEmpregado("1", nome, endereco, tipo, salario, comissao);
    }
 
	public String getAtributoEmpregado(String emp, String atributo) throws EmpregadoNaoExisteException, AtributoNaoExisteException, AtributoNaoPreenchidoException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException, ComissaoDeveSerNaoNegativaException, IdentificacaoException, EmpregadoNaoEComissionadoException, EmpregadoNaoRecebeEmBancoException, EmpregadoNaoSindicalizadoException  {
        return this.sistema.getAtributo(emp, atributo);
    }
    public String getEmpregadoPorNome(String nome, int indice) throws NaoHaEmpregadoComEsseNomeException{
		return this.sistema.getEmpregadoPorNome(nome, indice);
	}
	
    public void removerEmpregado(String emp) throws EmpregadoNaoExisteException, IdentificacaoException {
    	this.sistema.removerEmpregado(emp);
    }
    
    public void lancaCartao(String emp, String data, String horas) throws IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, HorasDevemSerPositivasException, DataInvalidaException {
		this.sistema.empHorista(emp, data, horas);
	}
    
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal) throws ParseException, IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException {
    	return this.sistema.getHorasExtras(emp, dataInicial, dataFinal);
    }
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws ParseException, EmpregadoNaoEHoristaException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException{
    	return this.sistema.getHorasNormais(emp, dataInicial, dataFinal);
    }
    
    public void lancaVenda(String emp, String data, String valor) throws IdentificacaoException, EmpregadoNaoExisteException, EmpregadoNaoEHoristaException, HorasDevemSerPositivasException, DataInvalidaException, ValorDeveSerPositivoException, EmpregadoNaoEComissionadoException {
    	this.sistema.empComissionado(emp, data, valor);
    }
    
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws ParseException, EmpregadoNaoEHoristaException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException, EmpregadoNaoEComissionadoException{
    	return this.sistema.getVendasRealizadas(emp, dataInicial, dataFinal);
    }
    
    public void alteraEmpregado(String emp, String atributo, String valor, String idSindicato, String taxaSindical) throws HaOutroEmpregadoComEsteIDSindicatoException, TaxaSindicalNaoPodeSerNulaException, EmpregadoNaoExisteException, IdentificacaoException, ValorDeveSerTrueOuFalseException, IdentificacaoDoSindicatoNaoPodeSerNulaException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
		this.sistema.alteraEmpregado(emp, atributo, valor, idSindicato, taxaSindical);
	}

	public void alteraEmpregado(String emp, String atributo, String valor, String banco, String agencia, String contaCorrente) throws EmpregadoNaoRecebeEmBancoException, EmpregadoNaoExisteException, IdentificacaoException, MetodoPagamentoInvalidoException, ContaCorrenteNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, BancoNaoPodeSerNuloException {
		this.sistema.alteraEmpregado(emp, atributo, valor, banco, agencia, contaCorrente);
	}

	public void alteraEmpregado(String emp, String atributo, String valor, String comissao) throws ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException, SalarioDeveSerNumericoException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, TipoNaoAplicavelException, TipoInvalidoException, EmpregadoNaoExisteException, IdentificacaoException, MetodoPagamentoInvalidoException, ContaCorrenteNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, BancoNaoPodeSerNuloException, TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
		this.sistema.alteraEmpregado(emp, atributo, valor, comissao);
	}
    
    public void alteraEmpregado(String emp, String atributo, String valor) throws ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException, EmpregadoNaoEComissionadoException, NomeNaoPodeSerNuloException, EnderecoNaoPodeSerNuloException, TipoInvalidoException, TipoNaoAplicavelException, SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException, EmpregadoNaoExisteException, IdentificacaoException, AtributoNaoExisteException, MetodoPagamentoInvalidoException, ContaCorrenteNaoPodeSerNuloException, AgenciaNaoPodeSerNuloException, BancoNaoPodeSerNuloException, ValorDeveSerTrueOuFalseException {
    	this.sistema.alteraEmpregado(emp, atributo, valor);
    }
    
    public void lancaTaxaServico(String membro, String data, String valor) throws IdentificacaoException, ValorDeveSerPositivoException, DataInvalidaException, EmpregadoNaoExisteException, IdentificacaoMembroException, MembroNaoExisteException {
    	this.sistema.lancaTaxaServico(membro, data, valor);
    }
    
    public String getTaxasServico(String emp, String dataInicial, String dataFinal) throws ParseException, IdentificacaoException, DataInicialInvalidaException, DataFinalInvalidaException, CompararDatasException, NaoESindicalizadoException {
    	return this.sistema.getTaxaServico(emp, dataInicial, dataFinal);
    }
    
    public String totalFolha(String data) throws ParseException {
    	return this.sistema.calcularTotalFolha(data);
    }
    
    public void rodaFolha(String data, String saida) throws ParseException {
    	this.sistema.rodaFolha(data, saida);
    }
    
	public void zerarSistema() throws IOException {
		this.sistema.zerarSistema();
	}
	
	public void encerrarSistema() {
		this.sistema.encerrarSistema();
	}
	
}
