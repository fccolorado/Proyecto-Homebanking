let app = new Vue({

    el: "#app",
    data: {
        user: {},
        strengths: []

    },

    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("https://bio.torre.co/api/bios/ferminciro")
                .then(response => {
                    this.user = response.data.person
                    this.strengths = response.data.strengths

                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })
        }
    }

})