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
 * 7. 친구 삭제 버튼 눌렀을 때 :
 *  (1) searchFriend() 에서 onClickDeleteFriend() 실행
 *  (2) onClickDeleteFriend()에서 click 이벤트 발생 시 deleteFriend(nickname, e) 실행 시
 *  (3) deleteFriend(nickname, e) 실행 시 서버에게 요청하고 (axios) showSearchFriend() 실행하여 돔조작
 *  (4) 
 * 8. 친구 요청 수락 버튼 눌렀을 때 :
 *  (1) 
 */

// modal 
const modal_delete = document.querySelector('.modal-delete');
const modal_confirm = document.querySelector('.modal-confirm');
const modal_background = document.querySelector('.modal-background');

// modal button
const modal_btn_exit = document.querySelector('.modal_btn-exit');
const modal_btn_delete = document.querySelector('.modal_btn-delete');

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

// strong
const domNumOfFriend = document.querySelector('.num-of-friend');
const domNumOfRequest = document.querySelector('.num-of-request');

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
const request_table_contents = document.querySelector('.request-table__contents');

// pagination
const pagination = document.querySelector('.pagination');

// 변수 
var numOfnotFriend = 0; // 서로 친구가 아닌 유저 수
var numOfRequestSend = 0; // 친구요청 보낸 수
var numOfFriend = 0; // 친구 수
var numOfRequestNotAccept = 0; // 친구요청 받은 수

// axios Get통신 
async function requestGet(url, params) {
    try {
        const options = {
            method: "GET",
            url: url,
            params
        };
        const res = await axios(options);
        console.log(res);
        return res.data;
    } catch(err) {
        console.log(err);
        throw new Error(err);
    }
}

// 문자, 특수문자 제외 키는 인식하지 않기
function isValidInput(input) {
    console.log(input.charCodeAt(input.length-1));
    if (input.charCodeAt(input.length-1) < 34) return false;
    return true;
}

// search창 비우기 
function resetSearch() {
    search_user.value = '';
    search_friend.value = '';
}

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

        resetSearch();
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

        resetSearch();
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

        resetSearch();
        showRequest();
    })
}

// scroll event
function onScroll(lastNickName) {

}

// 친구추가 버튼 혹은 (친구추가)요청 취소 버튼을 눌렀을 경우
function onClickAddOrResetFriend() {
    document.querySelectorAll('.user-table__btn-plus').forEach((elem) => {
        elem.addEventListener('click', (e) => {
            const nickname = e.target.parentElement.querySelector('.user-table__nickname').innerHTML;
            if (elem.classList.contains('active')) addFriend(nickname, e);
            else resetFriend(nickname, e);
        })
    })
}

// 친구삭제 버튼 눌렀을 경우
function onClickDeleteFriend() {
    document.querySelectorAll('.friend-table__btn-delete').forEach((elem) => {
        elem.addEventListener('click', (e) => {
            const nickname = e.target.parentElement.querySelector('.friend-table__nickname').innerHTML;
            console.log(nickname);
            modal_delete.classList.add('active');
            modal_background.classList.add('active');
            onClickModalDeleteFriend(nickname);
            onClickModalExitFriend();
        })
    })
}

// 모달 창에서 삭제 버튼 눌렀을 경우
function onClickModalDeleteFriend(nickname) {
    const deleteBtn = document.querySelector('.modal_btn-delete');
    deleteBtn.addEventListener('click', (e) => {
        console.log('모달창 삭제 버튼 누름');
    })
}

// 모달 창에서 취소 버튼 눌렀을 경우 
function onClickModalExitFriend() {
    const exitBtn = document.querySelector('.modal_btn-exit');
    exitBtn.addEventListener('click', (e) => {
        console.log('모달창 취소 버튼 누름');
        modal_delete.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// 친구 받기 버튼 눌렀을 경우
function onClickAcceptFriend() {
    document.querySelectorAll('.request-table__btn-accept').forEach((elem) => {
        elem.addEventListener('click', (e) => {
            const nickname = e.target.parentElement.parentElement.querySelector('.request-table__nickname').innerHTML;

            console.log(nickname);
            acceptFriend(nickname);
        })
    })
}

// 친구 거절 버튼 눌렀을 경우 
function onClickRefuseFriend() {
    document.querySelectorAll('.request-table__btn-refuse').forEach((elem) => {
        elem.addEventListener('click', (e) => {
            const nickname = e.target.parentElement.parentElement.querySelector('.request-table__nickname').innerHTML;
            console.log(nickname);
            refuseFriend(nickname);
        })
    })
}

// 유저목록에서 친구추가 버튼 눌렀을 경우, 요청취소 버튼으로 변경
function addBtnToResetBtn(btn) { 
    btn.classList.toggle('active');
    btn.innerHTML = "요청 취소";
}

// 유저목록에서 요청취소 버튼 눌렀을 경우, 친구추가 버튼으로 변경
function resetBtnToAddBtn(btn) { 
    btn.classList.toggle('active');
    btn.innerHTML = "추가";
}

// 친구목록 수 변경
function modifyFriendNum() {
    domNumOfFriend.innerHTML = numOfFriend;
}

// 친구요청 온 수 변경 
function modifyRequestNum() {
    domNumOfRequest.innerHTML = numOfRequestNotAccept;
}

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
            button.classList.add('active');
            divTag.append(button);
            break;

            case 1 : 
            button.innerHTML = "요청 취소";
            divTag.append(button);
            break;

            case 2 : 
            spanTag.innerHTML = "응답 대기";
            divTag.append(spanTag);
            break;
        }
        user_table_contents.append(liTag);
    }
}

