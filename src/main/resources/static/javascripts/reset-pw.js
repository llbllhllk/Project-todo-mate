'use strict'

// Btn
const changePwBtn = document.querySelector('#confirm-pw');
const loginBtn = document.querySelector('.modal__login-btn');
const deleteBtn = document.querySelector('.modal__close-btn');

// Input
const userPw = document.querySelector('#user-pw');
const userPwEnter = document.querySelector('#user-pw-enter');

// Alert
const emptyPw = document.querySelector('#empty-user-pw');
const wrongPw = document.querySelector("#wrong-user-pw");
const emptyPwEnter = document.querySelector('#empty-user-pw-enter');
const wrongPwEnter = document.querySelector('#wrong-user-pw-enter');

// Valid
let validPw = false;
let validPwEnter = false;

// Modal
const modal = document.querySelector('.modal');

function showPwAlert() {
  const validationCb =
    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[~@$^!%<>&*?&])[A-Za-z\d~@$^!%<>&*?&]{8,20}$/;

  userPw.addEventListener("blur", () => {
    // 비밀번호를 입력하지 않았을 경우
    if (userPw.value === "") {
      emptyPw.classList.add("active");
      wrongPw.classList.remove("active");
      // 유효성검사에 부적합할 경우
    } else if (validationCb instanceof RegExp) {
      if (!validationCb.test(userPw.value)) {
        validPw = false;
        wrongPw.classList.add("active");
        emptyPw.classList.remove("active");
      } else {
        // 유효성 검사에 적합할 경우
        validPw = true;
        wrongPw.classList.remove("active");
        emptyPw.classList.remove("active");
      }
    }
  });
}

function showPwEnterAlert() {
  userPwEnter.addEventListener('blur', (e) => {
    // 비밀번호 확인을 입력하지 않았을 경우
    if(userPwEnter.value === "") {
      emptyPwEnter.classList.add('active');
      wrongPwEnter.classList.remove('active');
      // 비밀번호 확인이 유효성검사에 부적합할 경우
    } else if(userPwEnter.value !== userPw.value) {
      validPwEnter = false;
      emptyPwEnter.classList.remove('active');
      wrongPwEnter.classList.add('active');
    } else {
      // 비밀번호 확인히 유효성 검사에 적합할 경우
      validPwEnter = true;
      emptyPwEnter.classList.remove('active');
      wrongPwEnter.classList.remove('active');
    }
  });
}

async function postUserPw() {
  try {
    const pwUrl = '/changePw';
    const resPw = await axios(pwUrl, {
      password: userPwEnter.value
    })
    const changedPw = resPw.data;
    return changedPw;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function onClickChangePwBtn() {
  // '재설정 하기'버튼을 클릭시 재설정된 비밀번호를 서버에게 전송하고 로그인페이지로 이동
  changePwBtn.addEventListener('click', (e) => {
    if(validPw && validPwEnter === true) {
      const changedPw = postUserPw();
      if(changedPw === true) {
        modal.classList.add('active');
      } else {
        modal.classList.remove('active');
      }
    } else {
      modal.classList.remove('active');
    }
  });
}

function modalHandler() {
  loginBtn.addEventListener('click', () => {
    location.href = "login";
  });
  deleteBtn.addEventListener('click', () => {
    modal.classList.remove('active');
  });
}

function init() {
  showPwAlert();
  showPwEnterAlert();
  onClickChangePwBtn();
  modalHandler();
}

init();