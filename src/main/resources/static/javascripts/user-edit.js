'use strict'

// Btn
const editAccountBtn = document.querySelector('#edit-account');
const logoutBtn = document.querySelector('#logout');
const secessionBtn = document.querySelector('#secession');
const xBtns = document.querySelectorAll('.modal__close-btn');
const cancelBtns = document.querySelectorAll('.modal__btn.cancel');

// Modal
const logoutModal = document.querySelector('.modal.logout');
const secessionModal = document.querySelector('.modal.secession');

function editAccountBtnHandler() {
  editAccountBtn.addEventListener('click', () => {
    location.href="edit-account.html";
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
  editAccountBtnHandler();
  logoutModalHandler();
  secessionModalHandler();
  deleteBtnHandler();
}

init();