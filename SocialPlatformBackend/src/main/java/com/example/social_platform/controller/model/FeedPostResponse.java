package com.example.social_platform.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedPostResponse {
    private String albumTitle;
    private String albumDescription;
    private LocalDateTime postedAt;
    private String postedByName;
    private Long postedById;
    private boolean isFriend;
    private String base64Image;
    private Long imageId;
    private Boolean isBlocked;

}
