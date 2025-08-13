package com.example.social_platform.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlbumRequest {
    @NotNull
    private Long userId;

    @NotNull
    String title;

    @NotNull
    String description;
}
