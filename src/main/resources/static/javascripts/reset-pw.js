'use strict'

// Btn
const changePwBtn = document.querySelector('#confirm-pw');

// Input
const userPw = document.querySelector('#user-pw');
const userPwEnter = document.querySelector('#user-pw-enter');

// Alert
const emptyPw = document.querySelector('#empty-user-pw');
const wrongPw = document.querySelector("#wrong-user-pw");
const emptyPwEnter = document.querySelector('#empty-user-pw-enter');
const wrongPwEnter = document.querySelector('#wrong-user-pw-enter');

function showPwAlert() {
  const validationCb =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[~@$^!%<>&*?&])[A-Za-z\d~@$^!%<>&*?&]{8,20}$/;

  userPw.addEventListener("blur", () => {
    if (userPw.value === "") {
      emptyPw.classList.add("active");
      wrongPw.classList.remove("active");
    } else if (validationCb instanceof RegExp) {
      if (!validationCb.test(userPw.value)) {
        wrongPw.classList.add("active");
        emptyPw.classList.remove("active");
      } else {
        wrongPw.classList.remove("active");
        emptyPw.classList.remove("active");
      }
    }
  });
}

function showPwEnterAlert() {
  userPwEnter.addEventListener('blur', (e) => {
    if(userPwEnter.value === "") {
      emptyPwEnter.classList.add('active');
      wrongPwEnter.classList.remove('active');
    } else if(userPwEnter.value !== userPw.value) {
      emptyPwEnter.classList.remove('active');
      wrongPwEnter.classList.add('active');
    } else {
      emptyPwEnter.classList.remove('active');
      wrongPwEnter.classList.remove('active');
    }
  });
}

function onClickChangePwBtn() {
  changePwBtn.addEventListener('click', (e) => {
    // '재설정 하기'버튼을 클릭시 재설정된 비밀번호를 서버에게 전송하고 로그인페이지로 이동
  });
}

function init() {
  showPwAlert();
  showPwEnterAlert();
  onClickChangePwBtn();
}

init();