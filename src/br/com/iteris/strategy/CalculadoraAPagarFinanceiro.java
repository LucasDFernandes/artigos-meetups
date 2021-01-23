package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraAPagarFinanceiro {

    private List<Conta> todasAsContas;

    public CalculadoraAPagarFinanceiro(List<Conta> todasAsContas) {
        this.todasAsContas = todasAsContas;
    }

    public double calculaRendimentoTotalAPagar() {
        return todasAsContas.stream().mapToDouble(CalculoRendimento::calculaRendimento).sum();
    }
}
