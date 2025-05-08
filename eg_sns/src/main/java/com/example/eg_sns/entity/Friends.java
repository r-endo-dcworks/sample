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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * フレンドEntityクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "friends")
public class Friends extends EntityBase {

	/** ID */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** ユーザーID */
	@Column(name = "users_Id", nullable = false)
	private Long usersId;


	/** フレンドID */
	@Column(name = "friend_users_Id", nullable = false)
	private Long friendId;

	/** フレンドステータス */
	@Column(name = "approval_status", nullable = false)
	private Integer friendStatus;
	
	/** ユーザー情報の紐づけ */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users users;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_users_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Users friendUser;

}
