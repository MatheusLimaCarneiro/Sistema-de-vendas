package estudo.jpa.spring.exception;

public class SenhaInvalidaException extends RuntimeException{
    public SenhaInvalidaException() {
        super("Senha Invalida");
    }
}
