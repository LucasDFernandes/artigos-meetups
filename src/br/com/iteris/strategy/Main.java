package br.com.iteris.strategy;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Conta contaCorrente = new ContaCorrente("Jon Snow", 1000.00);
        Conta contaUniversitaria = new ContaUniversitaria("Rhaegar Targeryan", 1000.00);
        Conta contaPoupanca = new ContaPoupanca("Robert Baratheon", 1000.00);

        List<Conta> contaList = Arrays.asList(contaCorrente, contaUniversitaria, contaPoupanca);

        CalculadoraAPagarFinanceiro calculadoraAPagarFinanceiro = new CalculadoraAPagarFinanceiro(contaList);
        System.out.println("Total de rendimentos a pagar: " + calculadoraAPagarFinanceiro.calculaRendimentoTotalAPagar());
    }

}
