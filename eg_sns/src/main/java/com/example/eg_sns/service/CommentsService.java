package com.example.eg_sns.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eg_sns.dto.ResponseComments;
import com.example.eg_sns.entity.PostComments;
import com.example.eg_sns.entity.Posts;
import com.example.eg_sns.entity.Users;
import com.example.eg_sns.repository.PostCommentsRepository;
import com.example.eg_sns.repository.PostsRepository;
import com.example.eg_sns.repository.UsersRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CommentsService {
	/** リポジトリインターフェース。 */

	@Autowired
	private PostCommentsRepository postCommentsRepository;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private PostsRepository postsRepository;

	/**
	 * コメント投稿処理を行う。
	 *
	 * @param requestShare コメント投稿DTO
	 * @param usersId ユーザーID
	 * @param postImagesUri 投稿画像URI
	 * @return リダイレクト先（home画面）
	 */

	public ResponseComments saveComment(Long postsId, Long usersId, String container) {
		System.out.println("saveCommentに入りました。");
		Users user = usersRepository.findById(usersId)
				.orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
		Posts post = postsRepository.findById(postsId)
				.orElseThrow(() -> new IllegalArgumentException("投稿が見つかりません"));

		PostComments comment = new PostComments();
		comment.setComment(container);
		comment.setUsers(user);
		comment.setPosts(post);
		System.out.println("saveCommentに入りました。" + post);

		PostComments saved = postCommentsRepository.save(comment);
		System.out.println("saveCommentに入りました。3");

		ResponseComments dto = new ResponseComments();
		dto.setId(saved.getId());
		dto.setComment(saved.getComment());
		dto.setUserName(user.getName());
		dto.setUserIconUri(user.getIconUri());
		dto.setUserLoginId(user.getLoginId());
		dto.setPostsId(post.getId());


		return dto;
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
			postCommentsRepository.saveAll(toSave); // 保存処理
		}

		List<PostComments> toDelete = commentsList.stream()
				.filter(comment -> comment.getId() != null)
				.collect(Collectors.toList());

		if (!toDelete.isEmpty()) {
			postCommentsRepository.deleteAll(toDelete); // 削除処理
		}
	}

}
