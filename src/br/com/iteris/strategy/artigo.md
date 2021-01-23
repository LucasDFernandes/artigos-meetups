# Design Pattern na Prática - Strategy
Como utilizar o o padrão de projeto Strategy no desenvolvimento de aplicações robustas, de fácil manutenção e orientadadas a mudança.

**O que é Design Pattern ?** 

Quando começamos no desenvolvimento do mundo orientado a objeto, é comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experiência em vários projetos, percebemos que determinados problemas são muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do domínio do projeto, foram desenhadas soluções elegantes que não só resolvem o problema, mas também facilita a manutenção no 
código e seguindo as melhores práticas de desenvolvimento. Para essas soluções damos o nome de Design Patterns ou Padrões de Projeto.


**Conceito do Strategy**

Um dos mais famosos Design Patterns é o padrão Strategy, também conhecido como Policy. Dentro das divisões de classificação dos tipos de Design Patterns, são elas: Criacionais, Estruturais e Comportamentais, o padrão Strategy pertence ao último grupo.

O conceito do padrão Strategy consiste em seu software ter um família de algoritimos que tem o mesmo objetivo mas tem uma implementação diferente entre eles e permite que o algoritmo varie independemente dos clientes que o utilizam. Em outras palavras, 
Strategy nos permite configurar uma classe com um de vários comportamentos, utilizando o conceito de OO chamado de polimorfismo.

A estrutura deste padrão pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://robsoncastilho.files.wordpress.com/2011/04/strategy.gif)
  
**Apresentação do Problema**

Para utilizar o padrão Strategy, vamos supor um problema do mundo real. Imagine que, em uma aplicação de administração de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupança e Conta Universitaria. Para cada tipo de conta existem uma regra para cálculo 
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

Exemplo de solução, sem utilizar o padrão Strategy:

```java
package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraAPagarFinanceiro {

    private List<Conta> todasAsContas;

    public CalculadoraAPagarFinanceiro(List<Conta> todasAsContas) {
        this.todasAsContas = todasAsContas;
    }

    public double calculaRendimentoTotalAPagar() {
        double valorTotal = 0;
        for (Conta conta : todasAsContas) {
            if (conta instanceof ContaCorrente) {
                valorTotal += conta.getSaldo() * 0.01;
            }
            if (conta instanceof ContaUniversitaria) {
                valorTotal += conta.getSaldo() * 0.02;
            }
            if (conta instanceof ContaPoupanca) {
                valorTotal += conta.getSaldo() * 0.05;
            }
        }

        return valorTotal;
    }
}
```
 
Vemos que a solução apresentada funciona, mas essa é uma solução de fácil manutenção? Orientada a mudança? 

E se nosso sistema crescer e novos tipos de conta forem criadas?

E se a lógica de cálculo do rendimento precisar ser utilizada em outro lugar? 

Obviamente, parece não ser a melhor forma de resolver nosso problema.

**Solução Utilizando Strategy**

Seguindo o conceito do Strategy, vamos delegar a cada tipo de conta sua responsabilidade de saber 
como ela implementa o seu cálculo de rendimento. Assim quem for utilizar a lógica de cálculo, o client da chamada, não precisará
conhecer a lógica de cálculo. 

Para isso, vamos criar a interface da estratégia: CalculoRendimento e depois implementar em cada tipo de conta a sua própria regra

*_show me the code_*:

```java
package br.com.iteris.strategy;

public interface CalculoRendimento {

    double calculaRendimento();
}
```
```java
package br.com.iteris.strategy;

public abstract class Conta implements CalculoRendimento {

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

    @Override
    public double calculaRendimento() {
        return saldo * 0.01;
    }
}
```
```java
package br.com.iteris.strategy;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    public double calculaRendimento() {
        return saldo * 0.05;
    }
}
```
```java
package br.com.iteris.strategy;

public class ContaUniversitaria extends Conta {

    public ContaUniversitaria(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    public double calculaRendimento() {
        return saldo * 0.02;
    }
}
```
```java
package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraFinanceiro {

    private List<CalculoRendimento> calculoRendimentoList;

    public CalculadoraAPagarFinanceiro(List<CalculoRendimento> calculoRendimentoList) {
        this.calculoRendimentoList = calculoRendimentoList;
    }

    public double calculaRendimentoTotalAPagar() {
        double valorTotal = 0;
        for (CalculoRendimento calculoRendimento : calculoRendimentoList) {
            valorTotal += calculoRendimento.calculaRendimento();
        }

        return valorTotal;
    }
}
```
Para aqueles já familiarizados com Java 8
```java
package br.com.iteris.strategy;

import java.util.List;

public class CalculadoraFinanceiro {

    private List<CalculoRendimento> calculoRendimentoList;

    public CalculadoraAPagarFinanceiro(List<CalculoRendimento> calculoRendimentoList) {
        this.calculoRendimentoList = calculoRendimentoList;
    }

    public double calculaRendimentoTotalAPagar() {
        return calculoRendimentoList.stream().mapToDouble(CalculoRendimento::calculaRendimento).sum();
    }
}

```

Como teste podemos rodar em um _Main_ de exemplo:
```java
package br.com.iteris.strategy;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CalculoRendimento contaCorrente = new ContaCorrente("Jon Snow", 1000.00);
        CalculoRendimento contaUniversitaria = new ContaUniversitaria("Rhaegar Targeryan", 1000.00);
        CalculoRendimento contaPoupanca = new ContaPoupanca("Robert Baratheon", 1000.00);

        List<CalculoRendimento> calculoRendimentoList = Arrays.asList(contaCorrente, contaUniversitaria, contaPoupanca);

        CalculadoraFinanceiro calculadoraFinanceiro = new CalculadoraFinanceiro(calculoRendimentoList);
        System.out.println("Total de rendimentos a pagar: " + calculadoraFinanceiro.calculaRendimentoTotalAPagar());
    }

}

```

**Ganhos com Strategy**

Percebam quão simples ficou para nossa classe _client_ utilizar as regras de cálculo de rendimento para os tipos de conta diferente.
Quem quiser saber o rendimento de uma conta basta invoca-la e sua própria implementação fará o trabalho. Se uma nova conta surgir ou 
alguma outra classe que também gera um rendimento, basta implementarmos o contrato e todo o sistema se encarregará do resto. 
Assim respeitamos o princípio _Open-Closed_.

Outro ganho importante nessa abordagem é que agora não dependemos mais de classes concretas e sim de uma interface, garantido assim
o princípio da inversão de dependência.

O Strategy, é um dos Design Patterns mais conhecidos e utilizados no mundo de desenvolvimento de software, use e abuse dessa poderoso padrão de projeto.
