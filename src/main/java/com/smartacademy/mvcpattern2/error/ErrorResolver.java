package com.smartacademy.mvcpattern2.error;

import com.smartacademy.mvcpattern2.controller.ControllerImpl;
import com.smartacademy.mvcpattern2.model.errormodel.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorResolver {

    private Logger log = LoggerFactory.getLogger(ControllerImpl.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                      HttpServletResponse servletResponse) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorDescription("Maybe try the right method like PUT if it is PUT, ¯\\_(ツ)_/¯ , i'm not a programmer yet");
        servletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        log.info("Error HttpRequestMethodNotSupportedException found");
        log.warn("Error has been found returning status: " + HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                      HttpServletResponse servletResponse) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorDescription("Argument not valid ლ(ಠ益ಠლ) ");
        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.info("Error MethodArgumentNotValidException found");
        log.warn("Error has been found returning status: " + HttpServletResponse.SC_BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
                                                                      HttpServletResponse servletResponse) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorDescription("The body is null or wrong written, please check again (づ￣ ³￣)づ ");
        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        log.info("Error HttpMessageNotReadableException found");
        log.warn("Error has been found returning status: " + HttpServletResponse.SC_BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse handleNoSuchElementException(NoSuchElementException exception,
                                                               HttpServletResponse servletResponse) {
        ErrorResponse response = new ErrorResponse();
        response.setErrorDescription("Choose at least one of customerID or phoneNumber ಠ╭╮ಠ ");
        servletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        log.info("Error NoSuchElementException found");
        log.warn("Error has been found returning status: " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return response;
    }
}
