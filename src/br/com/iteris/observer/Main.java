package br.com.iteris.observer;

public class Main {

    public static void main(String[] args) {
        Conta aegonTargeryan = new Conta("Aegon Targeryan", 5000);
        Conta tywinLannister = new Conta("Tywin Lannister", 5000);

        System.out.println("Conta de " + aegonTargeryan.getNome());
        aegonTargeryan.deposita(6000);
        System.out.println("");

        System.out.println("Conta de " + tywinLannister.getNome());
        tywinLannister.deposita(3000);
        System.out.println("");
    }
}
