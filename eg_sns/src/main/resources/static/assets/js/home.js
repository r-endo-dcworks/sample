//import { initLikeButton } from './like.js'; // ←相対パス or 絶対パスに注意
import { initCommentForPost, createCommentElement } from './comment.js';
//TODO からのファイルを打ってみ

function callWebAPI() {
	const button = document.getElementById('btn-more');
	const sinceId = button.getAttribute('data-sinceid');
	console.log("callWebApiが読み込まれました。sinceId:" + sinceId);


	// サーバーにリクエストを送る
	fetch('http://localhost:8080/api/getPosts?sinceId=' + sinceId)
		.then(response => response.json())
		.then(ret => {
			console.log('Response JSON:', ret);

			if (!ret.page_info.has_next) {
				document.getElementById('btn-more-section')?.remove();
			} else {
				button.setAttribute('data-sinceid', ret.page_info.since_id);
			}

			for (let posts of ret.data) {
				const template = document.getElementById('template-posts');
				const clone = template.cloneNode(true);  // contentなし
				clone.style.display = "";
				clone.removeAttribute('id');  // idの重複を避けるため外す


				//投稿タイトル
				clone.querySelector('.posts-title').textContent = posts.post.title;

				//投稿本文
				const bodyElement = clone.querySelector('.posts-body');
				bodyElement.innerHTML = posts.post.body.replace(/\n/g, '<br>');

				//投稿者情報
				const userIcon = clone.querySelector('.user-icon');
				const userName = clone.querySelector('.user-name');
				const userLink = clone.querySelector('.user-name');
				if (posts.post.users) {
					userIcon.src = `/assets/img/${posts.post.users.iconUri}`;
					userName.textContent = posts.post.users.name;
					userLink.href = `/profile/${posts.post.users.loginId}`;
				}

				//投稿画像
				const imageContainer = clone.querySelector('.post-images');
				const image = posts.post.postImagesList?.[0];
				if (image?.imageUri) {
					const img = document.createElement('img');
					img.src = `/assets/img/${image.imageUri}`;
					img.alt = '投稿画像';
					img.classList.add('card-img-top', 'mb-2');
					imageContainer.appendChild(img);
				}


				//いいねボタン
				const likeButton = clone.querySelector('.like-button');
				likeButton.setAttribute('data-post-id', posts.post.id);

				if (likeButton) {
					window.initLikeButton(likeButton); // ← like.jsの関数を呼び出す
				}


				const liked = clone.querySelector('.js-like-button');
				const icon = liked.querySelector('i');

				// likedフラグに応じてハートの表示を変更
				if (posts.liked) {
					icon.classList.remove('bi-heart');
					icon.classList.add('bi-heart-fill');
				} else {
					icon.classList.remove('bi-heart-fill');
					icon.classList.add('bi-heart');
				}



				const likeCount = clone.querySelector('.js-like-count');
				likeCount.textContent = posts.likeCount



				//投稿コメント
				const commentContainer = clone.querySelector('.post-comments');
				if (posts.post.postCommentsList && Array.isArray(posts.post.postCommentsList)) {
					for (const comment of posts.post.postCommentsList) {
						if (comment.comment) {
							const wrapper = document.createElement('div');
							wrapper.classList.add('d-flex', 'align-items-start', 'mb-2');

							const img = document.createElement('img');
							img.src = `/assets/img/${comment.users.iconUri}`;
							Object.assign(img.style, {
								width: '50px', height: '50px', objectFit: 'cover', marginRight: '45px'
							});
							img.classList.add('rounded-circle');

							const textWrapper = document.createElement('div');

							const name = document.createElement('a');
							name.href = `/profile/${comment.users.loginId}`;
							name.textContent = comment.users.name;
							name.classList.add('fw-bold');
							name.style.color = '#012970';
							name.style.display = 'inline-block';

							const p = document.createElement('p');
							p.textContent = comment.comment;
							p.style.color = '#777777';
							p.style.fontSize = '14px';

							textWrapper.appendChild(name);
							textWrapper.appendChild(p);

							wrapper.appendChild(img);
							wrapper.appendChild(textWrapper);

							commentContainer.appendChild(wrapper);
						}
					}
				}


				const container = clone.querySelector('.comment-container');
				const postId = posts.post.id;
				if (container && postId) {
					container.setAttribute('data-post-id', postId); // ← 忘れずに追加
					initCommentForPost(postId, container);
				}

				document.getElementById('posts-container').appendChild(clone);
			}
		})

}


document.addEventListener("DOMContentLoaded", function() {
	const button = document.getElementById('btn-more');
	if (button) {
		button.addEventListener("click", function() {
			callWebAPI();
		});
	}
});