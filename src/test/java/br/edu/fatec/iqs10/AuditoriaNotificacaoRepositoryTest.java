package br.edu.fatec.iqs10;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditoriaNotificacaoRepositoryTest {

    @Test
    void deveRejeitarDependenciaNulaNoConstrutor() {
        assertThrows(NullPointerException.class, () -> new AuditoriaNotificacaoRepository(null));
    }

    @Test
    void deveInicializarRegistrarEListarEmMemoriaViaJdbc() throws SQLException {
        AtomicBoolean schemaCriado = new AtomicBoolean(false);
        List<String> registros = new ArrayList<>();
        Connection fakeConnection = criarConexaoEmMemoria(schemaCriado, registros);

        AuditoriaNotificacaoRepository repository =
                new AuditoriaNotificacaoRepository(new NotificacaoService());

        repository.inicializarSchema(fakeConnection);
        assertTrue(schemaCriado.get());

        String confirmacao = repository.registrarEnvio(fakeConnection, "aluno@fatec.sp.gov.br");
        assertEquals("Email enviado para: aluno@fatec.sp.gov.br (simulação)", confirmacao);

        repository.registrarEnvio(fakeConnection, "devops@example.com");

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.registrarEnvio(fakeConnection, "email-invalido"));

        List<String> destinatarios = repository.listarDestinatarios(fakeConnection);
        assertEquals(List.of("aluno@fatec.sp.gov.br", "devops@example.com"), destinatarios);
    }

    @Test
    void deveValidarConexaoNulaNosMetodos() {
        AuditoriaNotificacaoRepository repository =
                new AuditoriaNotificacaoRepository(new NotificacaoService());
        assertThrows(NullPointerException.class, () -> repository.inicializarSchema(null));
        assertThrows(
                NullPointerException.class,
                () -> repository.registrarEnvio(null, "aluno@fatec.sp.gov.br"));
        assertThrows(NullPointerException.class, () -> repository.listarDestinatarios(null));
    }

    private static Connection criarConexaoEmMemoria(
            AtomicBoolean schemaCriado, List<String> registros) {
        ClassLoader loader = AuditoriaNotificacaoRepositoryTest.class.getClassLoader();

        Statement statement = (Statement) Proxy.newProxyInstance(
                loader,
                new Class<?>[]{Statement.class},
                (proxy, method, args) -> {
                    if ("execute".equals(method.getName())) {
                        schemaCriado.set(true);
                        return true;
                    }
                    if ("close".equals(method.getName())) {
                        return null;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });

        return (Connection) Proxy.newProxyInstance(
                loader,
                new Class<?>[]{Connection.class},
                (proxy, method, args) -> {
                    if ("createStatement".equals(method.getName())) {
                        return statement;
                    }
                    if ("prepareStatement".equals(method.getName())) {
                        String sql = (String) args[0];
                        return criarPreparedStatement(loader, sql, registros);
                    }
                    if ("close".equals(method.getName())) {
                        return null;
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
    }

    private static PreparedStatement criarPreparedStatement(
            ClassLoader loader, String sql, List<String> registros) {
        final String[] ultimoDestinatario = new String[1];
        return (PreparedStatement) Proxy.newProxyInstance(
                loader,
                new Class<?>[]{PreparedStatement.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "setString":
                            if ((Integer) args[0] == 1) {
                                ultimoDestinatario[0] = (String) args[1];
                            }
                            return null;
                        case "executeUpdate":
                            if (sql.startsWith("INSERT")) {
                                registros.add(ultimoDestinatario[0]);
                            }
                            return 1;
                        case "executeQuery":
                            return criarResultSet(loader, new ArrayList<>(registros).iterator());
                        case "close":
                            return null;
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }

    private static ResultSet criarResultSet(ClassLoader loader, Iterator<String> iterator) {
        final String[] atual = new String[1];
        return (ResultSet) Proxy.newProxyInstance(
                loader,
                new Class<?>[]{ResultSet.class},
                (proxy, method, args) -> {
                    switch (method.getName()) {
                        case "next":
                            if (iterator.hasNext()) {
                                atual[0] = iterator.next();
                                return true;
                            }
                            return false;
                        case "getString":
                            return atual[0];
                        case "close":
                            return null;
                        default:
                            throw new UnsupportedOperationException(method.getName());
                    }
                });
    }
}
