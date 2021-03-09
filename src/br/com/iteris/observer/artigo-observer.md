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

O objeto que tem o estado que deseja ser observado é normalmente chamado de sujeito (_subject_), mas já que ele também vai notificar outros objetos sobre as mudanças em seu estado, 
podemos chama-lo de Publicador (_Publisher_). Todos os outros objetos que querem saber das mudanças do estado do publicador são chamados de assinantes (_Subscriber_).

Como todos os assinantes precisa possuir a mesma a assinatura de método para usar o polimorfismo, usamos uma interface para realizar a execução dos assinantes.    

Vale lembrar que nesse padrão de projeto, a ordem em que os assinantes executam suas operações é um fator arbitrário, ou seja, 
podem ser executados em qualquer ordem. 

A estrutura deste padrão pode ser visualizada pelo seguinte diagrama de classes:
<br>
![](https://refactoring.guru/images/patterns/diagrams/observer/structure.png)

**Apresentação do Problema**

Para utilizar o padrão Observer, vamos supor um problema do mundo real. Imagine que, em uma aplicação de administração de banco, 
temos algumas funcionalidades que ficão disponíveis para os correntistas do banco dependendo da quantidade de saldo que tem na sua conta.

Regra: 

Após alteração no saldo da conta, sistema do banco deve fazer:

- Emitir Extrato de Alteração:
    - Toda alteração no saldo da conta deve emitir um extrato de alteração informando o saldo após a alteração.
- Verificação de análise de perfil para oferta de empréstimo
    - Após alteração do saldo da conta, realizar a análise de perfil para envio de oferta de empréstimo.
    
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
        // simulação de emissão de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }

}
```
```java
public class OfertaEmprestimo {

