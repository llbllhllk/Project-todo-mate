/**
 * @typedef InputInfo
 * @property {string} id
 * @property {(value: string) => boolean} validationPw
 * @property {boolean} isValid
 * @property {boolean} isEmpty
 * @property {string} data
 */

// @type {InputInfo[]}
const inputInfoArr = [
  {
    id: 'userId',
    isValid: false,
    isEmpty: true,
    alert: "아이디",
    data: "",
  },
  {
    id: 'userPw',
    validationPw: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[~@$^!%<>&*?&])[A-Za-z\d~@$^!%<>&*?&]{8,20}$/,
    isValid: false,
    isEmpty: true,
    alert: "비밀번호",
  },
  {
    id: 'userPwConfirm',
    isValid: false,
    isEmpty: true,
    alert: "비밀번호 확인",
    data: "",
  },
  {
    id: 'userName',
    isEmpty: true,
    alert: "이름",
    data: "",
  },
  {
    id: 'userNickname',
    isEmpty: true,
    alert: "닉네임",
    data: "",
  },
  {
    id: 'userEmail',
    validationEmail: /^([\w\.\_\-])*[a-zA-Z0-9]+([\w\.\_\-])*([a-zA-Z0-9])+([\w\.\_\-])+@([a-zA-Z0-9]+\.)+[a-zA-Z0-9]{2,8}$/,
    isValid: false,
    isEmpty: true,
    alert: "이메일",
    data: "",
  },
];

async function requestPost(url, data) {
  try {
    const options = {
      method: "POST",
      url,
      data,
    };
    const res =  await axios(options);
    console.log(res.data);
    return res.data;
  } catch(err) {
    console.log(err);
    throw new Error(err);
  }
}

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
  let validationPw = inputInfo.validationPw;
  let validationEmail = inputInfo.validationEmail;

  const signErrElem = elem.parentElement.parentElement.querySelector('.sign_err');
  const emailErrElem = elem.parentElement.parentElement.querySelector('.email_err');
  const infoElem = elem.parentElement.parentElement.querySelector('.sign_info');

  if (value === "") {
    infoElem.classList.remove('hidden');
    inputInfo.isEmpty = true;
    return;
  } else {
    inputInfo.data = value;
  }
  
  // 아이디
  if(inputInfo.id === 'userId') {
    const idElem = document.querySelector('#userId');
    const userId = {
      id: idElem.value,
    }
    requestPost('/validSignUpIdDuplicate', userId).then(res => {
      if(res === false) {
        signErrElem.classList.remove('hidden');
        inputInfo.isValid = false;
      } else if(res === true) {
        signErrElem.classList.add('hidden');
        inputInfo.isValid = true;
      } else {
        inputInfo.isEmpty = false;
      }
      turnCheck(elem);
    });
  }

  // 비밀번호
  if (validationPw instanceof RegExp) {
    if (validationPw.test(value)) {
      signErrElem.classList.add('hidden');
      inputInfo.isValid = true;
    }
    else {
      signErrElem.classList.remove('hidden');
      inputInfo.isValid = false;
    }
  } else {
    inputInfo.isEmpty = false;
  }
  
  // 비밀번호 확인
  if (inputInfo.id === 'userPwConfirm') {
    const pwElem = document.querySelector('#userPw');
    if (pwElem.value === value) {
      signErrElem.classList.add('hidden');
      inputInfo.isValid = true;
    }
    else {
      signErrElem.classList.remove('hidden');
      inputInfo.isValid = false;
    }
  } else {
    inputInfo.isEmpty = false;
  }

  // 닉네임
  if(inputInfo.id === 'userNickname') {
    const nicknameElem = document.querySelector('#userNickname');
    const userNickname = {
      nickname: nicknameElem.value,
    }
    requestPost('/validSignUpNicknameDuplicate', userNickname).then(res => {
      if(res === false) {
        signErrElem.classList.remove('hidden');
        inputInfo.isValid = false;
      } else if (res === true){
        signErrElem.classList.add('hidden');
        inputInfo.isValid = true;
      } else {
        inputInfo.isEmpty = false;
      }
      turnCheck(elem);
    });
  }

  // 이메일 
  if (validationEmail instanceof RegExp) {
    const emailElem = document.querySelector('#userEmail');
    const userEmail = {
      email: emailElem.value,
    }
    if(validationEmail.test(emailElem.value)) {
      requestPost('/validSignUpEmailDuplicate', userEmail).then(res => {
        if(res === false) {
          signErrElem.classList.add('hidden');
          emailErrElem.classList.remove('hidden');
          inputInfo.isValid = false;
        } else if(res === true) {
          signErrElem.classList.add('hidden');
          emailErrElem.classList.add('hidden');
          inputInfo.isValid = true;
        } else {
          inputInfo.isEmpty = false;
        }
        turnCheck(elem);
      });
    } else {
      emailErrElem.classList.add('hidden');
      signErrElem.classList.remove('hidden');
    }
  }
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
    if (isValid === true && isEmpty === false) {
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
const submit = document.querySelector('.sign-up_button button');
const modal = document.querySelector('.modal.close');
const deleteBtn = document.querySelector('.modal__close-btn');
const loginBtn = document.querySelector('.modal__login-btn');

submit.addEventListener('click', () => {
  const main = document.querySelector('.sign-up');
  if (!main) {
    return;
  } else {
    for (const key in inputInfoArr) {
      const inputInfo = inputInfoArr[key];
      if (inputInfo.isEmpty) {
        alert(inputInfo.alert + " 창이 비어있습니다!");
        return;
      }
    }
    const userInfo = {
      id: inputInfoArr[0].data,
      password: inputInfoArr[2].data,
      name: inputInfoArr[3].data,
      nickname: inputInfoArr[4].data,
      email: inputInfoArr[5].data,
    }
    requestPost('/postSignUp', userInfo).then(res => {
      if(res === true) {
        modal.classList.remove('close');
        deleteBtn.addEventListener('click', () => {
          modal.classList.add('close');
        });
        loginBtn.addEventListener('click', () => {
          location.href = "login";
        });
      } else {
        modal.classList.add('close');
      }
    });
  }
});
