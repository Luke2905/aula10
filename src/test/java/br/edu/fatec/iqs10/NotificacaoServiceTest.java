package br.edu.fatec.iqs10;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificacaoServiceTest {

    private final NotificacaoService notificacoes = new NotificacaoService();

    @ParameterizedTest
    @ValueSource(strings = {
            "aluno@fatec.sp.gov.br", "lucas.silva@example.com", "aluno+tag@example.com",
            "ALUNO@EXAMPLE.COM", "aluno_ti@sub-dominio.example.com"
    })
    void deveSimularEnvioParaEmailValido(String email) {
        assertEquals("Email enviado para: " + email + " (simulação)",
                notificacoes.enviarEmail(email));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", "email-invalido", "aluno@", "@example.com", "aluno@example",
            "aluno@@example.com", " aluno@example.com", "aluno@example.com ",
            "aluno teste@example.com", "aluno..teste@example.com", ".aluno@example.com",
            "aluno.@example.com", "aluno@-example.com", "aluno@example-.com",
            "aluno@example..com", "aluno@example.c", "aluno@example.com\n"
    })
    void deveRejeitarEmailNuloVazioOuComFormatoInvalido(String email) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> notificacoes.enviarEmail(email));
        assertEquals("Email inválido", exception.getMessage());
    }
}
