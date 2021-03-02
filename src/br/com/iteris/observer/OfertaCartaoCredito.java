package br.com.iteris.observer;

public class OfertaCartaoCredito implements AcaoAposAlteracaoSaldo{

    @Override
    public void executaAcao(Conta conta) {
        // simulação de envio de notificação de oferta de cartão de crédito
        System.out.println("Enviar notificação de oferta de cartão de crédito");
    }
}
