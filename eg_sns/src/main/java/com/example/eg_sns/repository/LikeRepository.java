package com.example.eg_sns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eg_sns.entity.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
	
	Optional<Like> findByPostsIdAndUsersId(Long postsId, Long usersId);//重複いいね防止
	int countByPostsId(Long postsId);//いいね数集計
	
}
