function styleLoadedProductTable(){
    tableInit = window.setTimeout(() => {
        if (DataTable == undefined) {
            tableInit;
        }
        let table = new DataTable('#product-list-table', {
            order: [[1, 'desc']]
        });
    }, 500)
    tableInit;
}