'use strict'

// Btn
const doubleCheckBtn = document.querySelector('#double-check-user-nickname');
const changeNicknameBtn = document.querySelector('#edit-user-nickname');

// Input
const userNickname = document.querySelector('#user-nickname');

// Alert
const emptyNickname = document.querySelector('#empty-user-nickname');
const doubleCheckedNickname = document.querySelector('#double-checked-user-nickname');
const correctNickname = document.querySelector('#correct-user-nickname');

function alertHandler() {
  doubleCheckBtn.addEventListener('click', (e) => {
    console.log(userNickname.value);
    if(userNickname.value === '') {
      emptyNickname.classList.add('active');
      doubleCheckedNickname.classList.remove('active');
      correctNickname.classList.remove('active');
    }
    // 닉네임이 중복될 경우 doubleCheckedNickname에 active클래스 추가
    // 닉네임이 중복되지 않을 경우 correctNickname에 active, valid(check) 클래스 추가 및 중복확인 완료 수정
  });
}

function changeNicknameBtnHandler() {
  changeNicknameBtn.addEventListener('click' () => {
    // 중복확인이 완료되지 않았을 경우 modal__description의 내용 변경
    // 중복확인이 완료됬을 경우 modal__description의 내용 변경 및 changeNicknameBtn의 경로를 main으로 바꾼다.
  });
}

function init() {
  alertHandler();
  changeNicknameBtnHandler();
}

init();