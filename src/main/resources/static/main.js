function notify(message, icon) {
    Swal.fire({
        position: "top-end",
        icon: icon,
        title: message,
        showConfirmButton: false,
        timer: 2000
    });
}

function confirm(title, text, yesBtn, noBtn, icon) {
    return Swal.fire({
        title: title,
        text: text,
        icon: icon,
        showCancelButton: true,
        confirmButtonColor: '#373737',
        cancelButtonColor: '#b33f3f',
        confirmButtonText: yesBtn,
        cancelButtonText: noBtn
    })
}


document.body.addEventListener('htmx:afterRequest',
    function handleResponse(event) {
        // alert(event);
        const xhr = event.detail.xhr;   //raw XHR object
        const contentType = xhr.getResponseHeader("Content-Type");
        let textField = document.querySelectorAll("#product-submission-form > input[type=text]")
        // alert(textField.length)
        if (contentType && contentType.includes("text/plain")) {
            const data = xhr.responseText;
            if (data === 'Product saved') {
                notify(data, "success")
                for (let field of textField) {
                    field.value = ''
                }
            } else {
                notify(data, "error")
            }
        }
    }
);

(
    function () {
        let urlQueryParams = window.location.search.replace("?", "")
        let params = [];
        if (urlQueryParams != '') {
            let urlParamsSplit = urlQueryParams.split("&");
            urlParamsSplit.forEach(p => params.push(p.split("=")));
        }
        let msg = params[1] == undefined || params[1][1] == "" ? "error" : params[1][1];

        if (params != undefined && params.length > 0) {
            if (params[0][0] === "error") {
                notify(decodeURIComponent(msg), "error");
            } else if (params[0][0] === "success") {
                notify(decodeURIComponent(msg), "success");
            }
            // autoLoadProducts();
            window.setTimeout(() => {
                window.location.search = "";
            }, 3000)
        }
    }
)();

function autoLoadProducts() {
    window.setTimeout(() => {
        document.getElementById("load-products-btn").click();
    }, 1000)
}

function deleteProduct(productId) {
    confirm("Are you sure?", "You won't be able to revert this!", "Yes, delete it!", "No, cancel!", "warning")
        .then((result) => {
            if (result.isConfirmed) {
                // htmx.ajax('GET', '/product')
                let response = "";
                fetch(`/products/delete/${productId}`)
                    .then(res => res.text())
                    .then(data => {
                        if (data == "success") {
                            document.getElementById("product-" + productId).style.backgroundColor = "#c16e6e"
                            htmx.remove(htmx.find("#product-" + productId), 1500);
                        }
                        notify(data == "success" ? "Product has been deleted!"
                            : "Unable to delete product", data);
                    })
            }
        })
}