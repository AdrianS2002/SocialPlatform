package com.example.social_platform.service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Album implements Serializable {
    Long id;
    Long userId;
    String title;
    String description;
}
