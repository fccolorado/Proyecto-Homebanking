
let app = new Vue({
    el: "#app",
    data: {
        clients: [],
        newClient: {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
           
        },
    },

    created() {
        this.loadData()
    },

    methods: {
        loadData() {
            axios.get('/api/clients')

                .then(response => {
                   this.clients = response.data
                   
                })
                .catch(error => {
                    console.log(`Error en: ${error}`)
                })

        },

        addClient() {
            if (this.newClient.firstName != '' && this.newClient.lastName != '' && this.newClient.email != '') {

                swal(`¡Added client!`, ` ${this.newClient.firstName.toUpperCase()}   ${this.newClient.lastName.toUpperCase()} has been added`, 'success');
                this.postClient()
            }

        },

        postClient() {
            axios.post('/rest/clients', {
                firstName: this.newClient.firstName,
                lastName: this.newClient.lastName,
                email: this.newClient.email,

            })
                .then(response => {
                    this.loadData()

                    this.newClient.firstName = ''
                    this.newClient.lastName = ''
                    this.newClient.email = ''
                })
                .catch(error => console.log(`Error en: ${error}`))
        },

        deleteClient(link) {
            swal({
                title: "Are you sure?",
                text: "A deleted client cannot be recovered!",
                icon: "warning",
                buttons: true,
                dangerMode: true,
            })
                .then((buttons) => {
                    if (buttons) {
                        axios.delete(link)
                        swal({
                            title: "Deleted",
                            text: "The client  has been deleted!",
                            icon: "success",
                            buttons: true,
                            // dangerMode: true,
                        })
                        this.loadData()
                    } else {
                    }
                })
        },


        toUpperCase(str) {
            return str.toUpperCase()
        }

    }
})



