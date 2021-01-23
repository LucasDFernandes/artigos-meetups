# Design Pattern na Pr�tica - Strategy
Como utilizar o o padr�o de projeto Strategy no desenvolvimento de aplica��es robustas, de f�cil manuten��o e orientadadas a mudan�a.

**O que � Design Pattern ?** 

Quando come�amos no desenvolvimento do mundo orientado a objeto, � comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirando experi�ncia em v�rios projetos, percebemos que determinados problemas s�o muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do dom�nio do projeto, foram desenhadas solu��es elegantes que n�o s� resolvem o problema, mas tamb�m facilita a manuten��o no 
c�digo e seguindo as melhores pr�ticas de desenvolvimento. Para essas solu��es damos o nome de Design Patterns ou Padr�es de Projeto.


**Conceito do Strategy**

Um dos mais famosos Design Patterns � o padr�o Strategy, tamb�m conhecido como Policy. Dentro das divis�es de classifica��o dos tipos de Design Patterns, s�o elas: Criacionais, Estruturais e Comportamentais, o padr�o Strategy pertence ao �ltimo grupo.

O conceito do padr�o Strategy consiste em seu software ter um fam�lia de algoritimos que tem o mesmo objetivo mas tem uma implementa��o diferente entre eles e permite que o algoritmo varie independemente dos clientes que o utilizam. Em outras palavras, 
Strategy nos permite configurar uma classe com um de v�rios comportamentos, utilizando o conceito de OO chamado de composi��o.

A estrutura deste padr�o pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://robsoncastilho.files.wordpress.com/2011/04/strategy.gif)
  
**Apresenta��o do Problema**

Para utilizar o padr�o Strategy, vamos supor um problema do mundo real. Imagine que, em uma aplica��o de administra��o de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupan�a e Conta Sal�rio. Para cada tipo de conta existem uma regra para c�lculo 
de rendimento em cima do saldo da conta.

Regras:

- Conta Corrente: rende 1% do saldo
- Conta Universit�ria: rende 2% do saldo
- Conta Poupan�a: rende 5% do saldo

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

 

