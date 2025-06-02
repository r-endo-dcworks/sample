function initLikeButton(button) {

	const icon = button.querySelector('i');
	const likeCount = button.nextElementSibling;

	button.addEventListener('click', function(event) {
		event.preventDefault();

		const postsId = button.getAttribute('data-post-id');
		console.log('いいねボタンが押されました。PostsId=' + postsId);

		fetch(`/api/like/${postsId}`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			}
		})
			.then(response => response.json())
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
			});
	});
}

// 初期表示のボタンにイベントを登録
document.addEventListener('DOMContentLoaded', function() {
	const likeButtons = document.querySelectorAll('.like-button');
	likeButtons.forEach(initLikeButton);
});

// グローバル公開 → home.jsなどからも使える
window.initLikeButton = initLikeButton;
