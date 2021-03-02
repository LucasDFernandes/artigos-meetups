package br.com.iteris.observer;

public class OfertaEmprestimo implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simulação de envio de análise de perfil para oferta de empréstimo
        System.out.println("Enviar notificação de análise de perfil para oferta de empréstimo");
    }
}
