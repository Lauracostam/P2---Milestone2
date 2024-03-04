package br.ufal.ic.p2.wepayu.Exception;

public class CompararDatasException extends Exception{
	public CompararDatasException() {
		super("Data inicial nao pode ser posterior aa data final.");
	}

}
