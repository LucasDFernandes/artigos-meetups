package br.com.iteris.observer;

public class ExtratoConta implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simula��o de emiss�o de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }
}
