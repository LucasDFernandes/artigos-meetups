# Design Pattern na Pr�tica - Template Method
Como utilizar o o padr�o de projeto Template Method no desenvolvimento de aplica��es robustas, de f�cil manuten��o e orientadadas a mudan�a.

**O que � Design Pattern ?** 

Quando come�amos no desenvolvimento do mundo orientado a objeto, � comum encontrarmos alguns desafios envolvendo a modelagem das classes e seus comportamentos.
Conforme vamos adquirindo experi�ncia em v�rios projetos, percebemos que determinados problemas s�o muito recorrentes mesmo em software distintos. 

Para esses desafios muito comuns e que ocorrem de maneira frequente, independente do dom�nio do projeto, foram desenhadas solu��es elegantes que n�o s� resolvem o problema, mas tamb�m facilita a manuten��o no 
c�digo e seguindo as melhores pr�ticas de desenvolvimento. Para essas solu��es damos o nome de Design Patterns ou Padr�es de Projeto.

**Conceito do Template Method**

Um dos mais conhecidos e utilizados � o padr�o Template Method. Dentro das divis�es de classifica��o dos tipos de Design Patterns, s�o elas: Criacionais, Estruturais e Comportamentais, o padr�o Strategy pertence ao �ltimo grupo.
                                                                
O conceito do pattern Template Method � muito simples, a idea � criar um algoritmo aonde determinadas partes desse algoritmo sejam implementadas pelas subclasses concretas que herdam o "esqueleto" da superclasse abstrata. 
Dessa forma na superclasse temos um m�todo(_method_) que possui lacunas(_template_) aonde as subclasses herdar�o o m�todo mas ficar�o encarregadas de preencher as lacunas com seus pr�prios comportamento.

Esse padr�o � muito parecido com o Strategy, com a sutil diferen�a que no Strategy o comportamento � implementado por completo e no Template Method apenas parte dele � sobrescrito.

**Apresenta��o do Problema**

Para utilizar o padr�o Template Method, vamos supor um problema do mundo real. Imagine que, em uma aplica��o de administra��o de banco, 
existem 3 tipos de conta: Conta Corrente, Conta Poupan�a e Conta Universitaria.  

Para cada tipo de conta, uma taxa diferente � descontada para realizar as transfer�ncia banc�rias: TED
                                                                               
Regras:

- Conta Corrente: desconta 1% do saldo
- Conta Universit�ria: desconta 0.5% do saldo
- Conta Poupan�a: desconta 0% do saldo
- O sistema deve verificar se o saldo � maior ou igual ao valor solicitante do DOC (desconsiderando a taxa de desconto). � permitido o cliente
ficar com a conta negativa devido a cobran�a da taxa de desconto
    
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
            System.out.println("N�o � possivel realizar DOC");
        }
    }

    private void recebeDoc(double valorDoc) {
        this.saldo += valorDoc;
    }

    // Getters ...

}
```
Vemos que a solu��o apresentada funciona, mas essa � uma solu��o de f�cil manuten��o? Orientada a mudan�a?

E se nosso sistema crescer e novos tipos de conta forem criadas?

E se a l�gica de c�lculo do rendimento precisar ser utilizada em outro lugar?

O uso demasiado de if e else normalmente tamb�m � um sinal que n�o estamos utilizando o paradigma OO corretamente.

Obviamente, parece n�o ser a melhor forma de resolver nosso problema.

**Solu��o Utilizando Template Method**

Seguindo o conceito do Template Method, vamos criar subclasses que extendam o mesmo comportamento mas com uma parte de sua implementa��o diferente
para cada tipo de conta. 

Para isso, vamos modificar a classe Conta para ser abstrata e definir nela o comportamento padr�o de realiza��o de DOC, mas uma parte desse m�todo ser� 
um m�todo abstrato que cada filha da classe conta (Conta Corrente, Conta Universit�ria e Conta Poupan�a) ficar� encarregada de preencher sua lacuna.

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
            System.out.println("N�o � possivel realizar DOC");
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
        System.out.println("Saldo de Conta Poupan�a deve ser igual a: 950, valor real: " + contaPoupancaTL.getSaldo());
        System.out.println("Saldo de Conta Universitaria deve ser igual a: 139.25, valor real: " + contaUniversitariaSC.getSaldo());
    }

}
```
```shell script
/home/lucasfernandes/.sdkman/candidates/java/15.0.1-open/bin/java -javaagent:/snap/intellij-idea-community/244/lib/idea_rt.jar=38985:/snap/intellij-idea-community/244/bin -Dfile.encoding=ISO-8859-1 -classpath /home/lucasfernandes/git/pessoal-poc/artigos-meetups/out/production/artigos-meetups br.com.iteris.templatemethod.Main
Saldo de Conta Corrente deve ser igual a: 455, valor real: 455.0
Saldo de Conta Poupan�a deve ser igual a: 950, valor real: 950.0
Saldo de Conta Universitaria deve ser igual a: 139.25, valor real: 139.25

Process finished with exit code 0
```

Ganhos com Template Method

// TODO