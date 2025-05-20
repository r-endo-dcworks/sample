package com.example.eg_sns.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eg_sns.core.AppNotFoundException;
import com.example.eg_sns.dto.RequestPosts;
import com.example.eg_sns.entity.PostComments;
import com.example.eg_sns.entity.PostImages;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.repository.PostImagesRepository;
import com.example.eg_sns.repository.PostsRepository;
import com.example.eg_sns.util.CollectionUtil;

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
	/** コメント関連サービスクラス。 */
	@Autowired
	private CommentsService commentsService;

	/**
	 * トピック全件取得する。
	 *
	 * @return トピックを全件取得する。
	 */
	public List<Posts> findAllPosts() {
		return (List<Posts>) repository.findByOrderByIdDesc();
	}
	
	
	/**
	 * トピック最新5件取得する。
	 *
	 * @return トピックを５件取得する。
	 */
	public List<Posts> findLatestPosts() {
	    return repository.findTop5ByOrderByIdDesc();
	}

	/**
	 * トピックさらに取得する。
	 *
	 * @return トピックをさらに取得する。
	 */
	public List<Posts> findNextPosts(Long sinceId) {
	    return repository.findTop5ByIdLessThanOrderByIdDesc(sinceId);
	}
	
	/**
	 * トピックの削除処理を行う。
	 *
	 * @param topicsId トピックID
	 * @param usersId ユーザーID
	 */
	@Transactional
	public void deletePosts(Long postsId, Long usersId) {
		log.info("トピックを削除します。：postsId={}, usersId={}", postsId, usersId);

		// 対象のトピックを検索。
		Posts posts = repository.findByIdAndUsersId(postsId, usersId).orElse(null);
		if (posts == null) {
			// データが取得できない場合は不正操作の為エラー。（404エラーとする。）
			throw new AppNotFoundException();
		}

		// トピックにぶら下がってるコメントを削除。
		 List<PostComments> commentsList = posts.getPostCommentsList();
		    if (CollectionUtil.isNotEmpty(commentsList)) {
		        commentsService.deleteAll(commentsList);  // コメント削除
		    }

		// トピックを削除。
		repository.delete(posts);
		log.info("トピックの削除が完了しました");
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
		return repository.findTop5ByUsersIdOrderByIdDesc(userId);
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
	
	
	
	
}
