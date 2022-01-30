'use strict';

//tab Btn
const tabBtn_search = document.querySelector('.friend-tab__search-btn');
const tabBtn_list = document.querySelector('.friend-tab__list-btn');
const tabBtn_request = document.querySelector('.friend-tab__request-btn');

// tab
const tabLi_search = document.querySelector('.friend-tab__search');
const tabLi_list = document.querySelector('.friend-tab__list');
const tabLi_request = document.querySelector('.friend-tab__request');

// form
const search_form = document.querySelector('.search-form-control');

// input
const search_user = document.querySelector('.search-user');
const search_friend = document.querySelector('.search-friend');

// table
const user_table = document.querySelector('.user-table');
const friend_table = document.querySelector('.friend-table');
const request_table = document.querySelector('.request-table');

// ul
const user_table_contents = document.querySelector('.user-table__contents');
const friend_table_contents = document.querySelector('.friend-table__contents');
const request_table_contents = document.querySelector('.request-talbe__contents');

// button
// const add_friend_btn = document.querySelector('.user-table__btn-plus');
const delete_friend_btn = document.querySelector('.friend-table__btn-delete');
const accept_friend_btn = document.querySelector('.request-table__btn-accept');
const refuse_friend_btn = document.querySelector('.request-table__btn-reject');

// tab Button 클릭할 때 
function tabBtnClick() {
    // 친구추가 
    tabBtn_search.addEventListener('click', (e) => {
        // tab
        tabLi_search.classList.add('active');
        tabLi_list.classList.remove('active');
        tabLi_request.classList.remove('active');

        //input
        search_form.classList.add('active');
        search_user.classList.add('active');
        search_friend.classList.remove('active');

        // table
        user_table.classList.add('active');
        friend_table.classList.remove('active');
        request_table.classList.remove('active');
    })
    
    // 친구목록 
    tabBtn_list.addEventListener('click', (e) => {
        // tab        
        tabLi_search.classList.remove('active');
        tabLi_list.classList.add('active');
        tabLi_request.classList.remove('active');

        // input
        search_form.classList.add('active');
        search_user.classList.remove('active');
        search_friend.classList.add('active');

        // table
        user_table.classList.remove('active');
        friend_table.classList.add('active');
        request_table.classList.remove('active');

        // 친구 보여주기
        showFriend();
    })

    tabBtn_request.addEventListener('click', (e) => {
        // tab
        tabLi_search.classList.remove('active');
        tabLi_list.classList.remove('active');
        tabLi_request.classList.add('active');

        // input
        search_form.classList.remove('active');

        // table
        user_table.classList.remove('active');
        friend_table.classList.remove('active');
        request_table.classList.add('active');

        showRequest();
    })
}

// scroll event
function onScroll(lastNickName) {

}

// 친구추가 버튼 눌렀을 경우 
function onClickAddFriend() {
    const add_friend_btn = document.querySelector('.user-table__btn-plus'); 
    add_friend_btn.addEventListener('click', (e) => {
        const nickname = e.target.parentElement.querySelector('.user-table__nickname').innerHTML;
        console.log(nickname);
        addFriend(nickname, e);
    })
}

// 친구삭제 버튼 눌렀을 경우

// 친구 받기 버튼 눌렀을 경우

// 친구 거절 버튼 눌렀을 경우 

// 유저목록에서 검색 후 돔조작 
function showSearchUser(data) {
    for (const [nickname, state] of Object.entries(data)) {
        var liTag = document.createElement('li');
        liTag.classList.add('user-table__list');

        var divTag = document.createElement('div');
        divTag.classList.add('user-table__list-item');

        var strongTag = document.createElement('strong');
        strongTag.classList.add('user-table__nickname');
        strongTag.innerHTML = nickname;

        var button = document.createElement('button');
        button.classList.add('user-table__btn-plus');

        var spanTag = document.createElement('span');
        spanTag.classList.add('user-table__description');

        liTag.append(divTag);
        divTag.append(strongTag);
        switch (state) {
            case 0 : 
            button.innerHTML = "추가";
            divTag.append(button);
            break;

            case 1 : 
            spanTag.innerHTML = "친구 신청 완료";
            divTag.append(spanTag);
            break;

            case 2 : 
            spanTag.innerHTML = "친구 응답 대기";
            divTag.append(spanTag);
            break;
        }
        
        user_table_contents.append(liTag);
    }
}

