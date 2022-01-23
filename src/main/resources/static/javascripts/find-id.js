'use strict'

// Button
const sendingCertificationBtn = document.querySelector('#sending-certification');
const enteringCertificationBtn = document.querySelector('#confirm-certification');

// Input
const userEmail = document.querySelector('#user-email');
const userCertification = document.querySelector('#user-certification');

// List
const list = document.querySelector('.find-id__list.close');

// Alert
const emptyEmail = document.querySelector('#empty-email');
const worngEmail = document.querySelector('#wrong-email');
const correctEmail = document.querySelector('#correct-email');
const emptyCertification = document.querySelector('#empty-certification');
const wrongCertification = document.querySelector('#wrong-certification');
const correctCertification = document.querySelector('#correct-certification');
const timeoutCertification = document.querySelector('#timeout-certification');

// Timer
const timer = document.querySelector('.find-id__timer');
let interval;

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
      // 제한시간 안에 인증번호 입력 못할 경우
    }
  }, 1000);
}

function showEmailAlert() {
  if(userEmail.value === "") {
    emptyEmail.classList.add('active');
    worngEmail.classList.remove('active');
    correctEmail.classList.remove('active')
    // 이메일이 틀렸을 경우 알림
  } else {
    // 존재하는 이메일인 경우 알림과 인증번호 보내는 창 띄움
    emptyEmail.classList.remove('active');
    worngEmail.classList.remove('active');
    correctEmail.classList.remove('active')
    list.classList.remove('close');
  }
}

function onClickSendingCertificationBtn() {
  sendingCertificationBtn.addEventListener('click', (e) => {
    showEmailAlert();
    clearInterval(interval);
    onTimer();
  });
}

function showCertificationAlert () {
  if(userCertification.value === "") {
    emptyCertification.classList.add('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.remove('active');
    // 인증번호가 틀렸을 경우 틀렸다는 알림
    // 인증번호가 맞을 경우 알림과 사용자의 아이디 modal로 출력
  }
}

function onClickEnteringCertificationBtn() {
  enteringCertificationBtn.addEventListener('click', (e) => {
    showCertificationAlert();
  });
}

function init() {
  onClickSendingCertificationBtn();
  onClickEnteringCertificationBtn();
}

init();