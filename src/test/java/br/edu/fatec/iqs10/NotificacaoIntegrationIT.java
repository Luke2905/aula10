package br.edu.fatec.iqs10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração com TestContainers (Desafio Integrado - Aula 10)
 * validando os serviços da Aula 09 integrados a um container PostgreSQL real.
 */
@Testcontainers
class NotificacaoIntegrationIT {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("iqs10_db")
            .withUsername("iqs_user")
            .withPassword("iqs_secret");

    @Test
    void deveIntegrarServicosComBancoEmContainer() throws SQLException {
        assertTrue(postgres.isRunning());

        LoginService loginService = new LoginService();
        HeaderUtil headerUtil = new HeaderUtil();
        NotificacaoService notificacaoService = new NotificacaoService();
        Calculadora calculadora = new Calculadora();

        assertTrue(loginService.validarSenha("SenhaForte123"));
        String header = headerUtil.normalizar("  Bearer token-ci  ");
        String confirmacao = notificacaoService.enviarEmail("aluno@fatec.sp.gov.br");
        int tentativas = calculadora.divide(10, 2);

        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
             Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE notificacoes_log ("
                            + "id SERIAL PRIMARY KEY, "
                            + "header VARCHAR(100), "
                            + "confirmacao VARCHAR(255), "
                            + "tentativas INT)");

            try (PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO notificacoes_log (header, confirmacao, tentativas) VALUES (?, ?, ?)")) {
                insert.setString(1, header);
                insert.setString(2, confirmacao);
                insert.setInt(3, tentativas);
                assertEquals(1, insert.executeUpdate());
            }

            try (ResultSet rs = statement.executeQuery(
                    "SELECT header, confirmacao, tentativas FROM notificacoes_log")) {
                assertTrue(rs.next());
                assertEquals("Bearer token-ci", rs.getString("header"));
                assertEquals("Email enviado para: aluno@fatec.sp.gov.br (simulação)", rs.getString("confirmacao"));
                assertEquals(5, rs.getInt("tentativas"));
            }
        }
    }
}
