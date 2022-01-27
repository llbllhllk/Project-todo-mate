'use strict'

// Button
const sendingCertificationBtn = document.querySelector('#sending-certification');
const enteringCertificationBtn = document.querySelector('#confirm-certification');
const closingModalBtn = document.querySelector('.modal__clos-btn');

// Input
const userEmail = document.querySelector('#user-email');
const userCertification = document.querySelector('#user-certification');

// List
const list = document.querySelector('.find-id__list.close');

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

async function postUserEmail() {
  try {
    const emailUrl = '/validEmail';
    const resValidEmail = await axios.post(emailUrl, {
      email: userEmail.value,
    });
    const validEmail = resValidEmail.data;
    console.log(validEmail);
    showEmailAlert(validEmail);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

async function postUserCertification() {
  try {
    const certificationUrl = '/validCertification'
    const resUserId = await axios.post(certificationUrl, {
      certification: userCertification.value,  
    })
    const userId = resUserId.data;
    showCertificationAlert(userId);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

async function postTimeoutCertification() {
  try {
    const timeoutCertificationUrl = '/timeoutCertification'
    const resTimeoutCertification = await axios.post(timeoutCertificationUrl, {
      timeout: true,
    })
    const validCertification = resTimeoutCertification.data;
    console.log(validCertification);
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

function showEmailAlert(validEmail) {
  if(userEmail.value === "") {
    // 이메일을 입력하지 않았을 경우
    emptyEmail.classList.add('active');
    wrongEmail.classList.remove('active');
    correctEmail.classList.remove('active');
  } 

  if(validEmail === true) {
    // 존재하는 이메일일 경우
    emptyEmail.classList.remove('active');
    wrongEmail.classList.remove('active');
    correctEmail.classList.add('active')
    // 인증번호 입력창 생성
    list.classList.remove('close');
    clearInterval(interval);
    onTimer();
    onClickEnteringCertificationBtn();

  } else {
    // 이메일이 틀렸을 경우 알림
    emptyEmail.classList.remove('active');
    wrongEmail.classList.add('active');
    correctEmail.classList.remove('active')
  }
}

function showCertificationAlert(id) {
  // 인증번호를 입력하지 않았을 경우
  if(userCertification.value === "") {
    emptyCertification.classList.add('active');
    wrongCertification.classList.remove('active');
    correctCertification.classList.remove('active');
    timeoutCertification.classList.remove('active');
  }
  if(id === "") {
    // 인증번호가 틀렸을 경우 틀렸다는 알림
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
    modalHandler(id);
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
  // 제한시간이 초과됐을 경우
  emptyCertification.classList.remove('active');
  wrongCertification.classList.remoe('active');
  correctCertification.classList.remove('active');
  timeoutCertification.classList.add('active');
  postTimeoutCertification();
}

function onClickSendingCertificationBtn() {
  sendingCertificationBtn.addEventListener('click', (e) => {
    postUserEmail();
  });
}

function onClickEnteringCertificationBtn() {
  enteringCertificationBtn.addEventListener('click', (e) => {
    postUserCertification();
  });
}

function modalHandler(id) {
  // modal의 x버튼을 클릭하면 창 닫힘
  closingModalBtn.addEventListener('click', () => {
    modal.classList.add('close');
  });
  // modal에 해당 사용자의 아이디 출력
  modalUserId.innerText = id;
}

function init() {
  onClickSendingCertificationBtn();
}

init();