package com.example.eg_sns.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.entity.Like;
import com.example.eg_sns.repository.LikeRepository;



@Service
public class LikeService {
	
	@Autowired
	private LikeRepository likeRepository;

	/**
	 * いいねをトグルする
	 * @param postsId 投稿ID
	 * @param usersId ユーザーID
	 * @return 最新のいいね数
	 */
	public Map<String, Object> toggleLike(Long postsId, Long usersId) {
	    Optional<Like> existingLike = likeRepository.findByPostsIdAndUsersId(postsId, usersId);

	    boolean liked;
	    if (existingLike.isPresent()) {
	        likeRepository.delete(existingLike.get());
	        liked = false;
	    } else {
	        Like like = new Like();
	        like.setPostsId(postsId);
	        like.setUsersId(usersId);
	        likeRepository.save(like);
	        liked = true;
	    }

	    int likeCount = likeRepository.countByPostsId(postsId);

	    Map<String, Object> result = new HashMap<>();
	    result.put("likeCount", likeCount);
	    result.put("liked", liked);
	    return result;
	}
	
	
	/**
     * 指定した投稿に対するいいね数を返す
     * @param postId 投稿ID
     * @return いいね数
     */
    public int countLikesByPostId(Long postId) {
        return likeRepository.countByPostsId(postId);
    }

    /**
     * ログインユーザーが特定の投稿にいいねしているかどうかを返す
     * @param postId 投稿ID
     * @param userId ユーザーID
     * @return いいね済みならtrue
     */
    public boolean isLikedByUser(Long postId, Long userId) {
        return likeRepository.findByPostsIdAndUsersId(postId, userId).isPresent();
    }


}
