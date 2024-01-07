package com.example.shoestore.core.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FileResponseDTO {

    private ByteArrayResource byteArrayResource;
    private String fileName;
}
