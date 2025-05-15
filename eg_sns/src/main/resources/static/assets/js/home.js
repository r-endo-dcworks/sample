/**
 * 
 */
function callWebAPI() {

	console.log("ボタンが押されました");

	//前回の最後の投稿のIDの取得
	var button = document.getElementById('btn-more');
	var sinceId = button.getAttribute('data-sinceid');
	console.log("sinceId:" + sinceId);


	// サーバーにリクエストを送る
	fetch('http://localhost:8080/api/getPosts?sinceId=' + sinceId)
		//返ってきたデータを変換
		.then(response => response.json())
		.then(ret => {
			console.log('test');

			//次にデータがあるかを確認
			//読み込む投稿がない場合ボタンを削除
			//ある場合は次回用のsinceIdを取得
			if (!ret.page_info.has_next) {
				var moreSection = document.getElementById('btn-more-section');
				moreSection.remove();
			} else {
				var button = document.getElementById('btn-more');
				button.setAttribute('data-sinceid', ret.page_info.since_id);
			}

			for (let posts of ret.data) {
				const template = document.getElementById('template-posts');
				const clone = template.content.cloneNode(true);

				// タイトルのセット
				clone.querySelector('.posts-title').textContent = posts.title;

				// 本文の改行を <br> に変換してセット
				const bodyElement = clone.querySelector('.posts-body');
				bodyElement.innerHTML = posts.body.replace(/\n/g, '<br>');

				// ユーザー情報を挿入
				const userIcon = clone.querySelector('.user-icon');
				const userName = clone.querySelector('.user-name');
				const userLink = clone.querySelector('.user-name');

				if (posts.users) {
					userIcon.src = `/assets/img/${posts.users.iconUri}`; // ユーザーアイコン画像のURL
					userName.textContent = posts.users.name; // ユーザー名
					userLink.href = `/profile/${posts.users.loginId}`; // ユーザープロフィールへのリンク
				}


				// 画像を挿入
				const imageContainer = clone.querySelector('.post-images');
				const image = posts.postImagesList?.[0];
				if (image?.imageUri) {
					const img = document.createElement('img');
					img.src = `/assets/img/${image.imageUri}`;
					img.alt = '投稿画像';
					img.classList.add('card-img-top', 'mb-2');
					imageContainer.appendChild(img);
				}

				//コメントを挿入
				const commentContainer = clone.querySelector('.post-comments');
				if (posts.postCommentsList && Array.isArray(posts.postCommentsList)) {
					for (const comment of posts.postCommentsList) {
						if (comment.comment) {
							const wrapper = document.createElement('div');
							wrapper.classList.add('d-flex', 'align-items-start', 'mb-2');

							//コメント・アイコン画像
							const img = document.createElement('img');
							img.src = `/assets/img/${comment.users.iconUri}`;
							img.classList.add('rounded-circle');
							img.style.width = '50px';
							img.style.height = '50px';
							img.style.objectFit = 'cover';
							img.style.marginRight = '45px';

							const textWrapper = document.createElement('div');

							//コメント者の名前
							const name = document.createElement('a');
							name.href = `/profile/${comment.users.loginId}`;
							name.textContent = comment.users.name;
							name.classList.add('ms-2', 'mb-0', 'fw-bold',);
							name.style.color = '#012970';
							name.style.display = 'inline-block';

							//コメント本文
							const p = document.createElement('p');
							p.textContent = comment.comment;
							//p.classList.add('mb-0');
							p.style.color = '#777777';
							p.style.fontSize 　= '14px';

							textWrapper.appendChild(name);
							textWrapper.appendChild(p);

							wrapper.appendChild(img);
							wrapper.appendChild(textWrapper);

							commentContainer.appendChild(wrapper);
						}
					}
				}

				const cardBody = clone.querySelector('.comment-form-area');

				// フォーム要素の作成
				const form = document.createElement('form');
				form.className = 'row g-3 needs-validation';
				form.action = '/home/comment';
				form.method = 'post';
				form.noValidate = true;

				// postIdのhidden input
				const inputHidden = Object.assign(document.createElement('input'), {
					type: 'hidden',
					name: 'postId',
					value: posts.id
				});

				// コメント入力欄
				const inputComment = Object.assign(document.createElement('input'), {
					type: 'text',
					name: 'comment',
					className: 'form-control',
					placeholder: 'コメントを入力...',
					required: true
				});

				// バリデーションメッセージ
				const invalidDiv = document.createElement('div');
				invalidDiv.className = 'invalid-feedback';
				invalidDiv.textContent = 'コメントを入力してください。';
				invalidDiv.style.display = 'none';

				// 入力グループ
				const inputGroup = document.createElement('div');
				inputGroup.className = 'input-group has-validation';
				inputGroup.append(inputHidden, inputComment, invalidDiv);

				// コメント入力用のcolラッパー
				const colDiv = document.createElement('div');
				colDiv.className = 'col-md-12';
				colDiv.appendChild(inputGroup);

				// 送信ボタン
				const submitBtn = document.createElement('button');
				submitBtn.type = 'submit';
				submitBtn.className = 'btn btn-primary';
				submitBtn.textContent = 'コメントする';

				const submitDiv = document.createElement('div');
				submitDiv.className = 'text-center';
				submitDiv.appendChild(submitBtn);

				// formにすべて追加
				form.append(colDiv, submitDiv);
				form.addEventListener('submit', function(e) {
					const value = inputComment.value.trim();

					if (!value) {
						e.preventDefault(); // 送信をキャンセル
						inputComment.classList.add('is-invalid'); // Bootstrapの赤枠
						invalidDiv.style.display = 'block'; // メッセージ表示
					} else {
						inputComment.classList.remove('is-invalid');
						invalidDiv.style.display = 'none';
					}
				});
				// card-bodyに追加
				cardBody.appendChild(form);
				document.getElementById('posts-container').appendChild(clone)
			}
			
		})
		.catch(error => {
			// エラーハンドリング
			console.error('Error fetching data:', error);
		});
		
		
}

