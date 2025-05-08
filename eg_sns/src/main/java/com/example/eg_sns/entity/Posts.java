package com.example.eg_sns.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * トピックEntityクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "posts")
public class Posts extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ユーザーID */
	@Column(name = "users_Id", nullable = false)
	private Long usersId;

	/** タイトル */
	@Column(name = "title", nullable = false)
	private String title;

	/** 本文 */
	@Column(name = "body", nullable = false)
	private String body;

	
	
	/** コメント情報の紐づけ */
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "posts_id", referencedColumnName = "id", insertable = false, updatable = false)
	private List<PostComments> postCommentsList;

	   /** 投稿画像情報の紐づけ（一方向リレーション） */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<PostImages> postImagesList;

	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
}
