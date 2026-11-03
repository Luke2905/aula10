package br.edu.fatec.iqs10;

import java.util.regex.Pattern;

/** Serviço de envio simulado de notificações por e-mail herdado da Aula 09. */
public class NotificacaoService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9_+%-]+(?:\\.[A-Za-z0-9_+%-]+)*@"
                    + "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,}$");

    /** Cria o serviço de notificações. */
    public NotificacaoService() {
    }

    /**
     * Valida o formato por regex e simula o envio para o destinatário.
     *
     * @param email destinatário no formato local@dominio.tld
     * @return confirmação do envio simulado
     * @throws IllegalArgumentException se o e-mail for nulo ou inválido
     */
    public String enviarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
        return "Email enviado para: " + email + " (simulação)";
    }
}