// 친구목록에서 검색했을 때 돔조작 
function showSearchFriend(data) {
    for (const nickname of data) {
        console.log(nickname);
        var liTag = document.createElement('li');
        liTag.classList.add('friend-table__list');

        var divTag = document.createElement('div');
        divTag.classList.add('friend-table__list-item');

        var strongTag = document.createElement('strong');
        strongTag.classList.add('friend-table__nickname');
        strongTag.innerHTML = nickname;

        var button = document.createElement('button');
        button.classList.add('friend-table__btn-delete');
        button.innerHTML = '삭제';

        liTag.append(divTag);
        divTag.append(strongTag);
        divTag.append(button);
        friend_table_contents.append(liTag);
    }
}

// 친구요청 리스트 돔조작 
function showRequestList(data) {
    for (const nickname of data) {
        var liTag = document.createElement('li');
        liTag.classList.add('request-table__list');

        var divTag = document.createElement('div');
        divTag.classList.add('request-table__list-item');

        var strongTag = document.createElement('strong');
        strongTag.classList.add('request-table__nickname');
        strongTag.innerHTML = nickname;

        var btnDivTag = document.createElement('div');
        btnDivTag.classList.add('request-table__btn');

        var acceptBtn = document.createElement('button');
        acceptBtn.classList.add('request-table__btn-accept');
        acceptBtn.innerHTML = '받기';

        var refuseBtn = document.createElement('button');
        refuseBtn.classList.add('request-table__btn-refuse');
        refuseBtn.innerHTML = '거절';

        liTag.append(divTag);
        divTag.append(strongTag);
        divTag.append(btnDivTag);
        btnDivTag.append(acceptBtn);
        btnDivTag.append(refuseBtn);
        request_table_contents.append(liTag);
    }
}

// 유저 검색 axios
let searchUser = async function() {
    var input = document.getElementById('search-user');
    user_table_contents.innerHTML = "";

    try {
        const res = await axios.get('/searchMember', {
            params: {
                user: input.value
            }
        });

        const data = res.data;
        if (data == null) return;
        const size = Object.keys(data).length;
        console.log(Object.keys(data).length);
        console.log(data);
        user_table_contents.innerHTML = "";
        
        showSearchUser(data);
        if (size > 0) onClickAddFriend();

        
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

// 친구 추가 axios
let addFriend = async function(nickname, e) {
    console.log(nickname);
    try {
        const res = await axios.get('/requestFriend', {
            params: {
                followUser: nickname
            }
        });
        console.log(res);
        const parent = e.target.parentElement.parentElement.parentElement;
        var child = e.target.parentElement.parentElement;
        parent.deleteElement(child);
        showSearchUser({"nickname": 1});
        
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

// 친구 검색 axios
let searchFriend = async function() {
    var input = document.getElementById('search-friend');
    friend_table_contents.innerHTML = "";

    try {
        const res = await axios.get('/searchFriend', {
            params: {
                friendName: input.value
            }
        });
        console.log(res);
        const data = res.data;
        
        showSearchFriend(data);
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

// 친구 목록 axios
let showFriend = async function() {
    friend_table_contents.innerHTML = "";

    try {
        const res = await axios.get('/friendList');
        console.log(res);
        const data = res.data;
        showSearchFriend(data);
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

// 친구 요청 보여주기 
let showRequest = async function() {
    request_table_contents.innerHTML = "";

    try {
        const res = await axios.get('/followerList');
        console.log(res);
        const data = res.data;
        showRequestList(data);
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

const scrollHandler = evt => {
    //현재 스크롤이 일어나는 이벤트의 속성값을 구해준다
        const scrollHeight = evt.nativeEvent.srcElement.scrollHeight;
        const scrollTop = evt.nativeEvent.srcElement.scrollTop;
        const clientHeight = evt.nativeEvent.srcElement.clientHeight;
    
    //조건이 만족될때 현재 데이터의 페이지 상태값이 추가되고 추가된 값을 
    //인자로 받아서 데이터를 더 불러온다.
        if (scrollTop + clientHeight >= scrollHeight) {
            setCurrentPostId(prev => prev + 1);
            fetchData(currentPostId);
        }
    };

function init() {
    tabBtnClick();
}

init();
