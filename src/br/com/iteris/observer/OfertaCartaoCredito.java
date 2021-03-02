package br.com.iteris.observer;

public class OfertaCartaoCredito implements AcaoAposAlteracaoSaldo{

    @Override
    public void executaAcao(Conta conta) {
        // simula��o de envio de notifica��o de oferta de cart�o de cr�dito
        System.out.println("Enviar notifica��o de oferta de cart�o de cr�dito");
    }
}
