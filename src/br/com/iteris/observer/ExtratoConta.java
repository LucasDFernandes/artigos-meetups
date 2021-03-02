package br.com.iteris.observer;

public class ExtratoConta {

    public void emitirExtratoConta(Conta conta) {
        // simulação de emissão de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }

}
