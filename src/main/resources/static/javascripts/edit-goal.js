'use strict';

// list 
const goal_list_contents = document.querySelector('.goal-list__contents');

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
const add_modal_color_btn = document.querySelectorAll('.color-picker__list-item.create');
var edit_modal_color_btn = document.querySelectorAll('.color-picker__list-item.edit');

// modal-input
const modal_create_input = document.querySelectorAll('.input-goal.create')[0];
const modal_edit_input = document.querySelectorAll('.input-goal.edit')[0];

// 목표 수
var num_of_goal = test;

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

// 중복되지 않는 랜덤 수 생성 
function makeGoalId() {
    var randomNumber;
    while (1) {
        randomNumber = Math.floor(Math.random() * 1000 + 1);
        if (goalId.includes(randomNumber) === false) break;
    }
    return randomNumber;
}

// create 모달창 초기화
function initCreateModal() {
    modal_create_input.value = "";

    var cnt = 1;
    add_modal_color_btn.forEach((elem) => {
        if (cnt === 1) {
            var color = elem.style.backgroundColor;
            modal_create_input.style.borderBottom = "1px solid " + color;
            modal_create_input.style.color = color;

            elem.classList.add('active');
        } else {
            elem.classList.remove('active');
        }
        cnt++;
    })
}

// edit 모달창 초기화 
function initEditModal(e) {
    console.log(e.target);
    modal_edit_input.value = e.target.parentElement.parentElement.querySelector('.goal-list__name').innerHTML;
    var color = e.target.parentElement.parentElement.querySelector('.goal-list__name').style.color;
    
    edit_modal_color_btn.forEach((elem) => {
        if (elem.style.backgroundColor === color) {
            elem.classList.add('active');
        } else elem.classList.remove('active');
    })

    modal_edit_input.style.borderBottom = "1px solid " + color;
    modal_edit_input.style.color = color;
}

// 목표 리스트 수정 버튼을 눌렀을 경우
function onClickGoalList() {
    if (num_of_goal > 0) {
        document.querySelectorAll('.goal-list__edit-btn').forEach((elem) => {
            elem.addEventListener('click', (e) => {
                modal_edit.classList.add('active');
                modal_background.classList.add('active');
                // 색상 정보 초기화 추가 
                const id = elem.parentElement.querySelector('.goal-list__name').id;
                initEditModal(e);
                onClickEditGoalConfirmBtn(id);
                onClickEditGoalDeleteBtn(id);
            })
        })
    }
}

// 목표 추가 버튼 눌렀을 경우 
function onClickAddGoal() {
    add_goal_btn.addEventListener('click', (e) => {
        modal_create.classList.add('active');
        modal_background.classList.add('active');

        initCreateModal();
        // 색상 정보 초기화 추가 
        onClickAddGoalConfirmBtn();
        onClickAddGoalBackBtn();
    })
}

