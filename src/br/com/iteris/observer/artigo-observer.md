# Design Pattern na Pr�tica - Observer
Como utilizar o o padr�o de projeto Observer no desenvolvimento de aplica��es robustas, de f�cil manuten��o e orientadadas a mudan�a.

**O que � Design Pattern ?** 

Quando come�amos no desenvolvimento do mundo orientado a objeto, � comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experi�ncia em v�rios projetos, percebemos que determinados problemas s�o muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do dom�nio do projeto, foram desenhadas solu��es elegantes que n�o s� resolvem o problema, mas tamb�m facilita a manuten��o no 
c�digo e seguindo as melhores pr�ticas de desenvolvimento. Para essas solu��es damos o nome de Design Patterns ou Padr�es de Projeto.

**Conceito do Observer**

O uso padr�o de projeto � um dos que evidenciam a diferen�a de um desenvolver de software iniciante para um desenvolvedor de software mais experiente. Dentro das divis�es de classifica��o dos tipos de Design Patterns, 
s�o elas: Criacionais, Estruturais e Comportamentais, o padr�o Observer pertence ao �ltimo grupo.  

O Observer, tamb�m conhecido como _Listener_ e _Event-Subscriber_, permite que voc� defina um mecanismo de assinatura para notificar m�ltiplos objetos 
sobre quaisquer eventos que aconte�am com o objeto que eles est�o observando. Dessa forma, quando o objeto que est� sendo observado muda seu estado, todos os assinantes
que est�o observando executam sua l�gica utilizando o objeto observado.

O objeto que tem o estado que deseja ser observado � normalmente chamado de sujeito (_subject_), mas j� que ele tamb�m vai notificar outros objetos sobre as mudan�as em seu estado, 
podemos chama-lo de Publicador (_Publisher_). Todos os outros objetos que querem saber das mudan�as do estado do publicador s�o chamados de assinantes (_Subscriber_).

Como todos os assinantes tem possuir a mesma a assinatura de m�todo para usar o polimorfismo, usamos uma interface para realizar a execu��o dos assinantes.    

Vale lembrar que nesse padr�o de projeto, a ordem em que os assinantes executam suas opera��es � um fator arbitr�rio, ou seja, 
podem ser executados em qualquer ordem. 

A estrutura deste padr�o pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://refactoring.guru/images/patterns/diagrams/observer/structure.png)

**Apresenta��o do Problema**

Para utilizar o padr�o Observer, vamos supor um problema do mundo real. Imagine que, em uma aplica��o de administra��o de banco, 
temos algumas funcionalidades que fic�o dispon�veis para os correntistas do banco dependendo da quantidade de saldo que tem na sua conta.

Regra: 

Ap�s altera��o no saldo da conta, sistema do banco deve fazer:

- Emitir Extrato de Altera��o:
    - Toda altera��o no saldo da conta deve emitir um extrato de altera��o informando o saldo ap�s a altera��o.
- Verifica��o de an�lise de perfil para oferta de empr�stimo
    - Ap�s altera��o do saldo da conta, realizar a an�lise de perfil para envio de oferta de empr�stimo.
    
Segue o modelo de exemplo do nosso sistema sem utilizar o Design Pattern Observer:
```java
public class Conta {

    private String nome;
    private double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;

            ExtratoConta extratoConta = new ExtratoConta();
            OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

            extratoConta.emitirExtratoConta(this);
            ofertaEmprestimo.notificarOfertaEmprestimo(this);

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;
        ExtratoConta extratoConta = new ExtratoConta();
        OfertaEmprestimo ofertaEmprestimo = new OfertaEmprestimo();

        extratoConta.emitirExtratoConta(this);
        ofertaEmprestimo.notificarOfertaEmprestimo(this);
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
```
```java
public class ExtratoConta {

    public void emitirExtratoConta(Conta conta) {
        // simula��o de emiss�o de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }

}
```
```java
public class OfertaEmprestimo {

    public void notificarOfertaEmprestimo(Conta conta) {
        // simula��o de envio de an�lise de perfil para oferta de empr�stimo
        System.out.println("Enviar notifica��o de an�lise de perfil para oferta de empr�stimo");
    }

}
```
```java
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
```
```shell script
Conta de Aegon Targeryan
Saldo da conta = 11000.0
Enviar notifica��o de an�lise de perfil para oferta de empr�stimo

Conta de Tywin Lannister
Saldo da conta = 8000.0
Enviar notifica��o de an�lise de perfil para oferta de empr�stimo


Process finished with exit code 0
```
Vemos que a solu��o apresentada funciona, mas essa � uma solu��o de f�cil manuten��o? Orientada a mudan�a? 

E se novas a��es ap�s a altera��o do saldo forem criadas? Iremos sempre instanciar uma classe a mais e manualmente invocarmos a execu��o da sua l�gica?

E se a l�gica de a��o ap�s altera��o do saldo forem utilizadas em outros lugares? Vamos replicar esse bloco de c�digo de chamadas em todos os pontos aonde o saldo for alterado?

Obviamente, parece n�o ser a melhor forma de resolver nosso problema.

**Solu��o Utilizando Observer**

Seguindo o conceito do Observer, nessa implementa��o o pr�prio objeto Conta ter� a lista de a��es ap�s altera��o de saldo e ele ser� respons�vel por registrar os assinantes 
que ficar�o observando a altera��o do saldo. O objeto Conta n�o se preocupa mais em instanciar e chamar a execu��o de cada assinante, por meio do uso do polimorfismo cada um
dentro do loop da lista executar� sua pr�pria a��o. 

*_show me the code_*
```java
public class Conta {

    private String nome;
    private double saldo;
    private List<AcaoAposAlteracaoSaldo> listaAcoes;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
        this.listaAcoes = Arrays.asList(new ExtratoConta(), new OfertaEmprestimo());
    }

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
            listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(this));
            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;
        listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(this));
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
}
```
```java
public interface AcaoAposAlteracaoSaldo {

    void executaAcao(Conta conta);
}
```
```java
public class OfertaEmprestimo implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simula��o de envio de an�lise de perfil para oferta de empr�stimo
        System.out.println("Enviar notifica��o de an�lise de perfil para oferta de empr�stimo");
    }
}
```
```java
public class ExtratoConta implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simula��o de emiss�o de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }
}
```
Como teste podemos rodar em um _Main_ de exemplo:
```shell script
Conta de Aegon Targeryan
Saldo da conta = 11000.0
Enviar notifica��o de an�lise de perfil para oferta de empr�stimo

Conta de Tywin Lannister
Saldo da conta = 8000.0
Enviar notifica��o de an�lise de perfil para oferta de empr�stimo


Process finished with exit code 0
```
