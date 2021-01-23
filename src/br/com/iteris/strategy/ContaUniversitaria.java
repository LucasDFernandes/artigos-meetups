package br.com.iteris.strategy;

public class ContaUniversitaria extends Conta {

    public ContaUniversitaria(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    public double calculaRendimento() {
        return saldo * 0.02;
    }
}
