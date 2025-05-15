package com.example.eg_sns.service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eg_sns.entity.PostComments;
import com.example.eg_sns.repository.PostCommentsRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CommentsService {
	/** リポジトリインターフェース。 */


	@Autowired
	private PostCommentsRepository postCommentsRepository; 
	
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
	/**
	 * コメントの削除処理を行う。
	 *
	 * @param id コメントID
	 * @param usersId ユーザーID
	 * @param postsId トピックID
	 */
	public void deleteComments(Long id, Long usersId, Long postsId) {
		log.info("コメントを削除します。：id={}, usersId={}, topicsId={}", id, usersId, postsId);

		postCommentsRepository.deleteByIdAndUsersIdAndPostsId(id, usersId, postsId);
	}

	/**
	 * コメントの削除処理を行う。
	 *
	 * @param commentsList コメントリスト
	 */
	@Transactional
	public void deleteAll(List<PostComments> commentsList) {
	    log.info("CommentServiceクラスに移動しました");

	    List<PostComments> toSave = commentsList.stream()
	            .filter(comment -> comment.getId() == null)
	            .collect(Collectors.toList());

	    if (!toSave.isEmpty()) {
	        postCommentsRepository.saveAll(toSave);  // 保存処理
	        log.info("保存したコメントの件数: {}", toSave.size());
	    } else {
	        log.info("保存するコメントはありません");
	    }
	    
	    List<PostComments> toDelete = commentsList.stream()
	            .filter(comment -> comment.getId() != null)
	            .collect(Collectors.toList());

	        if (!toDelete.isEmpty()) {
	            postCommentsRepository.deleteAll(toDelete);  // 削除処理
	            log.info("コメントを削除しました。件数: {}", toDelete.size());
	        } else {
	            log.info("削除対象のコメントはありません");
	        }
	}

}
