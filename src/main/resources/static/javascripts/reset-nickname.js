"use strict";

// Btn
const doubleCheckBtn = document.querySelector("#double-check-user-nickname");
const changeNicknameBtn = document.querySelector("#edit-user-nickname");
const modalDeleteBtn = document.querySelector(".modal__close-btn");
const modalEnterBtn = document.querySelector(".modal__login-btn");

// Input
const userNickname = document.querySelector("#user-nickname");

// Alert
const emptyNickname = document.querySelector("#empty-user-nickname");
const doubleCheckedNickname = document.querySelector(
  "#double-checked-user-nickname"
);
const correctNickname = document.querySelector("#correct-user-nickname");
const fixNickname = document.querySelector("#fix-user-nickname");

// Modal
const modal = document.querySelector(".modal");

// Check
let check = false;

document.addEventListener("keydown", (e) => {
  if (e.keyCode == 13) {
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
    const res = await axios(options);
    return res.data;
  } catch (err) {
    console.log(err);
    throw new Error(err);
  }
}

function doubleCheckedAlertHandler(checkedNickname) {
  // 중복된 닉네임일 경우
  if (checkedNickname === true) {
    emptyNickname.classList.remove("active");
    doubleCheckedNickname.classList.add("active");
    fixNickname.classList.remove("active");
    correctNickname.classList.remove("active");
  } else {
    // 중복된 닉네임이 아닐 경우
    emptyNickname.classList.remove("active");
    doubleCheckedNickname.classList.remove("active");
    fixNickname.classList.remove("active");
    correctNickname.classList.add("active");
  }
}

function modalHandler(res) {
  if (res === true) {
    modal.classList.add("active");
    modalDeleteBtn.addEventListener("click", (e) => {
      modal.classList.remove("active");
    });
    modalEnterBtn.addEventListener("click", (e) => {
      location.href = "user-edit";
    });
  } else {
    modal.classList.remove("active");
  }
}

function onClickDoubleCheckBtn() {
  doubleCheckBtn.addEventListener("click", (e) => {
    // 닉네임을 입력하지 않았을 경우
    if (userNickname.value === "") {
      emptyNickname.classList.add("active");
      doubleCheckedNickname.classList.remove("active");
      fixNickname.classList.remove("active");
      correctNickname.classList.remove("active");
    } else {
      // 닉네임을 입력했을 경우
      const nickname = {
        nickname: userNickname.value,
      };
      requestPost("/checkNickname", nickname).then((res) => {
        check = true;
        doubleCheckedAlertHandler(res);
      });
    }
  });
}

function onClickChangeNicknameBtn() {
  changeNicknameBtn.addEventListener("click", (e) => {
    if(check === false) {
      emptyNickname.classList.remove("active");
      doubleCheckedNickname.classList.remove("active");
      fixNickname.classList.add("active");
      correctNickname.classList.remove("active");
    } else {
      const nickname = {
        nickname: userNickname.value,
      };
      requestPost("/resetNickname", nickname).then((res) => {
        check = false;
        modalHandler(res);
      });
    }
  });
}

function init() {
  onClickDoubleCheckBtn();
  onClickChangeNicknameBtn();
  modalHandler();
}

init();
