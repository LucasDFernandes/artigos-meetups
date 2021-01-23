# Design Pattern na Prática - Strategy
Como utilizar o o padrão de projeto Strategy no desenvolvimento de aplicações robustas, de fácil manutenção e orientadadas a mudança.

**O que é Design Pattern ?** 

Quando começamos no desenvolvimento do mundo orientado a objeto, é comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirando experiência em vários projetos, percebemos que determinados problemas são muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do domínio do projeto, foram desenhadas soluções elegantes que não só resolvem o problema, mas também facilita a manutenção no 
código e seguindo as melhores práticas de desenvolvimento. Para essas soluções damos o nome de Design Patterns ou Padrões de Projeto.


**Conceito do Strategy**

Um dos mais famosos Design Patterns é o padrão Strategy, também conhecido como Policy. Dentro das divisões de classificação dos tipos de Design Patterns, são elas: Criacionais, Estruturais e Comportamentais, o padrão Strategy pertence ao último grupo.

O conceito do padrão Strategy consiste em seu software ter um família de algoritimos que tem o mesmo objetivo mas tem uma implementação diferente entre eles e permite que o algoritmo varie independemente dos clientes que o utilizam. Em outras palavras, 
Strategy nos permite configurar uma classe com um de vários comportamentos, utilizando o conceito de OO chamado de composição.

A estrutura deste padrão pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://robsoncastilho.files.wordpress.com/2011/04/strategy.gif)
  
**Apresentação do Problema**

Para utilizar o padrão Strategy, vamos supor um problema do mundo real. Imagine que, em uma aplicação de administração de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupança e Conta Salário. Para cada tipo de conta existem uma regra para cálculo 
de rendimento em cima do saldo da conta.

Regras:

- Conta Corrente: rende 1% do saldo
- Conta Universitária: rende 2% do saldo
- Conta Poupança: rende 5% do saldo

Nosso sistema tem que receber um lista de contas, de todos os tipos, e calcular o valor total de rendimento a ser pago pelo banco.

Segue os modelos de exemplo do nosso sistema:
 
```java
package br.com.iteris.strategy;

public abstract class Conta {

    protected String nome;
    protected double saldo;

    public Conta(String nome, double saldo) {
        this.nome = nome;
        this.saldo = saldo;
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
package br.com.iteris.strategy;

public class ContaCorrente extends Conta {

    public ContaCorrente(String nome, double saldo) {
        super(nome, saldo);
    }

}
```
```java
package br.com.iteris.strategy;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String nome, double saldo) {
        super(nome, saldo);
    }

}
```
```java
package br.com.iteris.strategy;

public class ContaUniversitaria extends Conta {

    public ContaUniversitaria(String nome, double saldo) {
        super(nome, saldo);
    }
    
}

```

 

