'use strict'

// Btn
const doubleCheckBtn = document.querySelector('#double-check-user-nickname');
const changeNicknameBtn = document.querySelector('#edit-user-nickname');
const modalDeleteBtn = document.querySelector('.modal__close-btn');
const modalEnterBtn = document.querySelector('.modal__login-btn');

// Input
const userNickname = document.querySelector('#user-nickname');

// Alert
const emptyNickname = document.querySelector('#empty-user-nickname');
const doubleCheckedNickname = document.querySelector('#double-checked-user-nickname');
const correctNickname = document.querySelector('#correct-user-nickname');

// Modal
const modal = document.querySelector('.modal');

async function postCheckNickname() {
  try {
    const checkNicknameUrl = '/checkNickname';
    const resCheckNickname = await axios(checkNicknameUrl, {
      nickname: userNickname.value,
    })
    const checkedNickname = resCheckNickname.data;
    return checkedNickname;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function alertHandler() {
  doubleCheckBtn.addEventListener('click', (e) => {
    const checkedNickname = postCheckNickname();
    // 닉네임을 입력하지 않았을 경우
    if(userNickname.value === '') {
      emptyNickname.classList.add('active');
      doubleCheckedNickname.classList.remove('active');
      correctNickname.classList.remove('active');
      // 닉네임이 중복됬을 경우
    } else if(checkedNickname === true) {
      emptyNickname.classList.remove('active');
      doubleCheckedNickname.classList.add('active');
      correctNickname.classList.remove('active');
    } else {
      // 닉네임이 중복되지 않았을 경우
      emptyNickname.classList.remove('active');
      doubleCheckedNickname.classList.remove('active');
      correctNickname.classList.add('active');
      changeNicknameBtnHandler();
    }
  });
}

async function postResetNickname() {
  try {
    const resetNicknameUrl = '/resetNickname';
    const resResetNickname = await axios(resetNicknameUrl, {
      nickname: userNickname.value,
    })
    const validReset = resResetNickname.data;
    return validReset;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function changeNicknameBtnHandler() {
  postResetNickname();
  changeNicknameBtn.addEventListener('click', () => {
    modal.classList.add('active');
  });
}

function modalHandler() {
  modalDeleteBtn.addEventListener('click', () => {
    modal.classList.remove('active');
  });
  modalEnterBtn.addEventListener('click', () => {
    location.href = "user-edit";
  });
}

function init() {
  alertHandler();
  changeNicknameBtnHandler();
  modalHandler()
}

init();