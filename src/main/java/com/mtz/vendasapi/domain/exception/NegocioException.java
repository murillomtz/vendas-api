package com.mtz.vendasapi.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class NegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus httpStatus;

    public NegocioException(final String mensagem, final HttpStatus httpStatus) {
        super(mensagem);
        this.httpStatus = httpStatus;
    }

}
