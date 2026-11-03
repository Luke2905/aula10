package br.edu.fatec.iqs10;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculadoraTest {

    private final Calculadora calculadora = new Calculadora();

    @ParameterizedTest
    @CsvSource({"10, 2, 5", "9, 2, 4", "-9, 2, -4", "9, -2, -4", "-9, -2, 4", "0, 5, 0"})
    void deveDividirInteiros(int a, int b, int esperado) {
        assertEquals(esperado, calculadora.divide(a, b));
    }

    @Test
    void deveRejeitarDivisaoPorZeroComMensagemEsperada() {
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculadora.divide(10, 0));
        assertEquals("Divisor não pode ser zero", exception.getMessage());
    }
}