// 친구목록에서 검색했을 때 돔조작 
function showSearchFriend(data) {
    for (const nickname of data) {
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
    request_table_contents.innerHTML = "";
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
    const params = {
        user: input.value
    }
    if (isValidInput(input.value) === false) return;
    requestGet('/searchMember', params)
        .then(res => {
            user_table_contents.innerHTML = "";

            if (res == null) return;
            console.log(Object.keys(res).length);
        
            // 받아온 데이터에서 서로 친구가 아닌 수와 친구요청을 보낸 수를 계산함
            numOfnotFriend = Object.values(res).reduce((cnt, ele) => cnt + (0 === ele), 0);
            numOfRequestSend = Object.values(res).reduce((cnt, ele) => cnt + (1 === ele) , 0);
        
            showSearchUser(res);
            if (numOfnotFriend > 0 || numOfRequestSend > 0) onClickAddOrResetFriend(); // 친구요청 버튼이나 요청 취소 버튼 눌렀을 때 
        })
}

// 친구 추가 버튼 눌렀을 때 axios
let addFriend = async function(nickname, e) {
    const params = {
        followUser: nickname
    }
    requestGet('/requestFriend', params)
        .then(res => {
            if (res === true) {
                const button = e.target.parentElement.parentElement.children[0].children[1];
                addBtnToResetBtn(button);
                numOfnotFriend--;
                numOfRequestSend++;
            }
        })
}

// 친구요청 취소 버튼 눌렀을 떄 axios
let resetFriend = async function(nickname, e) {
    const params = {
        followUser: nickname
    }
    requestGet('/cancelRequest', params)
        .then(res => {
            if (res === true) {
                const button = e.target.parentElement.parentElement.children[0].children[1];
                resetBtnToAddBtn(button);
                numOfRequestSend--;
                numOfnotFriend++;
            }
        })
}

// 친구 검색 axios
let searchFriend = async function() {
    var input = document.getElementById('search-friend');

    const params = {
        friendName: input.value
    }
    if (isValidInput(input.value) === false) return;
    requestGet('/searchFriend', params)
        .then(res => {
            friend_table_contents.innerHTML = "";
            showSearchFriend(res);
            if (res.length > 0) onClickDeleteFriend();
        })
}

// 친구 삭제 버튼 눌렀을 때 
let deleteFriend = async function(nickname, e) {
    const button = e.target.parentElement.parentElement.children[0].children[1];
    console.log(button);
    const params = {
        friendName: nickname
    }
    // requestGet('/deleteFriend', params)
    //     .then(res => {

    //     })
}

// 친구 목록 axios
let showFriend = async function() {
    friend_table_contents.innerHTML = "";

    requestGet('/friendList', {})
        .then (res => {
            numOfFriend = res.length;
            console.log(numOfFriend);
            showSearchFriend(res);
            modifyFriendNum();
            if (numOfFriend > 0) onClickDeleteFriend();
        })
}

// 친구 요청 보여주기 
let showRequest = async function() {
    request_table_contents.innerHTML = "";
    
    requestGet('/followerList', {})
        .then (res => {
            console.log(res);
            numOfRequestNotAccept = res.length;
            showRequestList(res);
            modifyRequestNum();
            if (numOfRequestNotAccept > 0) onClickAcceptFriend();
            if (numOfRequestNotAccept > 0) onClickRefuseFriend();
        })
}

// 친구요청수락할 때 axios
let acceptFriend = async function(nickname) {
    request_table_contents.innerHTML = "";
    const params = {
        follower: nickname
    }
    console.log("요청수락할 때 axios 수행");
    requestGet('/acceptFollower', params)
        .then (res => {
            console.log(res);
            numOfRequestNotAccept = res.length;
            showRequestList(res);
            modifyRequestNum();
        })
}

// 친구요청 거절할 때 axios
let refuseFriend = async function(nickname) {
    request_table_contents.innerHTML = "";
    const params = {
        follower: nickname
    }
    console.log("요청 거절할 때 axios");
    requestGet('/refuseFollower', params)
        .then (res => {
            console.log(res);
            numOfRequestNotAccept = res.length;
            showRequestList(res);
            modifyRequestNum();
        })
}



function isScroll() {
    const screenHeight = screen.height; // 화면 크기
    let onTime = false;

    document.addEventListener("scroll", onScroll, {passive:true});
    function onScroll() {
        const fullHeight = user_table_contents.clientHeight;
        const scrollPosition = scrollY;

        if (fullHeight-screenHeight/2 <= scrollPosition && !oneTime) {}
        oneTime = true;
        madeBox();
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