    public void notificarOfertaEmprestimo(Conta conta) {
        // simulação de envio de análise de perfil para oferta de empréstimo
        System.out.println("Enviar notificação de análise de perfil para oferta de empréstimo");
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
Enviar notificação de análise de perfil para oferta de empréstimo

Conta de Tywin Lannister
Saldo da conta = 8000.0
Enviar notificação de análise de perfil para oferta de empréstimo


Process finished with exit code 0
```
Vemos que a solução apresentada funciona, mas essa é uma solução de fácil manutenção? Orientada a mudança? 

E se novas ações após a alteração do saldo forem criadas? Iremos sempre instanciar uma classe a mais e manualmente invocarmos a execução da sua lógica?

E se a lógica de ação após alteração do saldo forem utilizadas em outros lugares? Vamos replicar esse bloco de código de chamadas em todos os pontos aonde o saldo for alterado?

Obviamente, parece não ser a melhor forma de resolver nosso problema.

**Solução Utilizando Observer**

Seguindo o conceito do Observer, como na nossa regra toda alteração de saldo sempre vai executar as ações dos assinantes o próprio objeto Conta terá a lista de ações após alteração de saldo
e ele será responsável por registrar os assinantes que ficarão observando a alteração do saldo. O objeto Conta não se preocupa mais em instanciar e chamar a execução de cada assinante, por meio do uso do polimorfismo cada um
dentro do loop da lista executará sua própria ação. 

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
        // simulação de envio de análise de perfil para oferta de empréstimo
        System.out.println("Enviar notificação de análise de perfil para oferta de empréstimo");
    }
}
```
```java
public class ExtratoConta implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        // simulação de emissão de extrato
        System.out.println("Saldo da conta = " + conta.getSaldo());
    }
}
```
Como teste podemos rodar em um _Main_ de exemplo:
```shell script
Conta de Aegon Targeryan
Saldo da conta = 11000.0
Enviar notificação de análise de perfil para oferta de empréstimo

Conta de Tywin Lannister
Saldo da conta = 8000.0
Enviar notificação de análise de perfil para oferta de empréstimo


Process finished with exit code 0
```

**Ganhos com Observer**

Com o uso do Observer, caso uma nova regra que tenha que ser executada após a alteração de saldo for criada, não precisamos mais
procurar no nosso código os pontos aonde essa lógica tem que ser adicionada. Caso outro serviço ou outro classe realize
alguma alteração no saldo e precise executar as regras, não precisamos realizar um _copy and paste_ do bloco de código da classe Conta, 
basta adicionarmos a lista de assinantes e o polimorfismo se encarregará do resto. Respeitando assim o princípio _Open Closed_. 

Dessa forma, diminuimos o acoplamento das nossas classes e tornamos o nosso código. 
Outro ganho importante nessa abordagem é que agora não dependemos mais de classes concretas e sim de uma coleção do tipo interface que é uma estrutura mais estável,
garantido assim o princípio da inversão de dependência.

**Para Saber Mais**

Seguindo o mesmo modelo e regras do problema apresentado anteriormente, segue uma solução mais próxima da realidade de desenvolvimento do mundo real. 
Apresentaremos uma solução rodando uma aplicação utilizando Spring Boot e algumas boas práticas e outras bibliotecas maduras e bastante utilizadas.
Agora que temos uma solução aonde delegamos a criação e gerencimanento dos nossos objetos para o contexto da aplicação, _Application Context_, podemos 
ursufruir de algumas funcionalidades como por exemplo _CDI (Contexts Dependency Injection)_. 

Segue um modelo de exemplo com uma aplicação Spring Boot:

```java
// anotações Lombok
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
        // Exception Genérica
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
@Slf4j /* anotação que permite utilizar variavel "log" */
@Service /* anotação indicando que essa classe é um Bean do tipo Service gerenciada pelo container Spring */
public class AnaliseOfertaEmprestimoService implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        log.info("Conta: {}, Enviar notificação de análise de perfil para oferta de empréstimo", conta.getNome());
    }
}
```
```java
@Slf4j /* anotação que permite utilizar variavel "log" */
@Service /* anotação indicando que essa classe é um Bean do tipo Service gerenciada pelo container Spring */
public class ExtratoFinanceiroService implements AcaoAposAlteracaoSaldo {

    @Override
    public void executaAcao(Conta conta) {
        log.info("Conta: {} possui o saldo atual de: {}", conta.getNome(), conta.getSaldo());
    }
}
```
```java
// anotação indicando que essa classe é um Bean do tipo Service gerenciada pelo container Spring
@Service
public class ContaService {

    // anotação responsável por fazer a injeção de todas as classes que implementam a interface AcaoAposAlteracaoSaldo
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
/* Startavel da aplicação */
@SpringBootApplication
public class Starter {

	public static void main(String[] args) {
		SpringApplication.run(Starter.class, args);
	}

}
```
```java
/* anotação que indica ao container Spring que essa classe é um Bean gerenciado. 
Ao implementar a interface CommandLineRunner, informa a aplicação que esse código será invocado após a aplicação ser startada.
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
Saida de resultado no log da aplicação:
```shell script
2021-03-06 21:03:31.993  INFO 7084 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2021-03-06 21:03:32.003  INFO 7084 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-03-06 21:03:32.003  INFO 7084 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.43]
2021-03-06 21:03:32.051  INFO 7084 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-03-06 21:03:32.052  INFO 7084 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 871 ms
2021-03-06 21:03:32.223  INFO 7084 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2021-03-06 21:03:32.364  INFO 7084 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2021-03-06 21:03:32.375  INFO 7084 --- [           main] br.com.iteris.Starter                    : Started Starter in 1.645 seconds (JVM running for 2.066)
2021-03-06 21:03:32.378  INFO 7084 --- [           main] b.c.i.s.AnaliseOfertaEmprestimoService   : Conta: Aegon Targeryan, Enviar notificação de análise de perfil para oferta de empréstimo
2021-03-06 21:03:32.379  INFO 7084 --- [           main] b.c.i.service.ExtratoFinanceiroService   : Conta: Aegon Targeryan possui o saldo atual de: 3000.0
2021-03-06 21:03:32.380  INFO 7084 --- [           main] b.c.i.s.AnaliseOfertaEmprestimoService   : Conta: Jaime Lannister, Enviar notificação de análise de perfil para oferta de empréstimo
2021-03-06 21:03:32.380  INFO 7084 --- [           main] b.c.i.service.ExtratoFinanceiroService   : Conta: Jaime Lannister possui o saldo atual de: 11000.0
```
