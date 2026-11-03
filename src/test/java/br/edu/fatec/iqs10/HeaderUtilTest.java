package br.edu.fatec.iqs10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeaderUtilTest {

    private final HeaderUtil headers = new HeaderUtil();

    @Test
    void deveTratarHeaderNuloSemNullPointerException() {
        assertEquals("", headers.normalizar(null));
    }

    @Test
    void deveRemoverSomenteEspacosExternos() {
        assertEquals("Bearer token", headers.normalizar("  Bearer token  "));
    }

    @Test
    void deveAceitarHeaderVazio() {
        assertEquals("", headers.normalizar(""));
    }
}
