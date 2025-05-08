package com.example.eg_sns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * アカウントDTOクラス。
 *
 * @author tomo-sato
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RequestModifyPassword extends DtoBase {

	/** 現在のパスワード */
	@NotBlank(message = "現在のパスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String currentPassword;

	/** 新しいパスワード */
	@NotBlank(message = "新しいパスワードを入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String newPassword;

	/** 新しいパスワードを再入力 */
	@NotBlank(message = "新しいパスワードを再入力してください。")
	@Size(max = 32, message = "パスワードは最大32文字です。")
	private String renewPassword;
	
}
