package com.example.shoestore.web.rest.advise;

import com.example.shoestore.core.common.dto.response.ResponseDTO;
import com.example.shoestore.infrastructure.constants.MessageCode;
import com.example.shoestore.infrastructure.exception.ResourceNotFoundException;
import com.example.shoestore.infrastructure.exception.ValidateException;
import com.example.shoestore.infrastructure.utils.MessageUtils;
import com.example.shoestore.sercurity.exception.TokenInvalidException;
import com.example.shoestore.sercurity.exception.UserDetailNotfoundException;
import javassist.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlerAdvise extends ResponseEntityExceptionHandler  {

    //    @ExceptionHandler(Exception.class)
//    public ModelAndView handleException(Exception e) {
//        ModelAndView modelAndView = new ModelAndView("error");
//        Map<String,String> mapErr = new HashMap<>();
//        mapErr.put("title",e.getMessage());
//        mapErr.put("error",MessageUtils.getMessage(MessageCode.Commom.MAINTENANCE));
//        modelAndView.addAllObjects(mapErr);
//        return modelAndView;
//    }

    @ExceptionHandler(UserDetailNotfoundException.class)
    public ResponseEntity<Object> userNotFoundException(UserDetailNotfoundException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        responseDTO.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Object> userNotFoundException(TokenInvalidException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        responseDTO.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<Object> handleValidateException(ValidateException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        responseDTO.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        responseDTO.setMessage(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        responseDTO.setMessage(MessageUtils.getMessage(MessageCode.Commom.NOT_FOUND));
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedExceptionException(AccessDeniedException ex) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.FORBIDDEN.value()));
        responseDTO.setMessage(MessageUtils.getMessage(MessageCode.Accounts.ACCOUNT_ACCESS_DENIED));
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        responseDTO.setMessage(MessageUtils.getMessage(MessageCode.Commom.DATA_TYPE_INVALID));
        responseDTO.setData(ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        BindingResult bindingResult = ex.getBindingResult();

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
        responseDTO.setMessage(MessageUtils.getMessage(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

}
