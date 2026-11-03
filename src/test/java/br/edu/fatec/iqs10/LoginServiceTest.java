package br.edu.fatec.iqs10;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginServiceTest {

    private final LoginService login = new LoginService();

    @ParameterizedTest
    @ValueSource(strings = {"Senha123", "senha-com-mais-caracteres"})
    void deveAceitarSenhaComPeloMenosOitoCaracteres(String senha) {
        assertTrue(login.validarSenha(senha));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"1234567", "123", "        ", "\t\n"})
    void deveRejeitarSenhaNulaVaziaCurtaOuSomenteEspacos(String senha) {
        assertFalse(login.validarSenha(senha));
    }
}
