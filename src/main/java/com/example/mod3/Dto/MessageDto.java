package com.example.mod3.Dto;

import lombok.*;


@Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public class MessageDto {
        private String email;
        private String operation;
    }

