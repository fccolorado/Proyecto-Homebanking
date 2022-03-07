let app = new Vue({

    el: "#app",
    data: {
        clients: {},
        cards: 0,
        creditCards: [],
        debitCards: [],
        cardType: [],
        cardColor: [],
        creditCardsTrue: [],
        debitCardsTrue: [],
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
                    this.cards = response.data.cards
                    this.creditCards = this.cards.filter(card => card.type == "CREDIT")
                    this.debitCards = this.cards.filter(card => card.type == "DEBIT")
                    this.creditCardsTrue = this.creditCards.filter(card => card.cardStatus == "true")
                    this.debitCardsTrue = this.debitCards.filter(card => card.cardStatus == "true")

                    if (this.clients.email.includes("fermin.colorado@mindhub.com")) {
                        this.isAdmin = true
                    }


                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        },

        createCard() {
            Swal.fire({
                title: 'Card created!',
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