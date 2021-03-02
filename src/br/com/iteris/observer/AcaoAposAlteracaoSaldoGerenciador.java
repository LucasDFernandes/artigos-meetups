package br.com.iteris.observer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcaoAposAlteracaoSaldoGerenciador {

    private Set<AcaoAposAlteracaoSaldo> listaAcaoAposAlteracaoSaldos;

    public AcaoAposAlteracaoSaldoGerenciador() {
        listaAcaoAposAlteracaoSaldos = new HashSet<>();
    }

    public AcaoAposAlteracaoSaldoGerenciador(Set<AcaoAposAlteracaoSaldo> listaAcaoAposAlteracaoSaldos) {
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
