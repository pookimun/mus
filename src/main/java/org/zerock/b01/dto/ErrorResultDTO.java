package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResultDTO {
    private String code;
    private String message;
}
