package com.example.eg_sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.RequestAccount;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.repository.UsersRepository;

import lombok.extern.log4j.Log4j2;

/**
 * ユーザー関連サービスクラス。
 *
 * @author tomo-sato
 */
@Log4j2
@Service
public class UsersService {

	/** リポジトリインターフェース。 */
	@Autowired
	private UsersRepository repository;

	/**
	 * ユーザー検索を行う。
	 * ログインIDを指定し、ユーザーを検索する。
	 * @param loginId ログインID
	 * @return ユーザー情報を返す。
	 */
	public Users findUsers(String loginId) {
		Users users = repository.findByLoginId(loginId);
		return users;
	}

	/**
	 * ユーザー検索を行う。
	 * ログインID、パスワードを指定し、ユーザーを検索する。
	 *
	 * @param loginId ログインID
	 * @param password パスワード
	 * @return ユーザー情報を返す。
	 */
	public Users findUsers(String loginId, String password) {
		Users users = repository.findByLoginIdAndPassword(loginId, password);
		return users;
	}

	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void save(RequestAccount requestAccount) {

		Users users = new Users();
		users.setLoginId(requestAccount.getLoginId());
		users.setPassword(requestAccount.getPassword());
		users.setName(requestAccount.getName());
		users.setEmailAddress(requestAccount.getEmailAddress());
		users.setIconUri("profile-dummy.png");
		users.setProfile("こんにちは");
		repository.save(users);
	}

	/**
	 * ユーザー情報更新処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void update(RequestAccount requestAccount) {
		Users users = repository.findByLoginId(requestAccount.getLoginId());

		users.setLoginId(requestAccount.getLoginId());
		users.setPassword(requestAccount.getPassword());
		users.setProfile(requestAccount.getProfile());
		users.setName(requestAccount.getName());
		users.setEmailAddress(requestAccount.getEmailAddress());
		users.setIconUri(requestAccount.getIconUri());
		repository.save(users);
	}

	/**
	 * パスワード更新処理を行う。
	 *
	 * @param requestModifyPassword ユーザーDTO
	 */
	public void updatePassword(Users users, String newPassword) {
		users.setPassword(newPassword);
		repository.save(users);
	}

	/**
	 * ユーザー登録処理を行う。
	 *
	 * @param requestAccount ユーザーDTO
	 */
	public void save(Users users) {
		repository.save(users);
	}

	/**
	 * ユーザーIDを指定してユーザーを検索する。
	 *
	 * @param id ユーザーID
	 * @return ユーザー情報
	 */
	public Users findById(Long id) {
		return repository.findById(id).orElse(null); 
	}

	/**
	 * ユーザー全件取得する。
	 * @return ユーザーを全件取得する。
	 */
	public List<Users> findAllUsers() {
		return (List<Users>) repository.findByOrderByIdDesc();
	}
}
