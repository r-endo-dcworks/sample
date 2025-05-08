package com.example.eg_sns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity. PostComments;

/**
 * トピック関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
/**
 * 投稿画像関連リポジトリインターフェース。
 */
public interface PostCommentsRepository
		extends PagingAndSortingRepository< PostComments, Long>, CrudRepository< PostComments, Long> {
}
