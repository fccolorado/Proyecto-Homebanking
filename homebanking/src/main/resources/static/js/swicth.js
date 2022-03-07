const buttonSwitch = document.querySelector('#switch')

buttonSwitch.addEventListener('click', () => {
    document.body.classList.toggle('dark')
    buttonSwitch.classList.toggle('active')
})