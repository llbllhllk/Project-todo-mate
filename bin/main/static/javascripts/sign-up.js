/**
 * @typedef InputInfo
 * @property {string} id
 * @property {(value: string) => boolean} validationCb
 * @property {boolean} isValid
 * @property {boolean} isEmpty
 * @property {string} data
 */

// @type {InputInfo[]}
const inputInfoArr = [
  {
    id: 'userId',
    isEmpty: true,
    data: "아이디",
  },
  {
    id: 'userPw',
    validationCb: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/,
    isValid: false,
    isEmpty: true,
    data: "비밀번호",
  },
  {
    id: 'userPwConfirm',
    validationCb: 'PW_CONFIRM',
    isValid: false,
    isEmpty: true,
    data: "비밀번호 확인",
  },
  {
    id: 'userName',
    isEmpty: true,
    data: "이름",
  },
  {
    id: 'userNickname',
    isEmpty: true,
    data: "닉네임",
  },
  {
    id: 'userEmail',
    validationCb: /^([\w\.\_\-])*[a-zA-Z0-9]+([\w\.\_\-])*([a-zA-Z0-9])+([\w\.\_\-])+@([a-zA-Z0-9]+\.)+[a-zA-Z0-9]{2,8}$/,
    isValid: false,
    isEmpty: true,
    data: "이메일",
  },
];

// 입력창이 focus되면 메세지들을 없애준다.
document.querySelectorAll('input').forEach((elem) => {
  elem.addEventListener('focus', (e) => {
    const target = e.target;

    if (!(target instanceof HTMLInputElement)) return;
    
    const activeElem =
      target.parentElement.parentElement.querySelector('.sign_info');
    if (activeElem) activeElem.classList.add('hidden');
    
    const signErrElem =
      target.parentElement.parentElement.querySelector('.sign_err');
    if (signErrElem) signErrElem.classList.add('hidden');
    
    const backErrElem =
      target.parentElement.parentElement.querySelector('.back_sign_err');
    if (backErrElem) backErrElem.classList.add('hidden');
  });
});


// 입력창이 change되면 유효성 검사를 해준다.
document.querySelectorAll('input').forEach((elem) => {
  elem.addEventListener('blur', (e) => {
    const target = e.target;
    checkValidation(target);
    turnCheck(target);
  });
});

/**
 * 각 입력창에 대한 유효성을 평가한다.
 * @param {input}
 * @returns {void}
 */
function checkValidation(elem) {
  const {id, value} = elem;

  let inputInfo = inputInfoArr.find((inputObj) => inputObj.id === id);
  let validationCb = inputInfo.validationCb;

  const signErrElem = elem.parentElement.parentElement.querySelector('.sign_err');
  const infoElem = elem.parentElement.parentElement.querySelector('.sign_info');

  if (value === "") {
    infoElem.classList.remove('hidden');
    inputInfo.isEmpty = true;
    return;
  }
  if (validationCb instanceof RegExp) {
    if (validationCb.test(value)) {
      signErrElem.classList.add('hidden');
      inputInfo.isValid = true;
    }
    else {
      signErrElem.classList.remove('hidden');
      inputInfo.isValid = false;
    }
  }
  else if (validationCb === 'PW_CONFIRM') {
    const pwElem = document.querySelector('#userPw');
    if (pwElem.value === value) {
      signErrElem.classList.add('hidden');
      inputInfo.isValid = true;
    }
    else {
      signErrElem.classList.remove('hidden');
      inputInfo.isValid = false;
    }
  }
  inputInfo.isEmpty = false;
}

/**
 * 각 입력창이 모든 유효성을 통과하면 체크를 활성화한다.
 * @param {input}
 * @returns {void}
 */
function turnCheck(elem) {
  let inputInfo = inputInfoArr.find((inputObj) => inputObj.id === elem.id);
  let isValid = inputInfo.isValid;
  let isEmpty = inputInfo.isEmpty;
  
  let signInput = elem.parentElement;

  if (inputInfo.hasOwnProperty('isValid')) {
    if (isValid && !(isEmpty)) {
      signInput.classList.add('valid');
    } else {
      signInput.classList.remove('valid');
    }
  }
  else {
    if (!(isEmpty)) {
      signInput.classList.add('valid');
    } else {
      signInput.classList.remove('valid');
    }
  }
}

/** 
 * 제출했을 때 빈칸인 항목을 팝업창 띄워준다.
 * @type {HTMLButtonElement}
 * */
const submit = document.querySelector('.sign-up_button');
submit.addEventListener('click', () => {
  const main = document.querySelector('.sign-up');
  if (!main) return;

  for (const key in inputInfoArr) {
    const inputInfo = inputInfoArr[key];
    if (inputInfo.isEmpty) {
      alert(inputInfo.data + " 창이 비어있습니다!");
      return;
    }
  }
});