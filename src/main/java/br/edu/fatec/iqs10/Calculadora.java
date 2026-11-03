package br.edu.fatec.iqs10;

/** Exemplo de divisão inteira herdado da Aula 09 e validado no pipeline CI/CD. */
public class Calculadora {

    /** Cria uma calculadora. */
    public Calculadora() {
    }

    /**
     * Divide dois inteiros com validação fail fast.
     *
     * @param a dividendo
     * @param b divisor
     * @return quociente inteiro com truncamento em direção a zero
     * @throws ArithmeticException se o divisor for zero
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Divisor não pode ser zero");
        }
        return a / b;
    }
}
