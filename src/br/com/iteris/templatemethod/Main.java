package br.com.iteris.templatemethod;

public class Main {

    public static void main(String[] args) {
        Conta contaCorrenteAT = new ContaCorrente("Aerys Targeryan", 1000);
        Conta contaPoupancaTL = new ContaPoupanca("Tywin Lannister", 1000);
        Conta contaUniversitariaSC = new ContaUniversitaria("Sandor Clegane", 1000);

        contaCorrenteAT.realizaDoc(100, contaPoupancaTL);
        contaPoupancaTL.realizaDoc(100, contaUniversitariaSC);
        contaUniversitariaSC.realizaDoc(100, contaCorrenteAT);

        System.out.println("Saldo de Conta Corrente deve ser igual a: 990, valor real: " + contaCorrenteAT.getSaldo());
        System.out.println("Saldo de Conta Poupança deve ser igual a: 1000, valor real: " + contaPoupancaTL.getSaldo());
        System.out.println("Saldo de Conta Universitaria deve ser igual a: 994.5, valor real: " + contaUniversitariaSC.getSaldo());
    }

}
