const toggleBtn = document.querySelector('.nav_toogle');
const menu = document.querySelector('#nav_menu');
const login = document.querySelector('#nav_login');

toggleBtn.addEventListener('click', () => {
    menu.classList.toggle('acctive');
    login.classList.toggle('acctive');
});