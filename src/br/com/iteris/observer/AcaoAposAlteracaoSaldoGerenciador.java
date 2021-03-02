package br.com.iteris.observer;

import java.util.ArrayList;
import java.util.List;

public class AcaoAposAlteracaoSaldoGerenciador {

    private List<AcaoAposAlteracaoSaldo> listaAcaoAposAlteracaoSaldos;

    public AcaoAposAlteracaoSaldoGerenciador() {
        listaAcaoAposAlteracaoSaldos = new ArrayList<>();
    }

    public AcaoAposAlteracaoSaldoGerenciador(List<AcaoAposAlteracaoSaldo> listaAcaoAposAlteracaoSaldos) {
        this.listaAcaoAposAlteracaoSaldos = listaAcaoAposAlteracaoSaldos;
    }

    public void adicionar(AcaoAposAlteracaoSaldo acaoAposAlteracaoSaldo) {
        listaAcaoAposAlteracaoSaldos.add(acaoAposAlteracaoSaldo);
    }

    public void remover(AcaoAposAlteracaoSaldo acaoAposAlteracaoSaldo) {
        listaAcaoAposAlteracaoSaldos.remove(acaoAposAlteracaoSaldo);
    }

    public void notificar(Conta conta) {
        listaAcaoAposAlteracaoSaldos.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(conta));
    }
}
