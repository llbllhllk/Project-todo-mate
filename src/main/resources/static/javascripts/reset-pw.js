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

document.addEventListener('keydown', (e) => {
  if(e.keyCode == 13) {
    e.preventDefault();  
  }
});

async function requestPost(url, data) {
  try {
    const options = {
      method: "POST",
      url,
      data,
    };
    const res =  await axios(options);
    console.log(res.data);
    return res.data;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showPwAlert() {
  const validationCb = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[~@$^!%<>&*?&])[A-Za-z\d~@$^!%<>&*?&]{8,20}$/;
  
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
}

function showPwEnterAlert() {
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
}

function onClickChangePwBtn() {
  changePwBtn.addEventListener('click', (e) => {
    showPwAlert()
    showPwEnterAlert();
    if(validPw && validPwEnter === true) {
      const password = {
        password: SHA256(userPwEnter.value),
      }
      requestPost('/changePw', password).then(res => {
        modalHandler(res);
        modal.classList.add('active');
      });
    }
  });
}

function modalHandler(res) {
  if(res === true) {
    loginBtn.addEventListener('click', (e) => {
      location.href = "login";
    });
    deleteBtn.addEventListener('click', (e) => {
      modal.classList.remove('active');
    });
  }
}

function init() {
  onClickChangePwBtn();
  modalHandler();
}

init();
