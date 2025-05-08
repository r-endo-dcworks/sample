package com.example.eg_sns.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.eg_sns.dto.RequestPosts;
import com.example.eg_sns.entity.PostComments;
import com.example.eg_sns.entity.PostImages;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.repository.PostCommentsRepository;
import com.example.eg_sns.repository.PostImagesRepository;
import com.example.eg_sns.repository.PostsRepository;

import lombok.extern.log4j.Log4j2;

/**
 * トピック関連サービスクラス。
 *
 * @author tomo-sato
 */
@Log4j2
@Service
public class PostsService {

	/** リポジトリインターフェース。 */
	@Autowired
	private PostsRepository repository;
	@Autowired
	private PostImagesRepository postImagesRepository; 
	@Autowired
	private PostCommentsRepository postCommentsRepository; 

	/**
	 * トピック全件取得する。
	 *
	 * @return トピックを全件取得する。
	 */
	public List<Posts> findAllPosts() {
		return (List<Posts>) repository.findByOrderByIdDesc();
	}

	/**
	 * トピック検索を行う。
	 * トピックIDと、ログインIDを指定し、トピックを検索する。
	 * @param id トピックID
	 * @return トピック情報を返す。
	 */
	// PostsService.java
	public List<Posts> findPostsByUserId(Long userId) {
		log.info("ユーザーの投稿一覧を取得します。：userId={}", userId);
		return repository.findByUsersId(userId);
	}


	/**
	 * 投稿処理を行う。
	 *
	 * @param requestShare コメント投稿DTO
	 * @param usersId ユーザーID
	 * @param postImagesUri 投稿画像URI
	 * @return リダイレクト先（home画面）
	 */
	public void save(RequestPosts requestPosts, Long usersId, String ImagesUri) {
		log.info("投稿保存処理を行います。：requestPosts={}, usersId={}, ImagesUri={}", requestPosts, usersId,ImagesUri);

		Posts posts = new Posts();
		posts.setUsersId(usersId);
		posts.setTitle(requestPosts.getTitle());
		posts.setBody(requestPosts.getBody());

		// 投稿データの登録及び、取得。
		Posts regPosts = repository.save(posts);
		Long postsId = regPosts.getId();

		if (ImagesUri != null) {
			PostImages postImages = new PostImages();
			postImages.setPostsId(postsId);
			postImages.setUsersId(usersId);
			postImages.setImageUri(ImagesUri);
	        postImagesRepository.save(postImages); 
		}
	}
	
	/**
	 * コメント投稿処理を行う。
	 *
	 * @param requestShare コメント投稿DTO
	 * @param usersId ユーザーID
	 * @param postImagesUri 投稿画像URI
	 * @return リダイレクト先（home画面）
	 */
	public void saveComment(Long postsId, Long usersId, String commentText) {
		log.info("コメント保存処理を行います。：requestPosts={}, usersId={}",usersId);
			PostComments comment = new PostComments();
			comment.setPostsId(postsId);
			comment.setUsersId(usersId);
			comment.setComment(commentText);
			postCommentsRepository.save(comment); 
	}
}
