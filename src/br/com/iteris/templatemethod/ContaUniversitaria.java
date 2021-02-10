package br.com.iteris.templatemethod;

public class ContaUniversitaria extends Conta {

    public ContaUniversitaria(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return saldo * 0.005;
    }

}
