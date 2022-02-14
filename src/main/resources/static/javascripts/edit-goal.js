'use strict';

// button
const add_goal_btn = document.querySelector('.goal__add-btn');

// modal
const modal_create = document.querySelector('.modal-create');
const modal_edit = document.querySelector('.modal-edit');
const modal_delete = document.querySelector('.modal-delete');
const modal_background = document.querySelector('.modal-background');

// modal-button
const add_goal_confirm_btn = document.querySelector('.create_confirm');
const add_goal_back_btn = document.querySelectorAll('.modal_btn.back')[0];
const edit_goal_confirm_btn = document.querySelector('.edit_confirm');
const edit_goal_delete_btn = document.querySelectorAll('.modal_btn.edit')[0];
const delete_goal_exit_btn = document.querySelectorAll('.modal_btn.exit')[0];
const delete_goal_delete_btn = document.querySelectorAll('.modal_btn.delete')[0];
const modal_color_btn = document.querySelectorAll('.color-picker__list-item');

// modal-input
const modal_create_input = document.querySelectorAll('.input-goal.create')[0];
const modal_edit_input = document.querySelectorAll('.input-goal.edit')[0];

// modal color

// 목표 수
var num_of_goal = 2;


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

// 모달창 초기화
function initCreateModal() {
    modal_create_input.value = "";
    
}

function initEditModal() {
    modal_edit_input.value = "";
    
}

// 목표 리스트 수정 버튼을 눌렀을 경우
function onClickGoalList() {
    if (num_of_goal > 0) {
        document.querySelectorAll('.goal-list__edit-btn').forEach((e) => {
            e.addEventListener('click', (e) => {
                modal_edit.classList.add('active');
                modal_background.classList.add('active');

                // 색상 정보 초기화 추가 
                onClickEditGoalConfirmBtn();
                onClickEditGoalDeleteBtn();
            })
        })
    }
}

// 목표 추가 버튼 눌렀을 경우 
function onClickAddGoal() {
    add_goal_btn.addEventListener('click', (e) => {
        modal_create.classList.add('active');
        modal_background.classList.add('active');

        // 색상 정보 초기화 추가 
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
        modal_background.classList.remove('active');
    })
}

// create 모달창에서 뒤로가기 버튼 눌렀을 경우 
function onClickAddGoalBackBtn() {
    add_goal_back_btn.addEventListener('click', (e) => {
        // 초기화
        initModal();
        modal_create.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// edit 모달창에서 확인 버튼 눌렀을 경우 
function onClickEditGoalConfirmBtn() {
    edit_goal_confirm_btn.addEventListener("click", (e) => {
        // axios 

        initModal();
        modal_edit.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// edit 모달창에서 삭제 버튼 눌렀을 경우 
function onClickEditGoalDeleteBtn() {
    edit_goal_delete_btn.addEventListener('click', (e) => {
        modal_edit.classList.remove('active');
        modal_delete.classList.add('active');
        onClickDeleteGoalExitBtn();
        onClickDeleteGoalDeleteBtn();
    })
}

// delete 모달창에서 취소 버튼 눌렀을 경우 
function onClickDeleteGoalExitBtn() {
    delete_goal_exit_btn.addEventListener('click', (e) => {
        initModal();
        modal_delete.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// delete 모달창에서 삭제 버튼 눌렀을 경우 
function onClickDeleteGoalDeleteBtn() {
    delete_goal_delete_btn.addEventListener('click', (e) => {
        // 목표 삭제 axios 요청 

        initModal();
        modal_delete.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// 모달 창에서 색상 선택했을 경우 
function onClickModalColor() {
    modal_color_btn.forEach((elem) => {
        elem.addEventListener('click', (e) => {
            // border-radius 초기화 
            if (e.target.classList.contains('active') == false) inItColorPicker(e);
            var isEdit = e.target.parentElement.parentElement.parentElement.classList.contains('edit');
            var color = getColorId(e);
            if (isEdit == false) { // goal 생성일 경우 
                console.log('이건 생성');
                modal_create_input.style.borderBottom = "1px solid " + color;
                modal_create_input.style.color = color;
            } else { // goal 수정일 경우 
                console.log('이건 수정');
                modal_edit_input.style.borderBottom = "1px solid " + color;
                modal_edit_input.style.color = color;
                console.log(modal_edit_input.placeholder.style);
            }
        })
        
    })
}

// 색상 정보 가져오기 
function getColorId(e) {
    return e.target.style.backgroundColor;
}

// 색상 선택 초기화
function inItColorPicker(e) {
    document.querySelectorAll('.color-picker__list-item.active').forEach((elem) => {
        elem.classList.remove('active');
    })
    e.target.classList.add('active');
}

function init() {
    onClickAddGoal();
    onClickGoalList();
    onClickModalColor();
}
init();