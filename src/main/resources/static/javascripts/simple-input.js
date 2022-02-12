'use strict';

// Btn
const addSimpleInputBtn = document.querySelectorAll('.simple-input__goal-btn');
const editSimpleInputBtn = document.querySelectorAll('.simple-input__edit-btn');

// Section
const sections = document.querySelectorAll('.simple-input__content');

// Modal
const editModal = document.querySelector('.modal.edit');

// Date
let date = new Date();
let year = date.getFullYear();
let month = date.getMonth();
let day = date.getDate();

const title = ['시작 날짜', '종료 날짜', '날짜'];

const initData = {
  startDate: `${year}. ${month + 1}. ${day}`,
  endDate: `${year}. ${month + 1}. ${day}`,
  date: '월 화 수 목 금 토 일',
}

function addSimpleInputHandler() {
  addSimpleInputBtn.forEach(btn => {
    btn.addEventListener('click', (e) => {
      const target = e.currentTarget;
      sections.forEach(section => {
        if(section.id === target.parentElement.id) {
          const ul = document.createElement('ul');
          ul.className = "simple-input__list";
          
          const li = document.createElement('li');
          li.className = "simple-input__list-header";

          const h2 = document.createElement('h2');
          h2.className = "simple-input__goal-title";
          h2.innerText = '간편입력 제목';

          const btn = document.createElement('button');
          btn.setAttribute('type', 'button');
          btn.className = "simple-input__edit-btn";

          ul.appendChild(li)
          li.appendChild(h2);
          li.appendChild(btn); 

          let i = 0;
          for(const key in initData) {
            const listItem = document.createElement('li');
            listItem.className = "simple-input__list-item";

            const strong = document.createElement('strong');
            strong.className = "simple-input__list-title";
            strong.innerText = title[i];

            const dateBtn = document.createElement('button');
            dateBtn.setAttribute('type', 'button');
            dateBtn.className = "simple-input__list-data";
            dateBtn.innerText = initData[key];

            ul.appendChild(listItem);
            listItem.appendChild(strong);
            listItem.appendChild(dateBtn);

            i++;
          }
          section.appendChild(ul);
          ul.scrollIntoView('end');
        }
      });
    });
  });
}

function editsimpleInputHandler() {
  editSimpleInputBtn.forEach(btn => {
    btn.addEventListener('click', (e) => {
      console.log('ddd')
      const editBtn = document.querySelector('#edit_modal--edit');
      const deleteBtn = document.querySelector('#edit_modal--delete');
      const cancleBtn = document.querySelector('#edit_modal--cancle');

      // 수정 버튼을 눌렀을 경우
      editBtn.addEventListener('click', (e) => {

      });
      // 삭제 버튼을 눌렀을 경우
      deleteBtn.addEventListener('click', (e) => {

      });
      // 취소 버튼을 눌렀을 경우
      cancleBtn.addEventListener('click', (edit) => {
        editModal.classList.remove('active');
      });
    });
  });   
}

function init() {
  addSimpleInputHandler();
  editsimpleInputHandler();
}

init();