package br.edu.fatec.iqs10;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void deveExecutarDemonstracaoCompletaNoConsole() {
        Main instancia = new Main();
        assertNotNull(instancia);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream capturado = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(capturado);
            Main.main(new String[0]);
        } finally {
            System.setOut(originalOut);
        }

        String saida = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(saida.contains("Aula 10"));
        assertTrue(saida.contains("10 / 2 = 5"));
        assertTrue(saida.contains("Divisão inválida: Divisor não pode ser zero"));
        assertTrue(saida.contains("Email enviado para: aluno@fatec.sp.gov.br"));
        assertTrue(saida.contains("Notificação rejeitada: Email inválido"));
    }
}
