package br.com.iteris.templatemethod;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return 0;
    }
}
