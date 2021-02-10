package br.com.iteris.templatemethod;

public abstract class Conta {

    protected String nome;
    protected double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

    protected abstract double taxaDescontoDoc(double saldo);

    public void realizaDoc(double valorDoc, Conta contaDestinatario) {
        if (saldo >= valorDoc) {
            saldo -= valorDoc + taxaDescontoDoc(saldo);
            contaDestinatario.recebeDoc(valorDoc);
        } else {
            System.out.println("Não é possivel realizar DOC");
        }
    }

    private void recebeDoc(double valorDoc) {
        this.saldo += valorDoc;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }

}
