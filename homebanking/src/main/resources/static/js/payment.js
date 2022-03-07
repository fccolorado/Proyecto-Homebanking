let app = new Vue({

    el: "#app",
    data: {
        number: "",
        cvv: "",
        amount: "",
        description: "",

    },

    methods: {
        payment() {

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
                    axios.post('/api/payment', { "number": this.number, "cvv": this.cvv, "amount": this.amount, "description": this.description }, { headers: { 'content-type': 'application/json' } })

                    .then(response => {
                        Swal.fire({
                            title: 'Successful payment!',
                            icon: "success",
                            showConfirmButton: false,
                            timer: 2000,
                        })
                    })

                    .then(response => {
                            setTimeout(() => {
                                window.location.href = "/web/accounts.html"
                            }, 2000);
                        })
                        .catch(error => {
                            console.log(`Error en: ${error}`)
                        })
                }
            })







        },



    },







})