// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        // 사용자에게 삭제 확인 메시지 표시
        const confirmation = confirm('삭제하시겠습니까?');
        if (!confirmation) return;

        let id = document.getElementById('article-id').value;
        deleteButton.disabled = true;
        deleteButton.innerText = '삭제 중...';

        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제에 실패했습니다. 다시 시도해 주세요.');
            location.replace('/articles');
        }

        httpRequest('DELETE', `/api/articles/${id}`, null, success, fail);
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
                let articleId = document.getElementById('article-id').value;
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
        if (!confirmation) return;

        // 제목과 내용 값 가져오기
        let title = document.getElementById('title').value;
        let content = document.getElementById('content').value;

        // 유효성 검사(제목과 내용이 비어있으면 경고 메시지 띄우기)
        if (!title || !content) {
             alert('제목과 내용을 모두 입력해주세요.');
             return;
         }

        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        // 로딩 상태 표시
        modifyButton.disabled = true;
        modifyButton.innerText = '수정 중...';

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
            location.replace(`/articles/${id}`);
        }

        httpRequest('PUT', `/api/articles/${id}`, body, success, fail);
    });
}

// 생성 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        // 생성하기 전에 확인 메시지 표시
        const confirmation = confirm('생성하시겠습니까?');
        if (!confirmation) return;

        let title = document.getElementById('title').value;
        let content = document.getElementById('content').value;

        // 유효성 검사(제목과 내용이 비어있으면 경고 메시지 띄우기)
        if (!title || !content) {
            alert('제목과 내용을 모두 입력해주세요.');
            return;
        }

        createButton.disabled = true;
        createButton.innerText = '등록 중...';

        body = JSON.stringify({
            title: title,
            content: content
        });

        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('등록 실패했습니다.');
            location.replace('/articles');
        }

        httpRequest('POST', '/api/articles', body, success, fail);
    });
}

// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');
        var dic = item.split('=');
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    const accessToken = localStorage.getItem('access_token');

    console.log('access_token:', accessToken);
    console.log('refresh_token in cookie:', getCookie('refresh_token'));

    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json',
        },
        body: body,
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: refresh_token,
                }),
            })
                .then(res => {
                    if (res.ok) return res.json();
                })
                .then(result => {
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    });
}