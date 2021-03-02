package br.com.iteris.observer;

import java.util.Collections;

public class Conta {

    private String nome;
    private double saldo;
    private AcaoAposAlteracaoSaldoGerenciador gerenciador;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
        this.gerenciador = new AcaoAposAlteracaoSaldoGerenciador(Collections.singleton(new ExtratoConta()));
    }

    private void verificaEnvioOFertaCredito() {
        if (saldo >= 5000) {
            gerenciador.adicionar(new OfertaCartaoCredito());
        } else {
            gerenciador.remover(new OfertaCartaoCredito());
        }
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;

            verificaEnvioOFertaCredito();
            gerenciador.notificar(this);

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;

        verificaEnvioOFertaCredito();
        gerenciador.notificar(this);
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
