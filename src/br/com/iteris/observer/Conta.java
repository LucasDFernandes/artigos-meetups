package br.com.iteris.observer;

public class Conta {

    private String nome;
    private double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;

            ExtratoConta extratoConta = new ExtratoConta();
            OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

            extratoConta.emitirExtratoConta(this);
            ofertaEmprestimo.notificarOfertaEmprestimo(this);

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;
        ExtratoConta extratoConta = new ExtratoConta();
        OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

        extratoConta.emitirExtratoConta(this);
        ofertaEmprestimo.notificarOfertaEmprestimo(this);
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
