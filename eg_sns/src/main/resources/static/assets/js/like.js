document.addEventListener('DOMContentLoaded', function() {


	//ログインユーザー情報の取得
	const loginUserId = document.querySelector('meta[name="user-id"]')?.getAttribute('content');
	if (loginUserId) {
		console.log('ログインユーザーIDを取得しました。LodinId' + loginUserId);
	}

	// 1. ページ内の全いいねボタンを取得
	const likeButtons = document.querySelectorAll('.like-button');

	// 2. 各ボタンにクリックイベントをセット
	likeButtons.forEach(function(button) {

		const icon = button.querySelector('i');
		const likeCount = button.nextElementSibling;
		const postsId = button.getAttribute('data-post-id');

		button.addEventListener('click', function(event) {
			event.preventDefault();


			// 3. 投稿IDをdata属性などから取得
			const postsId = button.getAttribute('data-post-id');
			console.log('いいねボタンが押されました。PostsId=' + postsId);
			//ログは表示されているが、PostId＝nullになっている


			// 4. APIへPOSTリクエスト
			fetch(`/api/like/${postsId}`, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				}
			})
				.then(response => {
					if (!response.ok) {
						if (response.status === 401) alert('ログインしてください');
						throw new Error('通信エラー');
					}
					return response.json();
				})
				.then(data => {
								const { likeCount: count, liked } = data;

								if (liked) {
									icon.classList.remove('bi-heart');
									icon.classList.add('bi-heart-fill');
								} else {
									icon.classList.remove('bi-heart-fill');
									icon.classList.add('bi-heart');
								}

								likeCount.textContent = count;
							})
				.catch(error => {
					console.error('エラー:', error);
				});
		});
	});
});


//window.applyLikeButtonLogic = applyLikeButtonLogic;
