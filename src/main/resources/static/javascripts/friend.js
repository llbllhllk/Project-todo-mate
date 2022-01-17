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

function tabBtnClick() {
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
    })
}

let searchUser = async function() {
    var input = document.getElementById('search-user');
    var table = document.querySelector('.user-table');
    table.innerHTML = "";
    try {
        let res = await axios({
            mehtod: 'POST',
            url: '/userList',
            data: {
                user: input.value
            }
        });

        console.log(res);
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

let searchFriend = async function() {
    var input = document.getElementById('search-friend');
    var table = document.querySelector('.friend-table');
    table.innerHTML = "";
    try {
        let res = await axios({
            mehtod: 'POST',
            url: '/friendList',
            data: {
                friend: input.value
            }
        });

        console.log(res);
    } catch (err) {
        console.log(err);
        throw new Error(err);
    }
}

function init() {
    tabBtnClick();
}

init();
