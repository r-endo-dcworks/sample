package com.example.eg_sns.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;


/**
 * 投稿画像Entityクラス。
 */
@Data
@Entity
@Table(name = "post_like", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"posts_id", "users_id"}) // 同じユーザーが同じ投稿に複数いいね不可
	})

public class Like {
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
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime created;
	
	@UpdateTimestamp
	@Column(nullable = false)
	private LocalDateTime updated;

	
	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference  // 循環参照を防ぐ
    private Posts posts;


}
