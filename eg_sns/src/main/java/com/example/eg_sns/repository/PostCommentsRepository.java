package com.example.eg_sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eg_sns.entity. PostComments;
/**
 * トピック関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
/**
 * 投稿画像関連リポジトリインターフェース。
 */
public interface PostCommentsRepository extends JpaRepository<PostComments, Long> {

	
}
