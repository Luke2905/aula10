package br.edu.fatec.iqs10;

/** Serviço de validação de senha herdado da Aula 09. */
public class LoginService {

    /** Cria o serviço de validação. */
    public LoginService() {
    }

    /**
     * Verifica se a senha possui ao menos 8 caracteres não brancos.
     *
     * @param senha senha a validar
     * @return verdadeiro se a senha for válida
     */
    public boolean validarSenha(String senha) {
        return senha != null && !senha.isBlank() && senha.length() >= 8;
    }
}
