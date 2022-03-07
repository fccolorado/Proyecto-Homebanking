let app = new Vue({
    el: "#app",
    data: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',

    },

    methods: {
        signUp() {
            if (this.firstName != '' && this.lastName != '' && this.email != '' && this.password != '') {
                Swal.fire({
                    title: 'REGISTERED!',
                    text: `¡Welcome ${this.firstName.toUpperCase()} ${this.lastName.toUpperCase()}!`,
                    icon: "success",
                    showConfirmButton: false,
                    timer: 2500,
                })
                setTimeout(() => {
                    axios.post('/api/clients', "firstName=" + this.firstName + "&lastName=" + this.firstName + "&email=" + this.email + "&password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(response => { return window.location.href = "/web/index.html" })

                    .catch(error => console.log(`Error en: ${error}`))
                }, 2000);
            } else {
                Swal.fire({
                    // title: 'Invalid combination',
                    text: 'Please fill in all the spaces',
                    icon: "error",
                    showConfirmButton: false,
                    timer: 3000,
                })

            }


        },
    }

})