function addVariant() {
    const container = document.getElementById('variants-container');
    const variantCount = container.getElementsByClassName('variant-fieldset').length;
    const newFieldset = document.createElement('fieldset');
    newFieldset.className = 'variant-fieldset';
    newFieldset.innerHTML = `
        <legend>Variant ${variantCount + 1}:</legend>
        <wa-input name="variants[${variantCount}].title" label="Title" required></wa-input>
        <br/>
        <wa-input name="variants[${variantCount}].price" label="Price" type="number" step="0.01" required></wa-input>
        <br/>
        <wa-input name="variants[${variantCount}].featuredImage" label="Featured Image URL"></wa-input>
        <br/>
        <button type="button" onclick="removeVariant(this)" class="btn btn-danger btn-sm">Remove Variant</button>
    `;
    container.appendChild(newFieldset);
}

function removeVariant(button) {
    button.closest('.variant-fieldset').remove();
}

