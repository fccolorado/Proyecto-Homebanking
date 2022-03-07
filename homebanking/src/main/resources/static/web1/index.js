let app = new Vue({
    el: "#app",
    data: {
        email: '',
        password: '',
    },

    methods: {
        signIn() {
            axios.post('/api/login', "email=" + this.email + "&password=" + this.password, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => { window.location.href = "/web/accounts.html" }, )

            .catch(error => {

                Swal.fire({
                    title: 'Invalid combination',
                    text: 'Please re-enter your details',
                    // html,
                    icon: "error",
                    showConfirmButton: false,
                    // confirmButtonText: 'false',
                    // footer: '<span class="fotterSweet">Lorem ipsum dolor sit amet consectetur adipisicing elit.</span>',
                    // width: '40%',
                    // padding: '1rem',
                    // background: 'trasnparent',
                    // grow,
                    // backdrop,
                    timer: 2000,
                })

            })

        },

    }
})