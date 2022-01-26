// input
const pw = document.querySelector('.input-pw');

// button
const loginBtn = document.querySelector('.login-form-btn');

function encodePW() {
    pw.addEventListener('blur', (e) => {
        console.log(pw.value);
        console.log(SHA256(pw.value));
    })
}

function init() {
    encodePW();
}

init();