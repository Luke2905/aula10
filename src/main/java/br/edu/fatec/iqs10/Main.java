package br.edu.fatec.iqs10;

/** Aplicação principal de demonstração para a Aula 10 (CI/CD com Automação de Qualidade). */
public class Main {

    /** Cria a aplicação de demonstração. */
    public Main() {
    }

    /**
     * Executa exemplos válidos e inválidos dos componentes do projeto.
     *
     * @param args argumentos da linha de comando, não utilizados
     */
    public static void main(String[] args) {
        System.out.println("Aula 10 - Integração Contínua (CI/CD) com Automação de Qualidade");

        LoginService login = new LoginService();
        System.out.println("Senha com 8 caracteres: " + login.validarSenha("Senha123"));
        System.out.println("Senha curta: " + login.validarSenha("123"));
        System.out.println("Header nulo tratado: [" + new HeaderUtil().normalizar(null) + "]");

        Calculadora calculadora = new Calculadora();
        System.out.println("10 / 2 = " + calculadora.divide(10, 2));
        try {
            calculadora.divide(10, 0);
        } catch (ArithmeticException exception) {
            System.out.println("Divisão inválida: " + exception.getMessage());
        }

        NotificacaoService notificacoes = new NotificacaoService();
        System.out.println(notificacoes.enviarEmail("aluno@fatec.sp.gov.br"));
        try {
            notificacoes.enviarEmail("email-invalido");
        } catch (IllegalArgumentException exception) {
            System.out.println("Notificação rejeitada: " + exception.getMessage());
        }
    }
}
