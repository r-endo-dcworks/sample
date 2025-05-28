/**
 *  コメントフォームを作って画面に追加し、
 送信ボタンを押したときにコメントをAjaxで送信し、
 送信成功後にコメント欄にコメントを追加表示する処理をする
 */

 // ログイン中のユーザー情報を取得

 const usersId = document.querySelector('meta[name="user-id"]')?.getAttribute('content');
 	if (usersId) {
 		console.log('ログインユーザーIDを取得しました。LodinId' + usersId);
 	}


export function createCommentElement(comment) {
	const wrapper = document.createElement('div');
	wrapper.classList.add('d-flex', 'align-items-center', 'mb-2');

	//imgタグの詳細
	const img = document.createElement('img');
	img.src = `/assets/img/${comment.userIconUri}`;
	img.classList.add('rounded-circle');
	Object.assign(img.style, {
		width: '50px', height: '50px', marginRight: '45px'
	});

	//aタグの詳細
	const a = document.createElement('a');
	a.href = `/profile/${comment.userLoginId}`;
	a.textContent = comment.userName;
	Object.assign(a.style, {
	  color: '#012970',
	  fontWeight: 'bold',
	});

	//pタグの詳細
	const p = document.createElement('p');
	p.textContent = comment.comment;
	//p.classList.add('') 
	Object.assign(p.style, {
	  color: '#777777',
	  fontSize: '14px',
	  marginLeft: '0' 
	});

	
	
	const textWrapper = document.createElement('div');	

	textWrapper.append(a, p);
	wrapper.append(img, textWrapper);

	return wrapper;
}

export function initCommentForPost(postsId, container) {

	//formタグの作成
	const form = document.createElement('form');

	// inputタグの作成
	const inputComment = document.createElement('input');
	inputComment.type = 'text';
	inputComment.name = 'comment';
	inputComment.className = 'form-control';
	inputComment.placeholder = 'コメントを入力...';

	// 送信ボタンの作成
	const submitBtn = document.createElement('button');
	submitBtn.type = 'submit';
	submitBtn.className = 'btn btn-primary';
	submitBtn.textContent = 'コメントする';

	form.append(inputComment,submitBtn);
	
	form.className = 'row g-3 needs-validation';
					form.action = '/api/comments';
					form.method = 'post';
					form.noValidate = true;

					// postIdのhidden input
					const inputHidden = Object.assign(document.createElement('input'), {
						type: 'hidden',
						name: 'postsId',
						value: postsId
					});
					
					// 入力グループ
					const inputGroup = document.createElement('div');
					inputGroup.className = 'input-group has-validation';
					inputGroup.append(inputHidden, inputComment);

					// コメント入力用のcolラッパー
					const colDiv = document.createElement('div');
					colDiv.className = 'col-md-12';
					colDiv.appendChild(inputGroup);

					const submitDiv = document.createElement('div');
					submitDiv.className = 'text-center';
					submitDiv.appendChild(submitBtn);

					// formにすべて追加
					form.append(colDiv, submitDiv);

	form.addEventListener('submit', e => {
		e.preventDefault();
		const commentText = form.comment.value.trim();
		if (!commentText) return;
		
		console.log('送信データ:', {
				postsId: postsId,
				usersId: usersId,
				comment: commentText
			});

		fetch('/api/comments', {
		  method: 'POST',
		  headers: { 'Content-Type': 'application/json' },
		  body: JSON.stringify({
		    postsId: postsId,
		    usersId: usersId, 
		    comment: commentText
		  })

		})

			.then(res => res.json()) // サーバからJSONを返す想定
			.then(comment => {			//commentにはJSONオブジェクトが入る
				
				console.log('Response JSON:', comment);

				const container = document.querySelector(`.comment-container[data-post-id="${postsId}"]`);
				      const div = document.createElement('div');
				      div.className = 'comment-item';
					  const commentElement = createCommentElement(comment);
					   container.appendChild(commentElement);
				      container.appendChild(div);

				      form.comment.value = '';
			})
			.catch(console.error);

		form.comment.value = '';
	});

	container.appendChild(form);
}

function appendComment(container, comment) {
	const commentElement = createCommentElement(comment);
	container.appendChild(commentElement);
}
