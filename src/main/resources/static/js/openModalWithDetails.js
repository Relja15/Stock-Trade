function openModalWithDetails(purchaseId) {
    fetch(`/api/purchase/${purchaseId}`)
        .then(response => response.json())
        .then(data => {
            // Popuni dodatne informacije
            document.getElementById("purchase-date").innerText = data.date;
            document.getElementById("supplier-name").innerText = data.supplier;
            document.getElementById("total-amount").innerText = data.totalAmount;

            // Popuni tabelu sa stavkama
            const tableBody = document.getElementById("purchase-details-body");
            tableBody.innerHTML = "";
            data.purchaseItems.forEach(item => {
                const row = `<tr>
                                <td>${item.product}</td>
                                <td>${item.quantity}</td>
                                <td>${item.price}</td>
                             </tr>`;
                tableBody.innerHTML += row;
            });

            // Prikaži modal
            var myModal = new bootstrap.Modal(document.getElementById('detailsModal'));
            myModal.show();
        })
        .catch(error => {
            console.error('Error fetching purchase details:', error);
        });
}
