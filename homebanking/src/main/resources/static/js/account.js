let app = new Vue({
    el: "#app",
    data: {
        clients: {},
        accounts: {},
        account: {},
        transactions: [],
        isAdmin: false,


    },

    created() {
        this.loadData()

    },

    methods: {

        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.clients = response.data
                    this.accounts = response.data

                    if (this.clients.email.includes("fermin.colorado@mindhub.com")) {
                        this.isAdmin = true
                    }

                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },


        loadData() {

            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');

            console.log(urlParams)

            axios.get(`/api/accounts/${id}`)
                .then(response => {
                    this.account = response.data
                    this.transactions = response.data.transactions

                    console.log(this.transactions)

                    // this.transactions.sort((a, b) => { return a.date - b.date })
                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })

        },

        signOut() {

            Swal.fire({
                title: 'Signed out!',
                // html,
                icon: "success",
                showConfirmButton: false,
                // confirmButtonText: 'false',
                // footer: '<span class="fotterSweet">Lorem ipsum dolor sit amet consectetur adipisicing elit.</span>',
                // width: '40%',
                // padding: '1rem',
                // background: 'trasnparent',
                // grow,
                // backdrop,

            })

            setTimeout(() => {
                axios.post('/api/logout').then(response => { window.location.href = "/web/home.html" })

            }, 2000);

        }



    }
})