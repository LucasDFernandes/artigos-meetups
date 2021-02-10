package br.com.iteris.templatemethod;

public class Main {

    public static void main(String[] args) {
        Conta contaCorrenteAT = new ContaCorrente("Aerys Targeryan", 500);
        Conta contaPoupancaTL = new ContaPoupanca("Tywin Lannister", 1000);
        Conta contaUniversitariaSC = new ContaUniversitaria("Sandor Clegane", 50);

        contaCorrenteAT.realizaDoc(50, contaPoupancaTL);
        contaPoupancaTL.realizaDoc(100, contaUniversitariaSC);
        contaUniversitariaSC.realizaDoc(10, contaCorrenteAT);

        System.out.println("Saldo de Conta Corrente deve ser igual a: 455, valor real: " + contaCorrenteAT.getSaldo());
        System.out.println("Saldo de Conta Poupança deve ser igual a: 950, valor real: " + contaPoupancaTL.getSaldo());
        System.out.println("Saldo de Conta Universitaria deve ser igual a: 139.25, valor real: " + contaUniversitariaSC.getSaldo());
    }

}
