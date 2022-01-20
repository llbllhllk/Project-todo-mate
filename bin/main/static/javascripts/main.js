"use strict";
// Handling Tab Button
const tabBtn = document.querySelector(".header__tab-button");
const tab = document.querySelector(".tab");
const tabDeleteBtn = document.querySelector(".tab__delete-button");

function handleTabBtn() {
  tabBtn.addEventListener("click", function (e) {
    tab.classList.add("active");
  });

  tabDeleteBtn.addEventListener("click", function (e) {
    tab.classList.remove("active");
  });
}

// Init
function init() {
  handleTabBtn();
}

init();
