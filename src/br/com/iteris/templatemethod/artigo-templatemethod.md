# Design Pattern na Prática - Template Method
Como utilizar o o padrão de projeto Template Method no desenvolvimento de aplicações robustas, de fácil manutenção e orientadadas a mudança.

**O que é Design Pattern ?** 

Quando começamos no desenvolvimento do mundo orientado a objeto, é comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experiência em vários projetos, percebemos que determinados problemas são muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do domínio do projeto, foram desenhadas soluções elegantes que não só resolvem o problema, mas também facilita a manutenção no 
código e seguindo as melhores práticas de desenvolvimento. Para essas soluções damos o nome de Design Patterns ou Padrões de Projeto.

**Conceito do Template Method**

Um dos mais conhecidos e utilizados é o padrão Template Method. Dentro das divisões de classificação dos tipos de Design Patterns, são elas: Criacionais, Estruturais e Comportamentais, o padrão Strategy pertence ao último grupo.
                                                                
O conceito do pattern Template Method é muito simples, a idea é criar um algoritmo aonde determinadas partes desse algoritmo sejam implementadas pelas subclasses concretas que herdam o "esqueleto" da superclasse abstrata. 
Dessa forma na superclasse temos um método(_method_) que possui lacunas(_template_) aonde as subclasses herdarão o método mas ficarão encarregadas de preencher as lacunas com seus próprios comportamento.

Esse padrão é muito parecido com o Strategy, com a sutil diferença que no Strategy o comportamento é implementado por completo e no Template Method apenas parte dele é sobrescrito.

**Apresentação do Problema**

Para utilizar o padrão Template Method, vamos supor um problema do mundo real. Imagine que, em uma aplicação de administração de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupança e Conta Universitaria.  

Para cada tipo de conta, uma taxa diferente é descontada para realizar as transferência bancárias: TED
                                                                               
Regras:

- Conta Corrente: desconta 1% do saldo
- Conta Universitária: desconta 0.5% do saldo
- Conta Poupança: desconta 0% do saldo
- O sistema deve verificar se o saldo é maior ou igual ao valor solicitante do DOC (desconsiderando a taxa de desconto). É permitido o cliente
ficar com a conta negativa devido a cobrança da taxa de desconto
    
Segue o modelo de exemplo do nosso sistema sem utilizar o Design Pattern Template Method:

```java
public class Conta {

    private String nome;
    private double saldo;
    private String tipoConta;

    public Conta(String nome, double saldo, String tipoConta) {
        this.nome = nome;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
    }

    public void realizaDoc(double valorDoc, Conta contaDestinatario) {
        if (saldo >= valorDoc) {
            if (tipoConta.equals("Conta Corrente")) {
                saldo -= valorDoc - saldo * 0.01;
            }

            if (tipoConta.equals("Conta Universitaria")) {
                saldo -= valorDoc - saldo * 0.005;
            }

            if (tipoConta.equals("Conta Poupanca")) {
                saldo -= valorDoc;
            }

            contaDestinatario.recebeDoc(valorDoc);
        } else {
            System.out.println("Não é possivel realizar DOC");
        }
    }

    private void recebeDoc(double valorDoc) {
        this.saldo += valorDoc;
    }

    // Getters ...

}
```
Vemos que a solução apresentada funciona, mas essa é uma solução de fácil manutenção? Orientada a mudança?

E se nosso sistema crescer e novos tipos de conta forem criadas?

E se a lógica de cálculo do rendimento precisar ser utilizada em outro lugar?

O uso demasiado de if e else normalmente também é um sinal que não estamos utilizando o paradigma OO corretamente.

Obviamente, parece não ser a melhor forma de resolver nosso problema.

**Solução Utilizando Template Method**

Seguindo o conceito do Template Method, vamos criar subclasses que extendam o mesmo comportamento mas com uma parte de sua implementação diferente
para cada tipo de conta. 

Para isso, vamos modificar a classe Conta para ser abstrata e definir nela o comportamento padrão de realização de DOC, mas uma parte desse método será 
um método abstrato que cada filha da classe conta (Conta Corrente, Conta Universitária e Conta Poupança) ficará encarregada de preencher sua lacuna.

show me the code:
```java
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
            saldo =- valorDoc - taxaDescontoDoc(saldo);
            contaDestinatario.recebeDoc(valorDoc);
        } else {
            System.out.println("Não é possivel realizar DOC");
        }
    }

    private void recebeDoc(double valorDoc) {
        this.saldo += valorDoc;
    }

    // Getters...

}
```
```java
public class ContaCorrente extends Conta {

    public ContaCorrente(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return saldo * 0.01;
    }

}
```
```java
public class ContaUniversitaria extends Conta {

    public ContaUniversitaria(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return saldo * 0.005;
    }

}
``` 
```java
public class ContaPoupanca extends Conta {

    public ContaPoupanca(String nome, double saldo) {
        super(nome, saldo);
    }

    @Override
    protected double taxaDescontoDoc(double saldo) {
        return 0;
    }
}
```

Como Teste podemos rodar em um Main de exemplo:
```java
public class Main {

    public static void main(String[] args) {
        Conta contaCorrenteAT = new ContaCorrente("Aerys Targeryan", 500);
        Conta contaPoupancaTL = new ContaPoupanca("Tywin Lannister", 1000);
        Conta contaUniversitariaSC = new ContaUniversitaria("Sandor Clegane", 50);

        contaCorrenteAT.realizaDoc(50, contaPoupancaTL);
        contaPoupancaTL.realizaDoc(100, contaUniversitariaSC);
        contaUniversitariaSC.realizaDoc(10, contaCorrenteAT);

        System.out.println("Saldo de Conta Corrente deve ser igual a: 455, valor real: " + contaCorrenteAT.getSaldo());
        System.out.println("Saldo de Conta Poupança deve ser igual a: 950, valor real: " + contaPoupancaTL.getSaldo());
        System.out.println("Saldo de Conta Universitaria deve ser igual a: 139.25, valor real: " + contaUniversitariaSC.getSaldo());
    }

}
```
```shell script
/home/lucasfernandes/.sdkman/candidates/java/15.0.1-open/bin/java -javaagent:/snap/intellij-idea-community/244/lib/idea_rt.jar=38985:/snap/intellij-idea-community/244/bin -Dfile.encoding=ISO-8859-1 -classpath /home/lucasfernandes/git/pessoal-poc/artigos-meetups/out/production/artigos-meetups br.com.iteris.templatemethod.Main
Saldo de Conta Corrente deve ser igual a: 455, valor real: 455.0
Saldo de Conta Poupança deve ser igual a: 950, valor real: 950.0
Saldo de Conta Universitaria deve ser igual a: 139.25, valor real: 139.25

Process finished with exit code 0
```

Ganhos com Template Method

// TODO