package br.com.iteris.observer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Conta {

    private String nome;
    private double saldo;
    private AcaoAposAlteracaoSaldoGerenciador gerenciador;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
        this.gerenciador = new AcaoAposAlteracaoSaldoGerenciador(Collections.singleton(new ExtratoConta()));
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;

            if (saldo >= 5000) {
                gerenciador.adicionar(new OfertaCartaoCredito());
            }

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;

        if (saldo >= 5000) {
            gerenciador.adicionar(new OfertaCartaoCredito());
        }
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
