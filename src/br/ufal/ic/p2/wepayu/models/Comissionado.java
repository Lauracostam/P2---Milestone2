package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.*;

import java.util.HashMap;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Comissionado extends Empregado{
	private double comissao;
	private Map<String, String> vendas = new HashMap<String, String>();
	private double pag_fixo = 0;
	private double pag_vendas = 0;
	private double total_vendas = 0;

	public Comissionado(String id, String nome, String endereco, String tipo, String salario, String comissao) {
		super(id, nome, endereco, tipo, salario);
		this.comissao = Double.valueOf(comissao.replace(",", "."));
		this.vendas = new HashMap<String, String>();
	}
	
	public double getComissao() {
		return comissao;
	}

	public void setComissao(String comissao) throws ComissaoDeveSerNaoNegativaException, ComissaoDeveSerNumericaException, ComissaoNaoPodeSerNulaException{
		if (comissao.isEmpty()){
			throw new ComissaoNaoPodeSerNulaException();
		}

		try {
			double comissaoEmp = Double.parseDouble(comissao.replace(",", "."));
			if (comissaoEmp < 0){
				throw new ComissaoDeveSerNaoNegativaException();
			}

			this.comissao = comissaoEmp;
		} catch (NumberFormatException e) {
			throw new ComissaoDeveSerNumericaException();
		}
	}
	
	public String getAtributo(String atributo) throws AtributoNaoPreenchidoException, AtributoNaoExisteException, EmpregadoNaoSindicalizadoException, EmpregadoNaoRecebeEmBancoException {
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
            case "comissao" -> {
                return String.format("%.2f", this.getComissao());
            }
			case "taxaSindical" -> {
				if (!this.getSindicalizado()) {
					throw new EmpregadoNaoSindicalizadoException();
				}
				return String.format("%.2f", this.getTaxaSindical());
			}
			case "idSindicato" -> {
				if (!this.getSindicalizado()) {
					throw new EmpregadoNaoSindicalizadoException();
				}
				return this.getIdSindicato();
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
    
    public void lancaVenda(String data, String valor) {
		this.vendas.put(data, valor);
	}

	public Map<String, String> getVendas() {
		return vendas;
	}

	public void setVendas(Map<String, String> vendas) {
		this.vendas = vendas;
	}
	
	
	public double getVendasRealizadas(String dataInicial, String dataFinal) throws ParseException{
		double venda = 0;
		SimpleDateFormat sdformat = new SimpleDateFormat("dd/MM/yyyy");
		Date inicio = sdformat.parse(dataInicial);
	    Date fim = sdformat.parse(dataFinal);
	    Date dataAtual = null;
		for (Map.Entry<String,String> itr : this.vendas.entrySet()) {
			dataAtual = sdformat.parse(itr.getKey());
//			System.out.println(itr.getValue());
			if(dataAtual.compareTo(inicio) >= 0 && dataAtual.compareTo(fim) < 0) {
				venda += Double.valueOf(itr.getValue().replace(",", "."));
			}
		}
//		System.out.println("venda" + venda);
		return venda;
	}

	public double getPag_fixo() {
		return pag_fixo;
	}

	public void setPag_fixo(double pag_fixo) {
		this.pag_fixo = pag_fixo;
	}

	public double getPag_vendas() {
		return pag_vendas;
	}

	public void setPag_vendas(double pag_vendas) {
		this.pag_vendas = pag_vendas;
	}

	public double getTotal_vendas() {
		return total_vendas;
	}

	public void setTotal_vendas(double total_vendas) {
		this.total_vendas = total_vendas;
	} 
}
