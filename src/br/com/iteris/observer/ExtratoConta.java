package br.com.iteris.observer;

public class ExtratoConta {

    public void emitirExtratoConta(Conta conta) {
        // simula��o de emiss�o de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }

}
