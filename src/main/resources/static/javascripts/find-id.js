'use strict'

// Button
const sendingCertificationBtn = document.querySelector('#sending-certification');
const enteringCertificationBtn = document.querySelector('#confirm-certification');
const closingModalBtn = document.querySelector('.modal__close-btn');

// Input
const userEmail = document.querySelector('#user-email');
const userCertification = document.querySelector('#user-certification');

// List
const form = document.querySelector('.find-id__form.close');

// Modal
const modal = document.querySelector('.modal.close');
const modalUserId = document.querySelector('.modal__user-id');

// Alert
const emptyEmail = document.querySelector('#empty-email');
const wrongEmail = document.querySelector('#wrong-email');
const correctEmail = document.querySelector('#correct-email');
const emptyCertification = document.querySelector('#empty-certification');
const wrongCertification = document.querySelector('#wrong-certification');
const correctCertification = document.querySelector('#correct-certification');
const timeoutCertification = document.querySelector('#timeout-certification');

// Timer
const timer = document.querySelector('.find-id__timer');
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
    return res.data;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showEmailAlert(validEmail) {
  if(validEmail === true) {
    // 존재하는 이메일일 경우
    emptyEmail.classList.remove('active');
    wrongEmail.classList.remove('active');
    correctEmail.classList.add('active')
    // 인증번호 입력창 생성
    form.classList.remove('close');
    clearInterval(interval);
    onTimer();
  } else {
    // 이메일이 틀렸을 경우 알림
    userEmail.value = null;
    emptyEmail.classList.remove('active');
    wrongEmail.classList.add('active');
    correctEmail.classList.remove('active')
  }
}

function showCertificationAlert(userId) {
  if(userId === "") {
    // 인증번호가 틀렸을 경우 틀렸다는 알림
    userCertification.value = null;
    emptyCertification.classList.remove('active');
    wrongCertification.classList.add('active');
    correctCertification.classList.remove('active'); 
    timeoutCertification.classList.remove('active');
  } else {
    // 인증번호가 맞을 경우 알림과 사용자의 아이디 modal로 출력
    emptyCertification.classList.remove('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.add('active');
    timeoutCertification.classList.remove('active');
    modal.classList.remove('close');
    modalHandler(userId);
  }
}

function onTimer(validCertification) {
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

function showTimeoutAlert(validCertification) {
  if(validCertification === true) {
    emptyCertification.classList.remove('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.remove('active');
    timeoutCertification.classList.add('active');
    modal.classList.add('close');
  }
}

function onClickSendingCertificationBtn() {
  sendingCertificationBtn.addEventListener('click', (e) => {
    // 이메일을 입력하지 않았을 경우
    if(userEmail.value === "") {
      emptyEmail.classList.add('active');
      wrongEmail.classList.remove('active');
      correctEmail.classList.remove('active');
    } else {
      const email = {
        email: userEmail.value,
      }
      requestPost('/validEmail', email).then(res => showEmailAlert(res));
    }
  });
}

function onClickEnteringCertificationBtn() {
  enteringCertificationBtn.addEventListener('click', (e) => {
    // 인증번호를 입력하지 않았을 경우
    if(userCertification.value === "") {
      emptyCertification.classList.add('active');
      wrongCertification.classList.remove('active');
      correctCertification.classList.remove('active');
      timeoutCertification.classList.remove('active');
    } else {
      const certification = {
        certification: userCertification.value,
      }
      requestPost('/validCertification', certification).then(res => showCertificationAlert(res));
    }
  });
}

function modalHandler(userId) {
  // modal의 x버튼을 클릭하면 창 닫힘
  closingModalBtn.addEventListener('click', () => {
    modal.classList.add('close');
  });
  // modal에 해당 사용자의 아이디 출력
  modalUserId.innerText = userId;
}

function init() {
  onClickSendingCertificationBtn();
  onClickEnteringCertificationBtn();
}

init();