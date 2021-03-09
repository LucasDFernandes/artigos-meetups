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

Como todos os assinantes precisa possuir a mesma a assinatura de m�todo para usar o polimorfismo, usamos uma interface para realizar a execu��o dos assinantes.    

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

Seguindo o conceito do Observer, como na nossa regra toda altera��o de saldo sempre vai executar as a��es dos assinantes o pr�prio objeto Conta ter� a lista de a��es ap�s altera��o de saldo
e ele ser� respons�vel por registrar os assinantes que ficar�o observando a altera��o do saldo. O objeto Conta n�o se preocupa mais em instanciar e chamar a execu��o de cada assinante, por meio do uso do polimorfismo cada um
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

**Ganhos com Observer**

Com o uso do Observer, caso uma nova regra que tenha que ser executada ap�s a altera��o de saldo for criada, n�o precisamos mais
procurar no nosso c�digo os pontos aonde essa l�gica tem que ser adicionada. Caso outro servi�o ou outro classe realize
alguma altera��o no saldo e precise executar as regras, n�o precisamos realizar um _copy and paste_ do bloco de c�digo da classe Conta, 
basta adicionarmos a lista de assinantes e o polimorfismo se encarregar� do resto. Respeitando assim o princ�pio _Open Closed_. 

Dessa forma, diminuimos o acoplamento das nossas classes e tornamos o nosso c�digo. 
Outro ganho importante nessa abordagem � que agora n�o dependemos mais de classes concretas e sim de uma cole��o do tipo interface que � uma estrutura mais est�vel,
garantido assim o princ�pio da invers�o de depend�ncia.

**Para Saber Mais**

Seguindo o mesmo modelo e regras do problema apresentado anteriormente, segue uma solu��o mais pr�xima da realidade de desenvolvimento do mundo real. 
Apresentaremos uma solu��o rodando uma aplica��o utilizando Spring Boot e algumas boas pr�ticas e outras bibliotecas maduras e bastante utilizadas.
Agora que temos uma solu��o aonde delegamos a cria��o e gerencimanento dos nossos objetos para o contexto da aplica��o, _Application Context_, podemos 
ursufruir de algumas funcionalidades como por exemplo _CDI (Contexts Dependency Injection)_. 

Segue um modelo de exemplo com uma aplica��o Spring Boot:

```java
// anota��es Lombok
@Data
@AllArgsConstructor
public class Conta {

    private String nome;
    private double saldo;

    public double saca(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
            return valor;
        }
        // Exception Gen�rica
        throw new RuntimeException("");
    }

    public void deposita(double valor) {
        saldo += valor;
    }
}
``` 
```java
public interface AcaoAposAlteracaoSaldo {

    void executaAcao(Conta conta);
}
```
```java
@Slf4j /* anota��o que permite utilizar variavel "log" */
@Service /* anota��o indicando que essa classe � um Bean do tipo Service gerenciada pelo container Spring */
public class AnaliseOfertaEmprestimoService implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        log.info("Conta: {}, Enviar notifica��o de an�lise de perfil para oferta de empr�stimo", conta.getNome());
    }
}
```
```java
@Slf4j /* anota��o que permite utilizar variavel "log" */
@Service /* anota��o indicando que essa classe � um Bean do tipo Service gerenciada pelo container Spring */
public class ExtratoFinanceiroService implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        log.info("Conta: {} possui o saldo atual de: {}", conta.getNome(), conta.getSaldo());
    }
}
```
```java
// anota��o indicando que essa classe � um Bean do tipo Service gerenciada pelo container Spring
@Service
public class ContaService {

    // anota��o respons�vel por fazer a inje��o de todas as classes que implementam a interface AcaoAposAlteracaoSaldo
    @Autowired
    private List<AcaoAposAlteracaoSaldo> listaAcoes;

    public double realizaSaque(Conta conta, double valor) {
        double saque = conta.saca(valor);
        listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(conta));
        return saque;
    }

    public void realizaDeposito(Conta conta, double valor) {
        conta.deposita(valor);
        listaAcoes.forEach(acaoAposAlteracaoSaldo -> acaoAposAlteracaoSaldo.executaAcao(conta));
    }


}
```
```java
/* Startavel da aplica��o */
@SpringBootApplication
public class Starter {

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

}
```
```java
/* anota��o que indica ao container Spring que essa classe � um Bean gerenciado. 
Ao implementar a interface CommandLineRunner, informa a aplica��o que esse c�digo ser� invocado ap�s a aplica��o ser startada.
*/
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private ContaService contaService;

    @Override
    public void run(String... args) throws Exception {
        Conta aegonTargeryan = new Conta("Aegon Targeryan", 5000);
        Conta jaimeLannister = new Conta("Jaime Lannister", 5000);

        contaService.realizaSaque(aegonTargeryan, 2000);
        contaService.realizaDeposito(jaimeLannister, 6000);
    }
}
```
Saida de resultado no log da aplica��o:
```shell script
2021-03-06 21:03:31.993  INFO 7084 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-03-06 21:03:32.003  INFO 7084 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-03-06 21:03:32.003  INFO 7084 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.43]
2021-03-06 21:03:32.051  INFO 7084 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-03-06 21:03:32.052  INFO 7084 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 871 ms
2021-03-06 21:03:32.223  INFO 7084 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-03-06 21:03:32.364  INFO 7084 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-03-06 21:03:32.375  INFO 7084 --- [           main] br.com.iteris.Starter                    : Started Starter in 1.645 seconds (JVM running for 2.066)
2021-03-06 21:03:32.378  INFO 7084 --- [           main] b.c.i.s.AnaliseOfertaEmprestimoService   : Conta: Aegon Targeryan, Enviar notifica��o de an�lise de perfil para oferta de empr�stimo
2021-03-06 21:03:32.379  INFO 7084 --- [           main] b.c.i.service.ExtratoFinanceiroService   : Conta: Aegon Targeryan possui o saldo atual de: 3000.0
2021-03-06 21:03:32.380  INFO 7084 --- [           main] b.c.i.s.AnaliseOfertaEmprestimoService   : Conta: Jaime Lannister, Enviar notifica��o de an�lise de perfil para oferta de empr�stimo
2021-03-06 21:03:32.380  INFO 7084 --- [           main] b.c.i.service.ExtratoFinanceiroService   : Conta: Jaime Lannister possui o saldo atual de: 11000.0
```
