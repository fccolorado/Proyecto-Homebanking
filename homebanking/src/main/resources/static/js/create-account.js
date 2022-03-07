let app = new Vue({

    el: "#app",
    data: {
        type: "",
        isAdmin: false
    },

    // created() {
    //     this.loadData()
    // },

    methods: {
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.clients = response.data


                    if (this.clients.email.includes("fermin.colorado@mindhub.com")) {
                        this.isAdmin = true
                    }

                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },

        createAccount() {
            Swal.fire({
                title: 'Created account!',
                icon: "success",
                showConfirmButton: false,
                timer: 2000,
            })

            setTimeout(() => {
                axios.post("/api/clients/current/accounts", "type=" + this.type)
                    .then(response => { window.location.href = "/web/accounts.html" })
                    .catch(error => {
                        console.log(`Error en: ${error}`)
                    })
            }, 2000);

        },

        signOut() {

            Swal.fire({
                title: 'Signed out!',
                icon: "success",
                showConfirmButton: false,
            })

            setTimeout(() => {
                axios.post('/api/logout').then(response => { window.location.href = "/web/home.html" })

            }, 2000);

        }





    }
})