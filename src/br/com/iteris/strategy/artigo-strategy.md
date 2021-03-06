# Design Pattern na Pr�tica - Strategy
Como utilizar o o padr�o de projeto Strategy no desenvolvimento de aplica��es robustas, de f�cil manuten��o e orientadadas a mudan�a.

**O que � Design Pattern ?** 

Quando come�amos no desenvolvimento do mundo orientado a objeto, � comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experi�ncia em v�rios projetos, percebemos que determinados problemas s�o muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do dom�nio do projeto, foram desenhadas solu��es elegantes que n�o s� resolvem o problema, mas tamb�m facilita a manuten��o no 
c�digo e seguindo as melhores pr�ticas de desenvolvimento. Para essas solu��es damos o nome de Design Patterns ou Padr�es de Projeto.


**Conceito do Strategy**

Um dos mais famosos Design Patterns � o padr�o Strategy, tamb�m conhecido como Policy. Dentro das divis�es de classifica��o dos tipos de Design Patterns, s�o elas: Criacionais, Estruturais e Comportamentais, o padr�o Strategy pertence ao �ltimo grupo.

O conceito do padr�o Strategy consiste em seu software ter um fam�lia de algoritimos que tem o mesmo objetivo mas tem uma implementa��o diferente entre eles e permite que o algoritmo varie independemente dos clientes que o utilizam. Em outras palavras, 
Strategy nos permite configurar uma classe com um de v�rios comportamentos, utilizando o conceito de OO chamado de polimorfismo.

A estrutura deste padr�o pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://robsoncastilho.files.wordpress.com/2011/04/strategy.gif)
  
**Apresenta��o do Problema**

Para utilizar o padr�o Strategy, vamos supor um problema do mundo real. Imagine que, em uma aplica��o de administra��o de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupan�a e Conta Universitaria. Para cada tipo de conta existem uma regra para c�lculo 
de rendimento em cima do saldo da conta.

Regras:

- Conta Corrente: rende 1% do saldo
- Conta Universit�ria: rende 2% do saldo
- Conta Poupan�a: rende 5% do saldo

Nosso sistema tem que receber um lista de contas, de todos os tipos, e calcular o valor total de rendimento a ser pago pelo banco.

Segue o modelo de exemplo do nosso sistema:
 
```java
package br.com.iteris.strategy;

public class Conta {

    private String nome;
    private double saldo;
    private String tipoConta;

    public Conta(String nome, double saldo, String tipoConta) {
        this.nome = nome;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }
    
    public String getTipoConta() {
        return tipoConta;    
    }

}
```

Exemplo de solu��o, sem utilizar o padr�o Strategy:

```java
package br.com.iteris.strategy;

import java.util.List;import br.com.iteris.domain.Conta;

public class CalculadoraFinanceiro {

    private List<Conta> contas;

    public CalculadoraAPagarFinanceiro(List<Conta> contas) {
        this.contas = contas;
    }

    public double calculaRendimentoTotalAPagar() {
        double valorTotal = 0;
        for (Conta conta : contas) {
            if (conta.getTipoConta().equals("Conta Corrente")) {
                valorTotal += conta.getSaldo() * 0.01;
            }
            if (conta.getTipoConta().equals("Conta Universitaria")) {
                valorTotal += conta.getSaldo() * 0.02;
            }
            if (conta.getTipoConta().equals("Conta Poupan�a")) {
                valorTotal += conta.getSaldo() * 0.05;
            }
        }

        return valorTotal;
    }
}
```
 
Vemos que a solu��o apresentada funciona, mas essa � uma solu��o de f�cil manuten��o? Orientada a mudan�a? 

E se nosso sistema crescer e novos tipos de conta forem criadas?

E se a l�gica de c�lculo do rendimento precisar ser utilizada em outro lugar? 

Obviamente, parece n�o ser a melhor forma de resolver nosso problema.

**Solu��o Utilizando Strategy**

Seguindo o conceito do Strategy, vamos delegar a cada tipo de conta sua responsabilidade de saber 
como ela implementa o seu c�lculo de rendimento. Assim quem for utilizar a l�gica de c�lculo, o client da chamada, n�o precisar�
conhecer a l�gica de c�lculo. 

Para isso, vamos criar a interface da estrat�gia: CalculoRendimento e depois implementar em cada tipo de conta a sua pr�pria regra

*_show me the code_*:

```java
public interface CalculoRendimento {

    double calculaRendimento();
}
```
```java
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
Para aqueles j� familiarizados com Java 8
```java
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

Percebam qu�o simples ficou para nossa classe _client_ utilizar as regras de c�lculo de rendimento para os tipos de conta diferente.
Quem quiser saber o rendimento de uma conta basta invoca-la e sua pr�pria implementa��o far� o trabalho. Se uma nova conta surgir ou 
alguma outra classe que tamb�m gera um rendimento, basta implementarmos o contrato e todo o sistema se encarregar� do resto. 
Assim respeitamos o princ�pio _Open-Closed (OCP)_.

Outro ganho importante nessa abordagem � que agora n�o dependemos mais de classes concretas e sim de uma interface que � uma estrutura mais est�vel,
garantido assim o princ�pio da invers�o de depend�ncia.

O Strategy, � um dos Design Patterns mais conhecidos e utilizados no mundo de desenvolvimento de software, use e abuse desse poderoso padr�o de projeto.
