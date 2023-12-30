/**
 *
 * You can write your JS code here, DO NOT touch the default style file
 * because it will make it harder for you to update.
 *
 */
// Hiện preview ảnh khi thêm sản phẩm
function handleFileSelect(evt) {
    // Loop through the FileList and render image files as thumbnails.
    for (const file of evt.target.files) {

        // Render thumbnail.
        const span = document.createElement('span')
        const src = URL.createObjectURL(file)
        span.innerHTML =
            `<img style="height: 75px; border: 1px solid #000; margin: 5px"` +
            `src="${src}" title="${escape(file.name)}">`

        document.getElementById('list').insertBefore(span, null)
    }
}

// Hiện form thêm nhanh loại giày
function popupLoaiGiay() {
    var popup = document.getElementById("formLoaiGiay");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

// Hiện form thêm nhanh thương hiệu
function popupThuongHieu() {
    var popup = document.getElementById("formThuongHieu");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function popupChatLieu() {
    var popup = document.getElementById("formChatLieu");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function popupDeGiay() {
    var popup = document.getElementById("formDeGiay");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function popupacc() {
    var popup = document.getElementById("formacc");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}
function popupChucVu() {
    var popup = document.getElementById("formChucvu");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function popupSize() {
    var popup = document.getElementById("formSize");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function popupMauSac() {
    var popup = document.getElementById("formMauSac");
    if (popup.style.display === "block") {
        popup.style.display = "none";
    } else {
        popup.style.display = "block";
    }
}

function deleteData(type, id, successMessage) {
    if (confirm("Bạn có muốn xoá không ?")) {
        $.ajax({
            type: "GET",
            url: "/admin/" + type + "/delete/" + id,
            success: function (data) {
                alert(successMessage);
            }
        });
    } else {
        return false;
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const sizeSelect = document.getElementById("size");
    const colorSelect = document.getElementById("color");
    const productSelect = document.getElementById("product");
    const quantityProduct = document.getElementById("quantity");
    const addButton = document.getElementById("add-product");
    const tableBody = document.querySelector("#product-table tbody");

    addButton.addEventListener("click", function () {
        const size = sizeSelect.value;
        const color = colorSelect.value;
        const product = productSelect.value;
        const quantity = quantityProduct.value;

        const newRow = tableBody.insertRow(tableBody.rows.length);
        const cell1 = newRow.insertCell(0);
        const cell2 = newRow.insertCell(1);
        const cell3 = newRow.insertCell(2);
        const cell4 = newRow.insertCell(3);

        cell4.innerHTML = quantity;
        cell3.innerHTML = size;
        cell2.innerHTML = color;
        cell1.innerHTML = product;
    });
});




"use strict";

