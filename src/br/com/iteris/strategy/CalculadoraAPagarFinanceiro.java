package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraAPagarFinanceiro {

    private List<Conta> todasAsContas;

    public CalculadoraAPagarFinanceiro(List<Conta> todasAsContas) {
        this.todasAsContas = todasAsContas;
    }

    public double calculaRendimentoTotalAPagar() {
        double valorTotal = 0;
        for (Conta conta : todasAsContas) {
            if (conta instanceof ContaCorrente) {
                valorTotal += conta.getSaldo() * 0.01;
            }
            if (conta instanceof ContaUniversitaria) {
                valorTotal += conta.getSaldo() * 0.02;
            }
            if (conta instanceof ContaPoupanca) {
                valorTotal += conta.getSaldo() * 0.05;
            }
        }

        return valorTotal;
    }
}
