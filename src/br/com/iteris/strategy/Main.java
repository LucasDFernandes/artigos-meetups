package br.com.iteris.strategy;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CalculoRendimento contaCorrente = new ContaCorrente("Jon Snow", 1000.00);
        CalculoRendimento contaUniversitaria = new ContaUniversitaria("Rhaegar Targeryan", 1000.00);
        CalculoRendimento contaPoupanca = new ContaPoupanca("Robert Baratheon", 1000.00);

        List<CalculoRendimento> calculoRendimentoList = Arrays.asList(contaCorrente, contaUniversitaria, contaPoupanca);

        CalculadoraFinanceiro calculadoraFinanceiro = new CalculadoraFinanceiro(calculoRendimentoList);
        System.out.println("Total de rendimentos a pagar: " + calculadoraFinanceiro.calculaRendimentoTotalAPagar());
    }

}
