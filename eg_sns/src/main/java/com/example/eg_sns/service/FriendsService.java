package com.example.eg_sns.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.entity.Friends;
import com.example.eg_sns.repository.FriendsRepository;

import lombok.extern.log4j.Log4j2;

/**
 * フレンド関連サービスクラス。
 *
 * @author tomo-sato
 */
@Log4j2
@Service
public class FriendsService {

	@Autowired
	private FriendsRepository friendsRepository;

	/**
	 * ステータス検索を行う。
	 */
	public Optional<Friends> findFriendshipStatus(Long userId1, Long userId2) {
		return friendsRepository.findFriendRelationship(userId1, userId2);
	}

	/**
	 * 友達申請処理を行う。
	 */
	public void applyFriend(Long usersId, Long friendId) {
		Friends newFriend = new Friends();
		newFriend.setUsersId(usersId);
		newFriend.setFriendId(friendId);
		newFriend.setFriendStatus(0); // 0 は「申請中」ステータス
		friendsRepository.save(newFriend);
	}

	/**
	 * 友達承認処理を行う。
	 */
	public void approvalFriend(Long loginUserId, Long requesterId) {
		Optional<Friends> friendOpt = friendsRepository.findByUsersIdAndFriendId(loginUserId,requesterId);
		
		if (friendOpt.isPresent()) {
			Friends friend = friendOpt.get();
			friend.setFriendStatus(1); // 1 = 承認
			friendsRepository.save(friend);
			System.out.println("フレンド申請を承認しました。");
		}
	}

	/**
	 * 友達却下処理を行う。
	 */
	public void rejectedFriend(Long loginUserId, Long requesterId) {
		Optional<Friends> friendOpt = friendsRepository.findByUsersIdAndFriendId(loginUserId,requesterId);

		if (friendOpt.isPresent()) {
		    Friends friend = friendOpt.get();
			friend.setFriendStatus(9); // 9 = 却下
			friendsRepository.save(friend);
			System.out.println("フレンド申請を却下しました。");
		}
	}
	
	/**
	 * 友達関係をブロック（存在すれば更新、なければ新規作成）
	 */
	public void blockFriend(Long usersId, Long targetUserId) {
	    Friends friend = friendsRepository.findByUsersIdAndFriendId(usersId, targetUserId)
	        .or(() -> friendsRepository.findByUsersIdAndFriendId(targetUserId, usersId))
	        .orElseGet(() -> {
	            Friends newFriend = new Friends();
	            newFriend.setUsersId(targetUserId);
	            newFriend.setFriendId(usersId);
	            return newFriend;
	        });

	    friend.setFriendStatus(10); // 10 = ブロック
	    friendsRepository.save(friend);
	    System.out.println("友達関係をブロックしました。");
	}

	
	/**
	 * フレンド関係削除処理を行う。
	 */
	public void deleteFriend(Long loginUserId, Long requesterId) {
		  Friends friend = friendsRepository.findByUsersIdAndFriendId(loginUserId, requesterId)
			        .orElseGet(() -> friendsRepository.findByUsersIdAndFriendId(requesterId, loginUserId).orElse(null));
		  if (friend != null) {
			  friendsRepository.delete(friend);
		    } 
	}
	
	
}
