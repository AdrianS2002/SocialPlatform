package com.example.social_platform.service.ports.incoming;

import com.example.social_platform.controller.model.FeedPostResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface FeedService {
    @Transactional
    List<FeedPostResponse> getNews(Long currentId);
}
