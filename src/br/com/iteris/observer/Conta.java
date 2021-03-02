package br.com.iteris.observer;

public class Conta {

    private static final double VALOR_APTO_OFERTA_EMPRESTIMO = 10000;

    private String nome;
    private double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

    private boolean verificaOfertaEmprestimoHabilitada() {
        return this.saldo >= VALOR_APTO_OFERTA_EMPRESTIMO;
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;

            ExtratoConta extratoConta = new ExtratoConta();
            OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

            extratoConta.emitirExtratoConta(this);
            if (verificaOfertaEmprestimoHabilitada()) {
                ofertaEmprestimo.notificarOfertaEmprestimo(this);
            }

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;
        ExtratoConta extratoConta = new ExtratoConta();
        OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

        extratoConta.emitirExtratoConta(this);
        if (verificaOfertaEmprestimoHabilitada()) {
            ofertaEmprestimo.notificarOfertaEmprestimo(this);
        }
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
