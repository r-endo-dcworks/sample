package com.example.eg_sns.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.eg_sns.entity.Users;

/**
 * ユーザー関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface UsersRepository extends PagingAndSortingRepository<Users, Long>, CrudRepository<Users, Long> {

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */
	Users findByLoginId(String loginId);

	/**
	 * ユーザー検索を行う。
	 * ログインID、パスワードを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @param password パスワード
	 * @return ユーザー情報を返す。
	 */
	Users findByLoginIdAndPassword(String loginId, String password);
	
	/**
	 * ユーザー一覧を取得する。
	 * ユーザーIDの降順。
	 * @return ユーザー一覧を返す。
	 */
	List<Users> findByOrderByIdDesc();
	
}

