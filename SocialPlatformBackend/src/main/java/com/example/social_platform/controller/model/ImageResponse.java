package com.example.social_platform.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse{
    private Long id;
    private String filename;
    private String base64Content;
    private LocalDateTime createdAt;
}