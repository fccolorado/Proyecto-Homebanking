let date = new Date()
let dateNow = date.getFullYear() + '/' + String(date.getMonth() + 1).padStart(2, '0') + '/' + String(date.getDate()).padStart(2, '0');


let app = new Vue({

    el: "#app",
    data: {
        clients: {},
        accounts: 0,
        loans: {},
        cardType: "",
        cardColor: "",
        cards: [],
        creditCards: [],
        debitCards: [],
        creditCardsTrue: [],
        debitCardsTrue: [],
        accountsTrue: [],
        accountsTrueLength: [],
        dateNow2: dateNow,
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
                    this.accounts = response.data.accounts
                    this.cards = response.data.cards
                    this.loans = response.data.loans.length

                    this.creditCards = this.cards.filter(card => card.type == "CREDIT")
                    this.debitCards = this.cards.filter(card => card.type == "DEBIT")
                    this.creditCardsTrue = this.creditCards.filter(card => card.cardStatus == true)
                    this.debitCardsTrue = this.debitCards.filter(card => card.cardStatus == true)

                    this.accountsTrue = this.accounts.filter(account => account.accountStatus == true)
                    this.accountsTrueLength = this.accountsTrue.length

                    if (this.clients.email.includes("fermin.colorado@mindhub.com")) {
                        this.isAdmin = true
                    }
                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },


        dates(thruDate) {
            let monthYear = thruDate.slice(5, 7) + "/" + thruDate.slice(0, 4);
            return monthYear;
        },


        copyNumberCard(cardNumber) {
            navigator.clipboard.writeText(cardNumber);
            Swal.fire({
                title: 'Copied!',
                showConfirmButton: false,
                icon: 'success',
                timer: 1000,
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
        createAccount() {
            Swal.fire({
                title: 'Account created!',
                icon: "success",
                showConfirmButton: false,
                timer: 2000,
            })

            setTimeout(() => {
                axios.post('/api/clients/current/accounts')
                    .then(response => { window.location.href = "/web/accounts.html" })
                    .catch(error => {
                        console.log(`Error en: ${error}`)
                    })
            }, 2000);

        },
        createCard() {
            Swal.fire({
                title: 'Account created!',
                icon: "success",
                showConfirmButton: false,
                timer: 2000,
            })

            setTimeout(() => {
                axios.post("/api/clients/current/cards", "cardType=" + this.cardType + "&cardColor=" + this.cardColor, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                    .then(response => { window.location.href = "/web/cards.html" })
                    .catch(error => {
                        console.log(`Error en: ${error}`)
                    })
            }, 2000);

        },
        deleteCard(id) {
            console.log(id)
            axios.patch(`/api/clients/current/cards/delete/${id}`)
            Swal.fire({
                    title: 'Card deleted!',
                    icon: "success",
                    showConfirmButton: false,
                    timer: 2000,
                })
                .then(response => { window.location.href = "/web/cards.html" })

            .catch(error => {
                console.log(`Error en: ${error}`)
            })
        },
        deleteAccount(id) {

            axios.patch(`/api/clients/current/accounts/${id}`)

            .then(response => {
                Swal.fire({
                    title: 'Account deleted!',
                    icon: "success",
                    showConfirmButton: false,
                    timer: 2000,
                })
            })

            .then(response =>

                setTimeout(() => {
                    window.location.href = "/web/accounts.html"
                }, 2000))

            .catch(error => {
                Swal.fire({
                    title: 'Current balance is not at $0',
                    text: "do you want to transfer the balance??!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'To transactions!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = "/web/transfers.html"

                    }
                })
            })


        }
    }
})