'use strict'

// Btn
const sendingCertificationBtn = document.querySelector('#sending-certification');
const enteringCertificationBtn = document.querySelector('#confirm-certification');

// Input
const userId = document.querySelector('#user-id');
const userEmail = document.querySelector('#user-email');
const userCertification = document.querySelector('#user-certification');

// List
const list = document.querySelector('.find-pw__list.close');

// Alert
const emptyId = document.querySelector('#empty-id');
const wrongId = document.querySelector('#wrong-id');
const emptyEmail = document.querySelector('#empty-email');
const wrongEmail = document.querySelector('#wrong-email');
const correctEmail = document.querySelector('#correct-email');
const emptyCertification = document.querySelector('#empty-certification');
const wrongCertification = document.querySelector('#wrong-certification');
const correctCertification = document.querySelector('#correct-certification');
const timeoutCertification = document.querySelector('#timeout-certification');

// Timer
const timer = document.querySelector('.find-pw__timer');
let interval;

async function postUserId() {
  try {
    const idUrl = '/validId';
    const resValidId = await axios(idUrl, {
      "user-id": userId.value
    })
    const validId = resValidId.data;
    showIdAlert(validId);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showIdAlert(validId) {
  // 아이디를 입력하지 않을 경우
  if(userId.value === "") {
    emptyId.classList.add('active');
    wrongId.classList.remove('active');
  }
  // 가입하지 않는 아이디면
  if(validId === false) {
    emptyId.classList.remove('active');
    wrongId.classList.add('active');
  }
}

async function postUserEmail() {
  try {
    const emailUrl = '/validEmail';
    const resValidEmail = await axios(emailUrl, {
      "user-email": userEmail.value
    })
    const validEmail = resValidEmail.data;
    showEmailAlert(validEmail);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showEmailAlert(validEmail) {
  // 이메일을 입력하지 않을 경우
  if(userEmail.value === "") {
    emptyEmail.classList.add('active');
    wrongEmail.classList.remove('active');
    correctEmail.classList.remove('active');
  }
  // 가입한 이메일이 아닐 경우 
  if(validEmail === false) {
    emptyEmail.classList.remove('active');
    wrongEmail.classList.add('active');
    correctEmail.classList.remove('active');
  } else {
    // 가입한 이메일인 경우
    emptyEmail.classList.remove('active');
    wrongEmail.classList.remove('active');
    correctEmail.classList.add('active');
  }
}

async function postTimeoutCertification() {
  try {
    const timeoutCertificationUrl = '/timeoutCertification'
    const resTimeoutCertification = await axios(timeoutCertificationUrl, {
      timeout: true,
    })
    const validCertification = resTimeoutCertification.data;
    showCertificationAlert(validCertification);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function onTimer() {
  let minutes = 2;
  let front = 5;
  let back = 9;
  interval = setInterval(() => {
    timer.innerText = `${minutes}:${front}${back}`;
    if(front === 0 && back === 0) {
      minutes--;
      back = 9;
      front = 5;
    } else if(back === 0) {
      front--;
      back = 9;
    } else {
      back--;
    }
    if(minutes < 0) {
      clearInterval(interval);
    }
  }, 1000);
  // 제한시간 안에 인증번호 입력 못할 경우
  emptyCertification.classList.remove('active');
  wrongCertification.classList.remoe('active');
  correctCertification.classList.remove('active');
  timeoutCertification.classList.add('active');
  postTimeoutCertification();
}

async function postUserCertification() {
  try {
    const certificationUrl = '/validCertification';
    const resValidCertification = await axios(certificationUrl, {
      "user-certification": userCertification.value
    })
    const validCertification = resValidCertification.data;
    console.log(validCertification);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showCertificationAlert(validCertification) {
  // 인증번호를 입력하지 않았을 경우
  if(userCertification.value === '') {
    emptyCertification.classList.add('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.remove('active');
    timeoutCertification.classList.remove('active');
  }
  // 인증번호가 틀릴 경우
  if(validCertification === false) {
    emptyCertification.classList.remove('active');
    wrongCertification.classList.add('active');
    correctCertification.classList.remove('active');
    timeoutCertification.classList.remove('active');
  } else {
    // 인증번호가 맞을 경우
    emptyCertification.classList.remove('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.add('active');
    timeoutCertification.classList.remove('active');
    // 비밀번호 재설정 페이지로 이동
    location.href = "reset-pw";
  }
}

function init() {
  userId.addEventListener('blur', (e) => {
    postUserId();
  });
  
  userEmail.addEventListener('blur', (e) => {
    postUserEmail();
  })

  sendingCertificationBtn.addEventListener('click', (e) => {
    // id와 email이 둘다 가입된 경우  
    if(validId && validEmail === true) {
      list.classList.remove('close');
      clearInterval(interval);
      onTimer();
    }
  });

  enteringCertificationBtn.addEventListener('click', (e) => {
    postUserCertification();
  });
}

init();