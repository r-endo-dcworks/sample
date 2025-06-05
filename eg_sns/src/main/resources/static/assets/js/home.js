import { initCommentForPost } from './comment.js';

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
				const template = document.getElementsByClassName('js-template-posts')[0];
				console.log("templateの中身：", template)
				const clone = template.cloneNode(true);  // contentなし
				clone.style.display = "";
				console.log('cloneの中身', clone);

				//投稿タイトル
				clone.querySelector('.js-posts-title').textContent = posts.post.title;

				//投稿本文
				const bodyElement = clone.querySelector('.js-posts-body');
				bodyElement.innerHTML = posts.post.body.replace(/\n/g, '<br>');

				//投稿者情報
				const userIcon = clone.querySelector('.js-user-icon');
				const userInfo = clone.querySelector('.js-user-name');
				if (posts.post.users) {
					userIcon.src = `/assets/img/${posts.post.users.iconUri}`;
					userInfo.textContent = posts.post.users.name;
					userInfo.href = `/profile/${posts.post.users.loginId}`;
				}

				//投稿画像
				const imageContainer = clone.querySelector('.js-post-images');
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
					window.initLikeButton(likeButton); 
				}
				// likedフラグに応じてハートの表示を変更
				const icon = likeButton.querySelector('i');
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

							textWrapper.append(name,p);
							wrapper.append(img,textWrapper);
							commentContainer.appendChild(wrapper);
						}
					}
				}

				const container = clone.querySelector('.comment-container');
				const postId = posts.post.id;
				if (container && postId) {
					container.setAttribute('data-post-id', postId);
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