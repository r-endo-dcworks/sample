package com.example.eg_sns.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ユーザーEntityクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users")
public class Users extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ログインID */
	@Column(name = "login_id", nullable = false)
	private String loginId;

	/** パスワード */
	@Column(name = "password", nullable = false)
	private String password;

	/** 名前 */
	@Column(name = "name", nullable = false)
	private String name;

	/** mail */
	@Column(name = "email_address", nullable = false)
	private String emailAddress;
	
	/** プロフィールアイコンURI */
	@Column(name = "icon_uri", nullable = true)
	private String iconUri;
	
	/** プロフィール自己紹介 */
	@Column(name = "profile", nullable = true)
	private String profile;
	

	
}

