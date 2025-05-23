package com.example.eg_sns.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投稿画像Entityクラス。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "post_comments")

public class PostComments extends EntityBase {


	
	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 投稿ID */
	@Column(name = "posts_id", nullable = false)
	private Long postsId;

	/** ユーザーID */
	@Column(name = "users_id", nullable = false)
	private Long usersId;

	/** コメント本文 */
	@Column(name = "comment", nullable = false)
	private String comment;
	
	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference  // 循環参照を防ぐ
    private Posts posts;


}