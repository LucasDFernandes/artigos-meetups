package br.com.iteris.observer;

public class OfertaEmprestimo implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simula��o de envio de an�lise de perfil para oferta de empr�stimo
        System.out.println("Enviar notifica��o de an�lise de perfil para oferta de empr�stimo");
    }
}
