'use strict';

// button
const add_goal_btn = document.querySelector('.goal__add-btn');

// modal
const modal_create = document.querySelector('.modal-create');
const modal_edit = document.querySelector('.modal-edit');
const modal_delete = document.querySelector('.modal-delete');

// modal-button
const add_goal_confirm_btn = document.querySelector('.create_confirm');
const add_goal_back_btn = document.querySelectorAll('.modal_btn.back')[0];

// modal-input
const modal_create_input = document.querySelectorAll('.input-goal.create')[0];

// 모달창 초기화
function initModal() {
    modal_create_input.value = "";
}

// 목표 추가 버튼 눌렀을 경우 
function onClickAddGoal() {
    add_goal_btn.addEventListener('click', (e) => {
        modal_create.classList.add('active');
        onClickAddGoalConfirmBtn();
        onClickAddGoalBackBtn();
    })
}

// create 모달창에서 확인 버튼 눌렀을 경우 
function onClickAddGoalConfirmBtn() {
    add_goal_confirm_btn.addEventListener('click', (e) => {
        // axios

        initModal();
        modal_create.classList.remove('active');
    })
}

// create 모달창에서 뒤로가기 버튼 눌렀을 경우 
function onClickAddGoalBackBtn() {
    add_goal_back_btn.addEventListener('click', (e) => {
        // 초기화
        initModal();
        modal_create.classList.remove('active');
    })
}



function init() {
    onClickAddGoal();

}
init();