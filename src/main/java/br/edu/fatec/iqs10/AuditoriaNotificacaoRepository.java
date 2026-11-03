package br.edu.fatec.iqs10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repositório JDBC de auditoria de notificações validado com TestContainers (PostgreSQL)
 * no estágio de testes de integração do pipeline CI/CD.
 */
public class AuditoriaNotificacaoRepository {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS auditoria_notificacoes ("
                    + "id SERIAL PRIMARY KEY, "
                    + "destinatario VARCHAR(255) NOT NULL, "
                    + "mensagem VARCHAR(500) NOT NULL"
                    + ")";

    private static final String SQL_INSERT =
            "INSERT INTO auditoria_notificacoes (destinatario, mensagem) VALUES (?, ?)";

    private static final String SQL_SELECT_ALL =
            "SELECT destinatario FROM auditoria_notificacoes ORDER BY id ASC";

    private final NotificacaoService notificacaoService;

    /**
     * Cria o repositório utilizando o serviço de notificações informado.
     *
     * @param notificacaoService serviço responsável pela validação e envio
     */
    public AuditoriaNotificacaoRepository(NotificacaoService notificacaoService) {
        this.notificacaoService = Objects.requireNonNull(
                notificacaoService, "notificacaoService não pode ser nulo");
    }

    /**
     * Inicializa a tabela de auditoria no banco de dados caso não exista.
     *
     * @param connection conexão JDBC aberta
     * @throws SQLException em caso de falha na criação da tabela
     */
    public void inicializarSchema(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection não pode ser nula");
        try (Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
        }
    }

    /**
     * Valida o destinatário via {@link NotificacaoService} e persiste o registro de auditoria.
     *
     * @param connection conexão JDBC aberta
     * @param email destinatário da notificação
     * @return confirmação retornada pelo serviço de notificações
     * @throws SQLException em caso de erro de persistência
     */
    public String registrarEnvio(Connection connection, String email) throws SQLException {
        Objects.requireNonNull(connection, "connection não pode ser nula");
        String confirmacao = notificacaoService.enviarEmail(email);
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setString(1, email);
            statement.setString(2, confirmacao);
            statement.executeUpdate();
        }
        return confirmacao;
    }

    /**
     * Lista todos os destinatários persistidos em ordem de inserção.
     *
     * @param connection conexão JDBC aberta
     * @return lista imutável ou mutável com os destinatários registrados
     * @throws SQLException em caso de erro de consulta
     */
    public List<String> listarDestinatarios(Connection connection) throws SQLException {
        Objects.requireNonNull(connection, "connection não pode ser nula");
        List<String> destinatarios = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                destinatarios.add(resultSet.getString("destinatario"));
            }
        }
        return destinatarios;
    }
}