// create 모달창에서 확인 버튼 눌렀을 경우 
function onClickAddGoalConfirmBtn() {
    add_goal_confirm_btn.addEventListener('click', (e) => {
        // axios
        axiosAddGoal();
        modal_create.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// create 모달창에서 뒤로가기 버튼 눌렀을 경우 
function onClickAddGoalBackBtn() {
    add_goal_back_btn.addEventListener('click', (e) => {
        modal_create.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// edit 모달창에서 확인 버튼 눌렀을 경우 
function onClickEditGoalConfirmBtn(id) {
    edit_goal_confirm_btn.addEventListener("click", (e) => {
        // axios 
        axiosEditGoal(id);
        modal_edit.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// edit 모달창에서 삭제 버튼 눌렀을 경우 
function onClickEditGoalDeleteBtn(id) {
    edit_goal_delete_btn.addEventListener('click', (e) => {
        modal_edit.classList.remove('active');
        modal_delete.classList.add('active');
        onClickDeleteGoalExitBtn();
        onClickDeleteGoalDeleteBtn(id);
    })
}

// delete 모달창에서 취소 버튼 눌렀을 경우 
function onClickDeleteGoalExitBtn() {
    delete_goal_exit_btn.addEventListener('click', (e) => {
        modal_delete.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// delete 모달창에서 삭제 버튼 눌렀을 경우 
function onClickDeleteGoalDeleteBtn(id) {
    delete_goal_delete_btn.addEventListener('click', (e) => {
        // 목표 삭제 axios 요청 
        axiosDeleteGoal(id);
        modal_delete.classList.remove('active');
        modal_background.classList.remove('active');
    })
}

// add 모달 창에서 색상 선택했을 경우 
function onClickAddModalColor() {
    add_modal_color_btn.forEach((elem) => {
        elem.addEventListener('click', (e) => {
            if (e.target.classList.contains('active') == false) inItColorPicker(e);
            var color = getColorId(e);
            console.log('이건 생성');
            modal_create_input.style.borderBottom = "1px solid " + color;
            modal_create_input.style.color = color;
        })
        
    })
}

// edit 모달 창에서 색상 선택했을 경우 
function onClickEditModalColor() {
    edit_modal_color_btn.forEach((elem) => {
        elem.addEventListener('click', (e) => {
            if (e.target.classList.contains('active') == false) inItColorPicker(e);
            var color = getColorId(e);
            console.log('이건 수정');
            modal_edit_input.style.borderBottom = "1px solid " + color;
            modal_edit_input.style.color = color;
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

// 목표 추가한 것 dom조작
function addGoalDom(title, color, id) {
    var liTag = document.createElement('li');
    var strongTag = document.createElement('strong');
    var buttonTag = document.createElement('button');

    liTag.classList.add('goal-list__item');

    strongTag.classList.add('goal-list__name');
    strongTag.innerHTML = title;
    strongTag.style.color = color;
    strongTag.id = id;

    buttonTag.classList.add('goal-list__edit-btn');
    buttonTag.setAttribute('type', 'button');
    buttonTag.innerHTML = '<i class="fas fa-chevron-right"></i>';

    liTag.append(strongTag);
    liTag.append(buttonTag);
    goal_list_contents.append(liTag);
}

// 목표 수정한 것 돔조작
function editGoalDom(title, color, id) { 
    var strongTag = document.getElementById(id);
    strongTag.innerHTML = title;
    strongTag.style.color = color;
}

// 목표 삭제한 것 돔조작 
function deleteGoalDom(id) {
    var strongTag = document.getElementById(id).parentElement;
    strongTag.remove();
}

// axios 목표등록 
let axiosAddGoal = async function() {
    var title = modal_create_input.value;
    var color;
    var ID = makeGoalId();
    add_modal_color_btn.forEach((elem) => {
        if (elem.classList.contains('active')) {
            color = elem.style.backgroundColor;
        }
    })
    const params = {
        title: title,
        color: color,
        goalKey: ID
    }
    requestGet('/addGoal', params)
        .then(res => {
            if (res === false) return; 
            goalId.push(ID);
            addGoalDom(title, color, ID);
            edit_modal_color_btn = document.querySelectorAll('.color-picker__list-item.edit');
            onClickGoalList();
            num_of_goal++;
        })
}

// axios 목표수정 
let axiosEditGoal = async function(id) {
    var title = modal_edit_input.value;
    var color;
    edit_modal_color_btn.forEach((elem) => {
        if (elem.classList.contains('active')) {
            color = elem.style.backgroundColor;
        }
    })
    const params = {
        title: title,
        color: color,
        goalKey: id
    }
    requestGet('/fixGoal', params)
        .then(res => {
            if (res === false) return;
            editGoalDom(title, color, id);
        })
}

// axios 목표삭제 
let axiosDeleteGoal = async function(id) {
    const params = {
        goalKey: id
    }
    requestGet('/deleteGoal', params)
        .then(res => {
            if (res === false) return;
            deleteGoalDom(id);
            num_of_goal--;
        })
}

function init() {
    onClickAddGoal();
    onClickGoalList();
    onClickAddModalColor();
    onClickEditModalColor();
}
init();