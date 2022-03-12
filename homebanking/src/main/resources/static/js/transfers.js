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
                axios.post('/api/logout').then(response => { window.location.href = "/web/index.html" })

            }, 2000);

        },

        transfer() {
            if (this.amount == null || this.description == '' || this.sourceAccountNumber || this.destinationAccountNumber == '') {
                Swal.fire({
                    title: 'Please fill in all the spaces!',
                    icon: "info",
                    showConfirmButton: false,
                    timer: 2000,
                })
            }

            if (this.amount <= 0) {
                Swal.fire({
                    title: 'Enter an amount greater than 0!',
                    icon: "info",
                    showConfirmButton: false,
                    timer: 2000,
                })
            } else if (this.sourceAccountNumber == this.destinationAccountNumber) {
                Swal.fire({
                    title: 'Source account number is equal to destination account!',
                    icon: "info",
                    showConfirmButton: false,
                    timer: 2000,
                })

            } else {
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
                        })
                        setTimeout(() => {
                            this.createTransfer()

                        }, 2000);
                    }
                })
            }


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