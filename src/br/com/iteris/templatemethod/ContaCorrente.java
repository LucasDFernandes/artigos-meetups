package br.com.iteris.templatemethod;

public class ContaCorrente extends Conta {

    public ContaCorrente(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return saldo * 0.01;
    }

}
