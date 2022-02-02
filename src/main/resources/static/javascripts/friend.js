'use strict';

/**
 * 1. 유저목록에서 검색 : (axios) searchUser()
 * 2. 친구목록 불러오기 : (axios) showFriend()
 * 3. 친구목록에서 검색 : (axios) searchFriend()
 * 4. 친구요청 목록 불러오기 : (axios) showRequest()
 * 5. 친구 요청 버튼 눌렀을 때 : 
 *  (1) searchUser() 에서 onClickAddFriend() 실행 
 *  (2) onClickAddFriend()에서 click 이벤트 발생 시 addFriend(nickname, e) 실행 
 *  (3) addFriend(nickname, e) 실행 시 서버에게 요청하고 (axios)
 *  (4) addBtnToResetBtn(btn) 실행하여 친구 추가 버튼을 친구 요청 취소 버튼으로 바꾸기 
 * 6. 친구 요청 취소 버튼 눌렀을 때 : 
 *  (1) searchUser() 에서 onClickResetFriend() 실행 
 *  (2) onClickResetFriend()에서 click 이벤트 발생 시 resetFriend(nickname, e) 실행
 *  (3) resetFriend(nickname, e)  실행 시 서버에게 요청하고 (axios)
 *  (4) resetBtnToAddBtn(btn) 실행하여 친구 요청 취소 버튼을 친구 추가 버튼으로 바꾸기 
 * 7.
 */

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

// 서로 친구가 아닌 유저 수
var numOfnotFriend = 0;

// 친구요청 보낸 수
var numOfRequestFriend = 0;

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

// 친구추가 버튼을 눌렀을 경우
function onClickAddFriend() {
    const add_friend_btn = document.querySelector('.user-table__btn-plus'); 
    add_friend_btn.addEventListener('click', (e) => {
        const nickname = e.target.parentElement.querySelector('.user-table__nickname').innerHTML;
        console.log(nickname);
        addFriend(nickname, e); // 
    })
}

// (친구추가)요청 취소 버튼을 눌렀을 경우
function onClickResetFriend() {
    const reset_friend_btn = document.querySelector('.user-table__btn-reset');
    reset_friend_btn.addEventListener('click', (e) => {
    const nickname = e.target.parentElement.querySelector('.user-table__nickname').innerHTML;
    console.log(nickname);
    resetFriend(nickname, e);
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

        var button_plus = document.createElement('button');
        button_plus.classList.add('user-table__btn-plus');

        var button_reset = document.createElement('button');
        button_reset.classList.add('user-table__btn-reset');

        var spanTag = document.createElement('span');
        spanTag.classList.add('user-table__description');

        liTag.append(divTag);
        divTag.append(strongTag);
        switch (state) {
            case 0 : 
            button_plus.innerHTML = "추가";
            divTag.append(button_plus);
            break;

            case 1 : 
            button_reset.innerHTML = "요청 취소";
            divTag.append(button_reset);
            break;

            case 2 : 
            spanTag.innerHTML = "응답 대기";
            divTag.append(spanTag);
            break;
        }
        
        user_table_contents.append(liTag);
    }
}

// 유저목록에서 친구추가 버튼 눌렀을 경우, 요청취소 버튼으로 변경
function addBtnToResetBtn(btn) { 
    btn.classList.remove("user-table__btn-plus");
    btn.classList.add("user-table__btn-reset");
    btn.innerHTML = "요청 취소";
}

// 유저목록에서 요청취소 버튼 눌렀을 경우, 친구추가 버튼으로 변경
function resetBtnToAddBtn(btn) { 
    btn.classList.remove("user-table__btn-reset");
    btn.classList.add("user-table__btn-plus");
    btn.innerHTML = "추가";
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

        // 받아온 데이터에서 서로 친구가 아닌 수와 친구요청을 보낸 수를 계산함
        numOfnotFriend = Object.values(data).reduce((cnt, ele) => cnt + (0 === ele), 0);
        numOfRequestFriend = Object.values(data).reduce((cnt, ele) => cnt + (1 === ele) , 0);

        showSearchUser(data);
        if (numOfnotFriend > 0) onClickAddFriend(); // 친구요청 버튼 눌렀을 때 
        if (numOfRequestFriend > 0) onClickResetFriend(); // 친구요청 취소 버튼 눌렀을 때 
        
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

// 친구 추가 버튼 눌렀을 때 axios
let addFriend = async function(nickname, e) {
    console.log("친구추가 버튼 눌림");
    const button = e.target.parentElement.parentElement.children[0].children[1];
    console.log(button);
    addBtnToResetBtn(button);
    numOfnotFriend--;
    console.log('이후 클래스들은 ');
    console.log(button.classList);
    // try {
    //     const res = await axios.get('/requestFriend', {
    //         params: {
    //             followUser: nickname
    //         }
    //     });
    //     console.log(res);
    //     const button = e.target.parentElement.parentElement.children[0].children[1];
    //     console.log(button);
    //     // addBtnToResetBtn({"nickname": 1}); // dom 조작 
    // } catch (err) {
    //     console.log(err);
    //     throw new Error(err);
    // }
}

// 친구요청 취소 버튼 눌렀을 떄 axios
let resetFriend = async function(nickname, e) {
    console.log('친구요청 취소 버튼 눌림');
    const button = e.target.parentElement.parentElement.children[0].children[1];
    console.log(button);
    resetBtnToAddBtn(button);
    numOfRequestFriend--;
    // try {
    //     const res = await axios.get('/cancelRequest', {
    //         params: {
    //             followUser: nickname
    //         }
    //     });
    //     console.log(res);
        
        
    // } catch (err) {
    //     console.log(err);
    //     throw new Error(err);
    // }
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
