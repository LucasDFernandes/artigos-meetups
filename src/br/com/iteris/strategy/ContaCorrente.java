package br.com.iteris.strategy;

public class ContaCorrente extends Conta {

    public ContaCorrente(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    public double calculaRendimento() {
        return saldo * 0.01;
    }
}
