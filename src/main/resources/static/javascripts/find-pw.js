'use strict'

// Button
const sendingCertificationBtn = document.querySelector('#sending-certification');
const enteringCertificationBtn = document.querySelector('#confirm-certification');

// Input
const userId = document.querySelector('#user-id');
const userEmail = document.querySelector('#user-email');
const userCertification = document.querySelector('#user-certification');

// List
const form = document.querySelector('.find-pw__list.close');

// Alert
const emptyId = document.querySelector('#empty-id');
const emptyEmail = document.querySelector('#empty-email');
const wrongInfo = document.querySelector('#wrong-info');
const correctBoth = document.querySelector('#correct-both');
const emptyCertification = document.querySelector('#empty-certification');
const wrongCertification = document.querySelector('#wrong-certification');
const correctCertification = document.querySelector('#correct-certification');
const timeoutCertification = document.querySelector('#timeout-certification');

// Timer
const timer = document.querySelector('.find-pw__timer');
let interval;

document.addEventListener('keydown', (e) => {
  if(e.keyCode == 13) {
    e.preventDefault();  
  }
});

async function requestPost(url, data) {
  try {
    const options = {
      method: "POST",
      url: url,
      data
    };
    const res =  await axios(options);
    console.log(res.data);
    return res.data;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function emptyInputHandler() {
  // 아이디를 입력하지 않을 경우
  userId.addEventListener('blur', (e) => {
    if(userId.value === "") {
      emptyId.classList.add('active');
    } else {
      emptyId.classList.remove('active');
    }
  });
  // 이메일을 입력하지 않을 경우
  userEmail.addEventListener('blur', (e) => {
    if(userEmail.value === "") {
      emptyEmail.classList.add('active');
      wrongInfo.classList.remove('active');
      correctBoth.classList.remove('active');
    } else {
      emptyEmail.classList.remove('active');
    }
  });
  // 인증번호를 입력하지 않았을 경우
  userCertification.addEventListener('blur', (e) => {
    if(userCertification.value === '') {
      emptyCertification.classList.add('active');
      wrongCertification.classList.remove('active');
      correctCertification.classList.remove('active');
      timeoutCertification.classList.remove('active');
    } else {
      emptyCertification.classList.remove('active');
    }
  });
}

function showUserInfoAlert(validUserInfo) {
  // 아이디, 이메일이 가입되어 있는 경우
  if(validUserInfo === true) {
    emptyId.classList.remove('active');
    emptyEmail.classList.remove('active');
    wrongInfo.classList.remove('active');
    correctBoth.classList.add('active')
    form.classList.remove('close');
    onTimer();
  } else {
    // 아이디, 이메일이 하나라도 가입되어 있지 않을 경우
    emptyId.classList.remove('active');
    emptyEmail.classList.remove('active');
    wrongInfo.classList.add('active');
    correctBoth.classList.remove('active')
    form.classList.add('close');
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
      // 제한시간이 초과됐을 경우
      clearInterval(interval);
      const timeout = {
        timeout: true,
      }
      requestPost('/timeoutCertification', timeout).then(res => showTimeoutAlert(res));
    }
  }, 1000);
}

function showTimeoutAlert(timeout) {
  if(timeout === true) {
    emptyCertification.classList.remove('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.remove('active');
    timeoutCertification.classList.add('active');
  }
}

function showCertificationAlert(id) {
  const resId = id;
  // 인증번호가 틀릴 경우
  if(resId === "") {
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
    location.href = "reset-pw";
  }
}

function onClickSendingCertificationBtn() {
  sendingCertificationBtn.addEventListener('click', (e) => {
    const userInfo = {
      id: userId.value,
      email: userEmail.value,
    }
    requestPost('/validInfo', userInfo).then(res => showUserInfoAlert(res));
  });
}

function onClickEnteringCertificationBtn() {
  enteringCertificationBtn.addEventListener('click', (e) => {
    const certification = {
      certification: userCertification.value,
    }
    requestPost('/validCertification', certification).then(res => showCertificationAlert(res));
  });
}

function init() {
  emptyInputHandler();
  onClickSendingCertificationBtn();
  onClickEnteringCertificationBtn();
}

init();