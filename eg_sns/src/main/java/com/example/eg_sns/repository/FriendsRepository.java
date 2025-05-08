package com.example.eg_sns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eg_sns.entity. Friends;

/**
 * トピック関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
/**
 * 投稿画像関連リポジトリインターフェース。
 */

	/**
	 * フレンド検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return フレンド情報を返す。
	 */
	@Repository
	public interface FriendsRepository extends JpaRepository<Friends, Long> {

	    @Query("SELECT f FROM Friends f WHERE (f.usersId = :id1 AND f.friendId = :id2) OR (f.usersId = :id2 AND f.friendId = :id1)")
	    Optional<Friends> findFriendRelationship(@Param("id1") Long id1, @Param("id2") Long id2);
	    //フレンド側、ユーザー側のどちらかが、申請を出していてもフレンド関係があるかどうかを調べる
	    
	   Optional<Friends> findByUsersIdAndFriendId(Long userId, Long friendId);

	    
	}



