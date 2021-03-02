# Design Pattern na Prática - Observer
Como utilizar o o padrão de projeto Observer no desenvolvimento de aplicações robustas, de fácil manutenção e orientadadas a mudança.

**O que é Design Pattern ?** 

Quando começamos no desenvolvimento do mundo orientado a objeto, é comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experiência em vários projetos, percebemos que determinados problemas são muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do domínio do projeto, foram desenhadas soluções elegantes que não só resolvem o problema, mas também facilita a manutenção no 
código e seguindo as melhores práticas de desenvolvimento. Para essas soluções damos o nome de Design Patterns ou Padrões de Projeto.

**Conceito do Observer**

O uso padrão de projeto é um dos que evidenciam a diferença de um desenvolver de software iniciante para um desenvolvedor de software mais experiente. Dentro das divisões de classificação dos tipos de Design Patterns, 
são elas: Criacionais, Estruturais e Comportamentais, o padrão Observer pertence ao último grupo.  

O Observer, também conhecido como _Listener_ e _Event-Subscriber_, permite que você defina um mecanismo de assinatura para notificar múltiplos objetos 
sobre quaisquer eventos que aconteçam com o objeto que eles estão observando. Dessa forma, quando o objeto que está sendo observado muda seu estado, todos os assinantes
que estão observando executam sua lógica utilizando o objeto observado.

Vale lembrar que nesse padrão de projeto, a ordem em que os assinantes executam suas operações é um fator arbitrário, ou seja, 
podem ser executados em qualquer ordem. 

O objeto que tem o estado que deseja ser observado é normalmente chamado de sujeito (_subject_), mas já que ele também vai notificar outros objetos sobre as mudanças em seu estado, podemos cham-alo de Publicador (_Publisher_).
Todos os outros objetos que querem saber das mudanças do estado do publicador são chamados de assinantes (_Subscriber_).

Como todos os assinantes tem possuir a mesma a assinatura de método para usar o polimorfismo, usamos uma interface para realizar a execução dos assinantes.    

A estrutura deste padrão pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://refactoring.guru/images/patterns/diagrams/observer/structure.png)

**Apresentação do Problema**

Para utilizar o padrão Observer, vamos supor um problema do mundo real. Imagine que, em uma aplicação de administração de banco, 
temos algumas funcionalidades que ficão disponíveis para os correntistas do banco dependendo da quantidade de saldo que tem na sua conta.

Regra: 

Após alteração no saldo da conta, sistema do banco deve fazer:

- Oferta de cartão de crédito: Quando o saldo da conta for alterado e tiver o valor igual ou acima de R$ 5.000,00, verificar:
    - Enviar a notificação de oferta de cartão de crédito.
    - Se o o saldo ficar abaixo de R$ 5.000,00, envio de notificação é suspensa.
- Emitir Extrato de Alteração:
    - Toda alteração de conta deve emitir um extrato de alteração informando o saldo após a alteração.
    
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

            OfertaCartaoCredito ofertaCartaoCredito = new OfertaCartaoCredito();
            ExtratoConta extratoConta = new ExtratoConta();

            ofertaCartaoCredito.ofertarCartaoCredito(this);
            extratoConta.emitirExtratoConta(this);

            return valor;
        }

        return 0;
    }

    public void deposita(double valor) {
        saldo += valor;

        OfertaCartaoCredito ofertaCartaoCredito = new OfertaCartaoCredito();
        ExtratoConta extratoConta = new ExtratoConta();

        ofertaCartaoCredito.ofertarCartaoCredito(this);
        extratoConta.emitirExtratoConta(this);
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
public class OfertaCartaoCredito {

    public void ofertarCartaoCredito(Conta conta) {
        if (conta.getSaldo() >= 5000) {
            // simulação de envio de notificação de oferta de cartão de crédito
            System.out.println("Enviar notificação de oferta de cartão de crédito");
        }
    }
}
```
```java
public class ExtratoConta {

    public void emitirExtratoConta(Conta conta) {
        // simulação de emissão de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }
}
```
```java
public class Main {

    public static void main(String[] args) {
        Conta aegonTargeryan = new Conta("Aegon Targeryan", 1000);
        Conta tywinLannister = new Conta("Tywin Lannister", 1000);

        System.out.println("Conta de " + aegonTargeryan.getNome());
        aegonTargeryan.deposita(6000);

        System.out.println("");

        System.out.println("Conta de " + tywinLannister.getNome());
        tywinLannister.deposita(3000);
    }
}
```
```shell script
Conta de Aegon Targeryan
Enviar notificação de oferta de cartão de crédito
Saldo da conta = 7000.0

Conta de Tywin Lannister
Saldo da conta = 4000.0

Process finished with exit code 0
```
Vemos que a solução apresentada funciona, mas essa é uma solução de fácil manutenção? Orientada a mudança? 

E se novas ações após a alteração do saldo forem criadas? Iremos sempre instanciar uma classe a mais e manualmente invocarmos a execução da sua lógica?

E se a lógica de ação após alteração do saldo forem utilizadas em outros lugares? Vamos replicar esse bloco de código de chamadas em todos os pontos aonde o saldo for alterado?

Obviamente, parece não ser a melhor forma de resolver nosso problema.

**Solução Utilizando Observer**

Seguindo o conceito do Observer, vamos criar um gerenciador de eventos e ele será responsável por adicionar, remover e notificar os assinantes
do objeto Conta. Vamos isolar a lógica da ação após alteração do saldo para uma interface e assim fazer uso do polimorfismo. 

*_show me the code_*
