package com.example.shoestore.core.common.dto.response;

import com.example.shoestore.infrastructure.constants.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactory {

    public static ResponseEntity<Object> data(ResponseDTO responseDTO) {
        return ResponseEntity.ok(responseDTO);
    }

    public static ResponseEntity<Object> error(String message) {
        ResponseDTO response = new ResponseDTO();
        response.setCode(ResponseCode.ERROR);
        response.setMessage(message);
        return ResponseEntity.ok(response);
    }

}
