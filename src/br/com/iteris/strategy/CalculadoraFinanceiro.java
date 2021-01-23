package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraFinanceiro {

    private List<CalculoRendimento> calculoRendimentoList;

    public CalculadoraFinanceiro(List<CalculoRendimento> calculoRendimentoList) {
        this.calculoRendimentoList = calculoRendimentoList;
    }

    public double calculaRendimentoTotalAPagar() {
        return calculoRendimentoList.stream().mapToDouble(CalculoRendimento::calculaRendimento).sum();
    }
}
