let app = new Vue({

    el: "#app",
    data: {
        clients: {},
        accounts: {},
        amount: "",
        description: "",
        sourceAccountNumber: "",
        destinationAccountNumber: "",
        accountsTrue: [],
        isAdmin: false

    },

    created() {
        this.loadData()
    },

    methods: {
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.clients = response.data
                    this.accounts = response.data.accounts
                    this.accountsTrue = this.accounts.filter(account => account.accountStatus == true)

                    if (this.clients.email.includes("fermin.colorado@mindhub.com")) {
                        this.isAdmin = true
                    }

                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },

        signOut() {
            Swal.fire({
                title: 'Signed out!',
                icon: "success",
                showConfirmButton: false,
                timer: 2000,
            })

            setTimeout(() => {
                axios.post('/api/logout').then(response => { window.location.href = "/web/home.html" })

            }, 2000);

        },

        transfer() {
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes!'
            }).then((result) => {
                if (result.isConfirmed) {

                    Swal.fire({
                            title: 'Successful transfer!',
                            icon: "success",
                            showConfirmButton: false,
                        }

                    )
                    setTimeout(() => {
                        this.createTransfer()

                    }, 2000);
                }
            })

        },

        createTransfer() {
            axios.post("/api/transactions", "amount=" + this.amount + "&description=" + this.description + "&sourceAccountNumber=" + this.sourceAccountNumber + "&destinationAccountNumber=" + this.destinationAccountNumber, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => { window.location.href = "/web/accounts.html" })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },

        showOwnAccounts() {
            document.getElementById('ownAccount').style.display = 'block';
            document.getElementById('enterAccountNumber').style.display = 'none';
        },

        thirdPart() {
            document.getElementById('enterAccountNumber').style.display = 'block';
            document.getElementById('ownAccount').style.display = 'none';
        }






    },







})