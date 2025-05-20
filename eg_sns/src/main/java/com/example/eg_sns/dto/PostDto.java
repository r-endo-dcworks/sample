package com.example.eg_sns.dto;

import com.example.eg_sns.entity.Posts;

public class PostDto {
    private Posts post;
    private int likeCount;
    private boolean liked;

    public PostDto(Posts post, int likeCount, boolean liked) {
        this.post = post;
        this.likeCount = likeCount;
        this.liked = liked;
    }

    public Posts getPost() { return post; }
    public int getLikeCount() { return likeCount; }
    public boolean isLiked() { return liked; }
}


