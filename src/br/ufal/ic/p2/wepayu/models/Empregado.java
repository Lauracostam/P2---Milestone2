package br.ufal.ic.p2.wepayu.models;

import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import br.ufal.ic.p2.wepayu.Exception.*;

public class Empregado {
    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private boolean sindicalizado;
    private String idSindicato;
    private double taxaSindical;
    private String horarioPagamento;
    private int diaPagamento;
    private int mesPagamento;
    private int anoPagamento;
    private int diaDaSemanaPagamento;
    private int primeiroDia;
    private int primeiroMes;
    private int primeiroAno;
    private int primeiroDiaDaSemana;

    
    private Map<String, String> taxaServico = new HashMap<String, String>();

    private String metodoPagamento;
    private String banco;
    private String agencia;
    private String contaCorrente;
    private double pag_bruto = 0;
    private double descontos = 0;
    private double pag_liq = 0;
    private String ultimoPagamento = "1/1/2005";
    private String ultimoPagamento_Desconto = "1/1/2005";


    public Empregado(String id, String nome, String endereco, String tipo, String salario){
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = Double.valueOf(salario.replace(",", "."));
        this.sindicalizado = false;   
        this.idSindicato = "";
        this.taxaSindical = 0.0;
        this.taxaServico = new HashMap<String, String>();
        this.metodoPagamento = "emMaos";
        this.banco = "";
        this.agencia = "";
        this.contaCorrente = "";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) throws NomeNaoPodeSerNuloException{
        if (nome.isEmpty()){
            throw new NomeNaoPodeSerNuloException();
        }
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) throws EnderecoNaoPodeSerNuloException{
        if (endereco.isEmpty()){
            throw new EnderecoNaoPodeSerNuloException();
        }

        this.endereco = endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) throws TipoInvalidoException, TipoNaoAplicavelException{
        if (!tipo.equals("assalariado") && !tipo.equals("comissionado") && !tipo.equals("horista")){
            throw new TipoInvalidoException();
        }
        if (tipo.equals("comissionado") && this.getSalario() == 0){
            throw new TipoNaoAplicavelException();
        }
        this.tipo = tipo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(String salario) throws SalarioDeveSerNaoNegativoException, SalarioNaoPodeSerNuloException, SalarioDeveSerNumericoException{
        if (salario.isEmpty()){
            throw new SalarioNaoPodeSerNuloException();
        }
        try {
            double salarioEmp = Double.parseDouble(salario.replace(",", "."));
            if (salarioEmp < 0){
                throw new SalarioDeveSerNaoNegativoException();
            }
            this.salario = salarioEmp;
        } catch (NumberFormatException e) {
            throw new SalarioDeveSerNumericoException();
        }
    }

	public String getAtributo(String atributo) throws AtributoNaoPreenchidoException, AtributoNaoExisteException, EmpregadoNaoEComissionadoException, EmpregadoNaoRecebeEmBancoException, EmpregadoNaoSindicalizadoException {
        switch (atributo) {
            case "nome" -> {
                return this.getNome();
            }
            case "endereco" -> {
                return this.getEndereco();
            }
            case "tipo" -> {
                return this.getTipo();
            }
            case "salario" -> {
                return String.format("%.2f", this.getSalario());
            }
            case "sindicalizado" -> {
                return String.valueOf(this.getSindicalizado());
            }
            case "comissao" -> throw new EmpregadoNaoEComissionadoException();
            case "idSindicato" -> {
                if (!this.getSindicalizado()) {
                    throw new EmpregadoNaoSindicalizadoException();
                }

                return this.getIdSindicato();
            }
            case "taxaSindical" -> {
                if (!this.getSindicalizado()) {
                    throw new EmpregadoNaoSindicalizadoException();
                }
                return String.format("%.2f", this.getTaxaSindical());
            }
            case "metodoPagamento" -> {
                return this.getMetodoPagamento();
            }
            case "banco" -> {
                if (!this.getMetodoPagamento().equals("banco")) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return this.getBanco();
            }
            case "agencia" -> {
                if (!this.getMetodoPagamento().equals("banco")) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return this.getAgencia();
            }
            case "contaCorrente" -> {
                if (!this.getMetodoPagamento().equals("banco")) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return this.getContaCorrente();
            }
            default -> throw new AtributoNaoExisteException();
        }
    }

	public boolean getSindicalizado() {
		return sindicalizado;
	}

	public void setSindicalizado(boolean sindicalizado) {
		this.sindicalizado = sindicalizado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdSindicato() {
		return idSindicato;
	}

	public void setIdSindicato(String idSindicato) {
		this.idSindicato = idSindicato;
	}

	public double getTaxaSindical() {
		return taxaSindical;
	}

	public void setTaxaSindical(String taxaSindical) throws TaxaSindicalDeveSerNaoNegativaException, TaxaSindicalDeveSerNumericaException {
		try {
            double taxa = Double.valueOf(taxaSindical.replace(",", "."));
            if (taxa < 0){
                throw new TaxaSindicalDeveSerNaoNegativaException();
            }
            this.taxaSindical = taxa;
        } catch (NumberFormatException e) {
            throw new TaxaSindicalDeveSerNumericaException();
        }
	}
	
	public void lancaTaxaServico(String data, String valor){
        this.taxaServico.put(data, valor);
    }
    
    public Map<String, String> getTaxaServico(){
        return taxaServico;
    }
    
    public void setTaxas(Map<String, String> taxaServico) {
		this.taxaServico = taxaServico;
	}
    
    public double getTaxasServico(String dataInicial, String dataFinal) throws ParseException{
		double taxa = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    Date dataAtual = null;
		for (Map.Entry<String,String> itr : this.taxaServico.entrySet()) {
			dataAtual = sdformat.parse(itr.getKey());
			if(dataAtual.compareTo(inicio) >= 0 && dataAtual.compareTo(fim) < 0) {
				taxa += Double.valueOf(itr.getValue().replace(",", "."));
			}
		}
		return taxa;
	}

    public void setMetodoPagamento(String metodoPagamento) throws MetodoPagamentoInvalidoException {
        if (!(metodoPagamento.equals("emMaos") || metodoPagamento.equals("correios") || metodoPagamento.equals("banco"))){
            throw new MetodoPagamentoInvalidoException();
        }
        this.metodoPagamento = metodoPagamento;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setBanco(String banco) throws BancoNaoPodeSerNuloException {
        if (this.metodoPagamento.equals("banco") && banco.isEmpty()){
            throw new BancoNaoPodeSerNuloException();
        }

        this.banco = banco;
    }

    public String getBanco() {
        return banco;
    }

    public void setAgencia(String agencia) throws AgenciaNaoPodeSerNuloException {
        if (this.metodoPagamento.equals("banco") && agencia.isEmpty()){
            throw new AgenciaNaoPodeSerNuloException();
        }
        this.agencia = agencia;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setContaCorrente(String contaCorrente) throws ContaCorrenteNaoPodeSerNuloException {
        if (this.metodoPagamento.equals("banco") && contaCorrente.isEmpty()){
            throw new ContaCorrenteNaoPodeSerNuloException();
        }
        this.contaCorrente = contaCorrente;
    }

    public String getContaCorrente() {
        return contaCorrente;
    }

	public int getDiaPagamento() {
		return diaPagamento;
	}

	public void setDiaPagamento(int diaPagamento) {
		this.diaPagamento = diaPagamento;
	}

	public int getMesPagamento() {
		return mesPagamento;
	}

	public void setMesPagamento(int mesPagamento) {
		this.mesPagamento = mesPagamento;
	}

	public int getAnoPagamento() {
		return anoPagamento;
	}

	public void setAnoPagamento(int anoPagamento) {
		this.anoPagamento = anoPagamento;
	}

	public int getDiaDaSemanaPagamento() {
		return diaDaSemanaPagamento;
	}

	public void setDiaDaSemanaPagamento(int diaDaSemanaPagamento) {
		this.diaDaSemanaPagamento = diaDaSemanaPagamento;
	}

	public int getPrimeiroDia() {
		return primeiroDia;
	}

	public void setPrimeiroDia(int primeiroDia) {
		this.primeiroDia = primeiroDia;
	}

	public int getPrimeiroMes() {
		return primeiroMes;
	}

	public void setPrimeiroMes(int primeiroMes) {
		this.primeiroMes = primeiroMes;
	}

	public int getPrimeiroAno() {
		return primeiroAno;
	}

	public void setPrimeiroAno(int primeiroAno) {
		this.primeiroAno = primeiroAno;
	}

	public int getPrimeiroDiaDaSemana() {
		return primeiroDiaDaSemana;
	}

	public void setPrimeiroDiaDaSemana(int primeiroDiaDaSemana) {
		this.primeiroDiaDaSemana = primeiroDiaDaSemana;
	}

	public String getHorarioPagamento() {
		return horarioPagamento;
	}

	public void setHorarioPagamento(String horarioPagamento) {
		this.horarioPagamento = horarioPagamento;
	}

	public double getPag_bruto() {
		return pag_bruto;
	}

	public void setPag_bruto(double pag_bruto) {
		this.pag_bruto = pag_bruto;
	}

	public double getDescontos() {
		return descontos;
	}

	public void setDescontos(double descontos) {
		this.descontos = descontos;
	}

	public double getPag_liq() {
		return pag_liq;
	}

	public void setPag_liq(double pag_liq) {
		this.pag_liq = pag_liq;
	}

	public String getUltimoPagamento() {
		return ultimoPagamento;
	}

	public void setUltimoPagamento(String ultimoPagamento) {
		this.ultimoPagamento = ultimoPagamento;
	}

	public String getUltimoPagamento_Desconto() {
		return ultimoPagamento_Desconto;
	}

	public void setUltimoPagamento_Desconto(String ultimoPagamento_Desconto) {
		this.ultimoPagamento_Desconto = ultimoPagamento_Desconto;
	}

}
