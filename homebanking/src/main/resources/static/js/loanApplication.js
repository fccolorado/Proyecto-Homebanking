let app = new Vue({

    el: "#app",
    data: {
        accounts: {},
        accountsTrue: [],
        loans: [],

        //Loan aplication client
        name: "",
        amount: 0,
        payments: 0,
        destinationAccountNumber: "",
        loanInteres: 0,

        //Create loan admin
        newNameLoan: "",
        newMaxAmountLoan: "",
        newPaymentsLoan: [],
        newPercentageLoan: "",
        isAdmin: false,
        loansPayments1: [0],
        loansPayments: [],
        loansMaxAmount: "",
        loansPercentage: ""

    },

    created() {
        this.loadLoans()
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

        loadLoans() {
            axios.get('/api/loans')
                .then(response => {
                    this.loans = response.data

                    this.loansPayments1 = this.loans.filter(loan => loan.name == this.name)
                    this.loansPayments = this.loansPayments1[0].payments
                    this.loansMaxAmount = this.loansPayments1[0].maxAmount
                    this.loansPercentage = this.loansPayments1[0].percentage

                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },

        filterLoans() {


        },

        createLoansAdmin() {
            axios.post('/api/create/loans', { "name": this.newNameLoan, "maxAmount": this.newMaxAmountLoan, "payments": this.newPaymentsLoan, "percentage": this.newPercentageLoan })
                .then((response) => {
                    Swal.fire({
                            title: `Loan created`,
                            icon: "success",
                            showConfirmButton: false,
                        }),

                        setTimeout(() => {
                            window.location.href = "/web/accounts.html"

                        }, 3000)
                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },
        createLoan() {
            if (this.amount <= 0) {
                Swal.fire({
                    title: 'Monto debe ser mayor a 0!',
                    showConfirmButton: false,
                    icon: 'info',
                    timer: 1000,
                })

            } else {
                Swal.fire({
                    title: 'Accept loan?',
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    cancelButtonText: 'Reject',
                    confirmButtonText: 'Accept'
                }).then((result) => {
                    if (result.isConfirmed) {
                        axios.post('/api/loans', { "name": this.name, "amount": this.amount, "payments": this.payments, "destinationAccountNumber": this.destinationAccountNumber })
                            .then(() => {
                                Swal.fire({
                                        title: `Amount deposited in your account: ${this.destinationAccountNumber}`,
                                        icon: "success",
                                        showConfirmButton: false,
                                    }),

                                    setTimeout(() => {
                                        window.location.href = "/web/accounts.html"

                                    }, 3000)
                            })

                        .catch(() => console.log('error'))
                    }
                })

            }

        },

        loanInterest(percentage) {
            this.loanInteres = this.amount / this.payments
            this.loanInteres = ((this.loanInteres * percentage) + this.loanInteres).toFixed(2)
            return this.loanInteres
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

        }
    },

})