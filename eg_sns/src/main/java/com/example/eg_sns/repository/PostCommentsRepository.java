package com.example.eg_sns.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

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
	
	/**
	 * コメントの削除処理を行う。
	 * ※物理削除（データが完全に消える。）
	 *
	 * @param id コメントID
	 * @param usersId ユーザーID
	 * @param postsId トピックID
	 */
	@Transactional
	void deleteByIdAndUsersIdAndPostsId(Long id, Long usersId, Long postsId);
	
}
