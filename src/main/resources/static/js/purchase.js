document.getElementById('supplier').addEventListener('change', handleSupplierChange);
document.getElementById('addItemButton').addEventListener('click', addItem);

let itemCounter = 0;
let selectedSupplierName = null;

function handleSupplierChange() {
    selectedSupplierName = document.getElementById('supplier').value;

    // Ukloni sve prethodno dodane redove kada se dobavljač promeni
    const itemsContainer = document.getElementById('items-container');
    while (itemsContainer.firstChild) {
        itemsContainer.removeChild(itemsContainer.firstChild);
    }

    // Ako želite da očistite itemCounter ili obavite druge radnje, možete to učiniti ovde
    itemCounter = 0;
}

function addItem() {
    if (!selectedSupplierName) {
        alert('Please select a supplier before adding items.');
        return;
    }

    const itemsContainer = document.getElementById('items-container');

    const itemDiv = document.createElement('div');
    itemDiv.classList.add('form-group');

    const productLabel = document.createElement('label');
    productLabel.setAttribute('for', `product${itemCounter}`);
    productLabel.textContent = "Product";

    const selectElement = document.createElement('select');
    selectElement.id = `product${itemCounter}`;
    selectElement.classList.add('custom-input');
    selectElement.name = `purchaseItems[${itemCounter}].product`;
    selectElement.required = true;

    const chooseOption = document.createElement('option');
    chooseOption.value = "";
    chooseOption.textContent = "Choose product";
    chooseOption.disabled = true;
    chooseOption.selected = true;

    selectElement.appendChild(chooseOption);

    products.forEach(product => {
        if (product.supplier.name === selectedSupplierName) {
            const option = document.createElement('option');
            option.value = product.name;
            option.textContent = product.name;
            option.dataset.supplier = product.supplier.id;
            option.dataset.price = product.price;
            selectElement.appendChild(option);
        }
    });

    const quantityLabel = document.createElement('label');
    quantityLabel.setAttribute('for', `quantity${itemCounter}`);
    quantityLabel.textContent = "Quantity";

    const quantityInput = document.createElement('input');
    quantityInput.type = 'number';
    quantityInput.id = `quantity${itemCounter}`;
    quantityInput.classList.add('custom-input');
    quantityInput.name = `purchaseItems[${itemCounter}].quantity`;
    quantityInput.required = true;

    const priceLabel = document.createElement('label');
    priceLabel.setAttribute('for', `price${itemCounter}`);
    priceLabel.textContent = "Price";

    const priceInput = document.createElement('input');
    priceInput.type = 'number';
    priceInput.id = `price${itemCounter}`;
    priceInput.classList.add('custom-input');
    priceInput.name = `purchaseItems[${itemCounter}].price`;
    priceInput.step = "0.01";
    priceInput.min = "0";
    priceInput.required = true;
    priceInput.readOnly = true;

    // Add event listeners to update total amount on input change
    priceInput.addEventListener('input', updateTotalAmount);
    quantityInput.addEventListener('input', updateTotalAmount);

    // Update price when product is selected
    selectElement.addEventListener('change', (event) => {
        const selectedOption = event.target.options[event.target.selectedIndex];
        const productPrice = parseFloat(selectedOption.dataset.price) || 0;
        priceInput.value = parseFloat((productPrice * 0.7).toFixed(2));
        updateTotalAmount();
    });

    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.classList.add('btn', 'btn-danger', 'btn-sm');
    removeButton.textContent = "Remove";
    removeButton.onclick = function () {
        removeItem(removeButton);
        updateTotalAmount(); // Update total amount after removing an item
    };

    itemDiv.appendChild(productLabel);
    itemDiv.appendChild(selectElement);
    itemDiv.appendChild(quantityLabel);
    itemDiv.appendChild(quantityInput);
    itemDiv.appendChild(priceLabel);
    itemDiv.appendChild(priceInput);
    itemDiv.appendChild(removeButton);

    itemsContainer.appendChild(itemDiv);

    itemCounter++;
    updateTotalAmount(); // Update total amount after adding an item
}

function removeItem(button) {
    const itemDiv = button.parentElement;
    itemDiv.remove();
    updateTotalAmount(); // Update total amount after removing an item
}

function updateTotalAmount() {
    let totalAmount = 0;
    const priceInputs = document.querySelectorAll('input[name$=".price"]');
    const quantityInputs = document.querySelectorAll('input[name$=".quantity"]');

    for (let i = 0; i < priceInputs.length; i++) {
        const price = parseFloat(priceInputs[i].value) || 0;
        const quantity = parseFloat(quantityInputs[i].value) || 0;
        totalAmount += price * quantity;
    }

    document.getElementById('totalAmount').value = totalAmount;
}
