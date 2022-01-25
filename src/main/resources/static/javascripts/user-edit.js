'use strict'

// Btn
const editNickNameBtn = document.querySelector('#edit-nickname');
const editPasswordBtn = document.querySelector('#edit-password');
const logoutBtn = document.querySelector('#logout');
const secessionBtn = document.querySelector('#secession');
const xBtns = document.querySelectorAll('.modal__close-btn');
const cancelBtns = document.querySelectorAll('.modal__btn.cancel');

// Modal
const logoutModal = document.querySelector('.modal.logout');
const secessionModal = document.querySelector('.modal.secession');

function editNickNameBtnHandler() {
  editNickNameBtn.addEventListener('click', () => {
    location.href="reset-nickname";
  });
}

function editPasswordBtnHandler() {
  editPasswordBtn.addEventListener('click', (e) => {
    location.href="reset-pw";
  });
}

function deleteBtnHandler() {
  xBtns.forEach((xBtn) => {
    xBtn.addEventListener('click', () => {
      logoutModal.classList.remove('active');
      secessionModal.classList.remove('active');
    });
  });

  cancelBtns.forEach((cancelBtn) => {
    cancelBtn.addEventListener('click', () => {
      logoutModal.classList.remove('active');
      secessionModal.classList.remove('active');
    });
  });
}

function logoutModalHandler() {
  logoutBtn.addEventListener('click', (e) => {
    logoutModal.classList.add('active');
  });
}

function secessionModalHandler() {
  secessionBtn.addEventListener('click', (e) => {
    secessionModal.classList.add('active');
  });
}

function init() {
  editNickNameBtnHandler();
  editPasswordBtnHandler();
  logoutModalHandler();
  secessionModalHandler();
  deleteBtnHandler();
}

init();