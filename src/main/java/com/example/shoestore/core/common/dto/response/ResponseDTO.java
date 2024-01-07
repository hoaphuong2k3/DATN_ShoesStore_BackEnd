package com.example.shoestore.core.common.dto.response;

import com.example.shoestore.infrastructure.constants.ResponseCode;
import com.example.shoestore.infrastructure.constants.ResponseMess;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO {

    private String code;
    private String message;
    private Object data;

    public static ResponseDTO success() {
        ResponseDTO response = new ResponseDTO();
        response.setCode(ResponseCode.SUCCESS);
        response.setMessage(ResponseMess.SUCCESS);
        return response;
    }

    public static ResponseDTO success(String message) {
        ResponseDTO response = new ResponseDTO();
        response.setCode(ResponseCode.SUCCESS);
        response.setMessage(message);
        return response;
    }

    public static ResponseDTO success(Object data) {
        ResponseDTO response = new ResponseDTO();
        response.setCode(ResponseCode.SUCCESS);
        response.setMessage(ResponseMess.SUCCESS);
        response.setData(data);
        return response;
    }

}
