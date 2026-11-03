package br.edu.fatec.iqs10;

/** Utilitário de normalização de cabeçalhos HTTP herdado da Aula 09. */
public class HeaderUtil {

    /** Cria o utilitário. */
    public HeaderUtil() {
    }

    /**
     * Remove espaços externos e trata cabeçalho ausente.
     *
     * @param header valor do cabeçalho, podendo ser nulo
     * @return valor sem espaços externos, ou string vazia se nulo
     */
    public String normalizar(String header) {
        return header == null ? "" : header.trim();
    }
}
