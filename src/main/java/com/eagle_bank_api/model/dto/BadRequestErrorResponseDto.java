package com.eagle_bank_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BadRequestErrorResponseDto {

    @Data
    @AllArgsConstructor
    public static class Detail {
        private String field;
        private String message;
        private String type;
    }

    private String message;
    private List<Detail> details;
}
