package br.com.iteris.strategy;

public abstract class Conta implements CalculoRendimento {

    protected String nome;
    protected double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
