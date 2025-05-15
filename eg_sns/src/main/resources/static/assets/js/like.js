/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
	const likeButtons = document.querySelectorAll('.like-button');

	likeButtons.forEach(function(likeButton) {
		const icon = likeButton.querySelector('i');
		const likeCount = likeButton.nextElementSibling;

		likeButton.addEventListener('click', function(e) {
			e.preventDefault();

			const isLiked = icon.classList.contains('bi-heart-fill');
			let count = parseInt(likeCount.textContent);

			if (isLiked) {
				icon.classList.remove('bi-heart-fill');
				icon.classList.add('bi-heart');
				likeCount.textContent = count - 1;
			} else {
				icon.classList.remove('bi-heart');
				icon.classList.add('bi-heart-fill');
				likeCount.textContent = count + 1;
			}
		});
	});
});