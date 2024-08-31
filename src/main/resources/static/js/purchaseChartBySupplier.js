fetch('/api/charts/purchase-supplier-data')
    .then(response => response.json())
    .then(data => {
        // Sortiranje podataka po vrednostima (ukupna količina po dobavljaču) u opadajućem redosledu
        const sortedData = Object.entries(data).sort(([, valueA], [, valueB]) => valueB - valueA);

        // Izdvajanje sortirane liste dobavljača (labels) i vrednosti (values)
        const labels = sortedData.map(entry => entry[0]); // Dobavljači
        const values = sortedData.map(entry => entry[1]); // Ukupne količine po dobavljaču

        const ctx = document.getElementById('supplier-chart').getContext('2d');
        new Chart(ctx, {
            type: 'bar', // Možete koristiti 'bar', 'pie', 'line', itd.
            data: {
                labels: labels,
                datasets: [{
                    label: 'Number of purchases per supplier',
                    data: values,
                    backgroundColor: 'rgba(153, 102, 255, 0.2)',
                    borderColor: 'rgba(153, 102, 255, 1)',
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
