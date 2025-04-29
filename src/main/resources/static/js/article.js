//삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        // 사용자에게 삭제 확인 메시지 표시
        const confirmation = confirm('정말 삭제하시겠습니까?');

        if (confirmation) {
            let id = document.getElementById('article-id').value;

            // 삭제 요청 보내기
            fetch(`/api/articles/${id}`, {
                method: 'DELETE'
            })
                .then(() => {
                    alert('삭제가 완료되었습니다.');
                    location.replace('/articles');  // 삭제 후 아티클 목록 페이지로 리디렉션
                })
                .catch(error => {
                    console.error('삭제 실패:', error);
                    alert('삭제에 실패했습니다. 다시 시도해 주세요.');
                });
        } else {
            alert('삭제가 취소되었습니다.');
        }
    });
}

// 수정할 화면 (article.html)에서 수정 버튼 클릭 시
document.addEventListener('DOMContentLoaded', () => {
    const toModifyButton = document.getElementById('to-modify-btn');

    if (toModifyButton) {
        toModifyButton.addEventListener('click', event => {
            console.log("수정 버튼이 클릭되었습니다."); // 여기에 로그 추가

            const confirmation = confirm('수정하시겠습니까?');
            if (confirmation) {

                const articleId = document.getElementById('article-id').value;

                location.replace(`/new-article?id=${articleId}`);

            } else {
                alert('수정이 취소되었습니다.');
            }
        });
    } else {
        console.log("버튼을 찾을 수 없습니다.");
    }
});



//수정 중인 화면 기능 (newArticle.html)에서 수정 버튼 클릭 시
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        // 사용자에게 수정 확인 메시지 표시
        const confirmation = confirm('수정하시겠습니까?');

        if (confirmation) {
            // URL에서 파라미터로 'id' 값을 가져옴
            let params = new URLSearchParams(location.search);
            let id = params.get('id');

            // 제목과 내용 값 가져오기
            const title = document.getElementById('title').value;
            const content = document.getElementById('content').value;

            // 유효성 검사(제목과 내용이 비어있으면 경고 메시지 띄우기)
            if (!title || !content) {
                alert('제목과 내용을 모두 입력해주세요.');
                return;
            }

            // 로딩 상태 표시
            modifyButton.disabled = true;
            const loadingMessage = document.createElement('div');
            loadingMessage.innerText = '수정 중...';
            document.body.appendChild(loadingMessage);

            // 수정 요청 보내기
            fetch(`/api/articles/${id}`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title: title,
                    content: content
                })
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('수정에 실패했습니다. 다시 시도해 주세요.');
                    }
                    return response.json();  // 성공적인 응답 처리
                })
                .then(() => {
                    alert('수정이 완료되었습니다.');
                    location.replace(`/articles/${id}`);  // 수정된 아티클 페이지로 리디렉션
                })
                .catch(error => {
                    console.error('수정 실패:', error);
                    alert(error.message);  // 오류 메시지 띄우기
                })
                .finally(() => {
                    modifyButton.disabled = false;  // 버튼 다시 활성화
                    document.body.removeChild(loadingMessage);  // 로딩 메시지 제거
                });
        } else {
            alert('수정이 취소되었습니다.');
        }
    });
}



//생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {

        // 생성하기 전에 확인 메시지 표시
        const confirmation = confirm('생성하시겠습니까?');

        if (confirmation) {
            const title = document.getElementById('title').value;
            const content = document.getElementById('content').value;

        // 유효성 검사(제목과 내용이 비어있으면 경고 메시지 띄우기)
        if (!title || !content) {
            alert('제목과 내용을 모두 입력해주세요.');
            return;
        }

        // 로딩 상태 표시
        createButton.disabled = true;
        const loadingMessage = document.createElement('div');
        loadingMessage.innerText = '등록 중...';
        document.body.appendChild(loadingMessage);

        // fetch 요청 보내기
        fetch('/api/articles', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: title,
                content: content
            })
        })
            .then(response => {
                if (!response.ok) { //HTTP 응답 상태 확인
                    throw new Error('등록에 실패했습니다. 다시 시도해 주세요.');
                }
                return response.json();  // 성공적인 응답 처리
            })
            .then(() => {
                alert('등록 완료되었습니다.');
                location.replace('/articles');  // 아티클 목록 페이지로 리디렉션
            })
            .catch(error => {
                console.error('Error:', error);
                alert(error.message);  // 오류 메시지 띄우기
            })
            .finally(() => {
                createButton.disabled = false;  // 버튼 다시 활성화
                document.body.removeChild(loadingMessage);  // 로딩 메시지 제거
            });
        } else {
            alert('생성이 취소되었습니다.');
        }
    });
}