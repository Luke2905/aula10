package br.edu.fatec.iqs10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração real com TestContainers e PostgreSQL executado no perfil
 * {@code integration-tests} do Desafio Integrado.
 */
@Testcontainers
class AuditoriaNotificacaoPostgresIT {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("iqs10_db")
            .withUsername("iqs_user")
            .withPassword("iqs_secret");

    @Test
    void devePersistirEConsultarNotificacoesNoPostgresContainer() throws SQLException {
        assertTrue(postgres.isRunning());

        AuditoriaNotificacaoRepository repository =
                new AuditoriaNotificacaoRepository(new NotificacaoService());

        try (Connection connection = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            repository.inicializarSchema(connection);

            String msg1 = repository.registrarEnvio(connection, "integracao@fatec.sp.gov.br");
            String msg2 = repository.registrarEnvio(connection, "pipeline@example.org");

            assertTrue(msg1.contains("integracao@fatec.sp.gov.br"));
            assertTrue(msg2.contains("pipeline@example.org"));

            assertThrows(
                    IllegalArgumentException.class,
                    () -> repository.registrarEnvio(connection, "invalido-sem-arroba"));

            List<String> persistidos = repository.listarDestinatarios(connection);
            assertEquals(
                    List.of("integracao@fatec.sp.gov.br", "pipeline@example.org"),
                    persistidos);
        }
    }
}
