fetch('/api/charts/purchase-category-data')
    .then(response => response.json())
    .then(data => {
        // Pretvaranje objekta u niz parova [kategorija, broj nabavki]
        const sortedData = Object.entries(data).sort(([, valueA], [, valueB]) => valueB - valueA);

        // Izdvajanje sortirane liste kategorija (labels) i vrednosti (values)
        const labels = sortedData.map(entry => entry[0]); // Kategorije
        const values = sortedData.map(entry => entry[1]); // Broj nabavki po kategorijama

        const ctx = document.getElementById('category-chart').getContext('2d');
        new Chart(ctx, {
            type: 'bar', // Možete koristiti 'bar', 'pie', 'line', itd.
            data: {
                labels: labels,
                datasets: [{
                    label: 'Number of purchases per category',
                    data: values,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    });
