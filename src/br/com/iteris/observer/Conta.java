package br.com.iteris.observer;

import java.util.Arrays;
import java.util.List;

public class Conta {

    private String nome;
    private double saldo;
    private List<AcaoAposAlteracaoSaldo> listaAcoes;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
        this.listaAcoes = Arrays.asList(new ExtratoConta(), new OfertaEmprestimo());
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
            listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(this));
            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;
        listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(this));
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
