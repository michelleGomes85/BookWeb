function updateTotalPrice(spinner, price, totalInputClientId) {
    let quantity = spinner.value || 1; // Se estiver vazio, assume 1
    let total = quantity * price;

    // Atualiza o campo de preço total
    let totalInput = document.getElementById(totalInputClientId);
    if (totalInput) {
        totalInput.value = total.toFixed(2).replace('.', ',');
    }
}
