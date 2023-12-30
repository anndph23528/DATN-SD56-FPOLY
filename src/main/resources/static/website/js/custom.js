
    const quantityInputs = document.querySelectorAll(".quantity");
    const priceElements = document.querySelectorAll(".price");
    const subtotalElements = document.querySelectorAll(".subtotal");
    const totalElement = document.getElementById("total");
    const emptyCartText = document.getElementById("emptyCartText");
    const diaChiInput = document.getElementById("diaChi");
    const phiGiaoHangLabel = document.getElementById("phiGiaoHangLabel");
    const tongTienLabel = document.getElementById("tongTienLabel");
    const phieuGiamGiaSelect = document.getElementById("phieuGiamGia");
    let phiGiaoHangValue = 0;
    let phieuGiamGiaValue = 0;

    function formatToVND(amount) {
    return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
}).format(amount);
}

    function updateTotal() {
    let total = 0;
    let hasProducts = false;
    for (let i = 0; i < quantityInputs.length; i++) {
    const quantity = parseInt(quantityInputs[i].value);
    const price = parseFloat(
    priceElements[i].textContent.replace("₫", "").replace(",", "")
    );
    const subtotal = quantity * price;
    subtotalElements[i].textContent = formatToVND(subtotal);
    total += subtotal;

    if (quantity > 0) {
    hasProducts = true;
}
}

    if (!hasProducts) {
    emptyCartText.style.display = "block";
    totalElement.textContent = formatToVND(0);
} else {
    emptyCartText.style.display = "none";
    totalElement.textContent = formatToVND(total);
}

    const tongTien = total - phieuGiamGiaValue + phiGiaoHangValue;
    tongTienLabel.textContent = formatToVND(tongTien);
}

    quantityInputs.forEach((input) => {
    input.addEventListener("change", updateTotal);
});

    const removeButtons = document.querySelectorAll(".remove");
    removeButtons.forEach((button) => {
    button.addEventListener("click", function () {
        const row = this.parentElement.parentElement;
        const quantityInput = row.querySelector(".quantity");
        quantityInput.value = 0;
        row.remove();
        updateTotal();
    });
});

    const orderButton = document.querySelector("button.btn.btn-primary");
    orderButton.addEventListener("click", function () {
    const hoTen = document.getElementById("hoTen");
    const diaChi = document.getElementById("diaChi");
    const sdt = document.getElementById("sdt");
    const phuongThucThanhToan = document.querySelector('input[name="phuongThucThanhToan"]:checked');

    hoTen.style.borderColor = "";
    diaChi.style.borderColor = "";
    sdt.style.borderColor = "";

    if (hoTen.value.trim() === "") {
    hoTen.style.borderColor = "red";
    document.getElementById("error-hoTen").textContent = "Vui lòng nhập họ tên";
} else {
    document.getElementById("error-hoTen").textContent = "";
}

    if (diaChi.value.trim() === "") {
    diaChi.style.borderColor = "red";
    document.getElementById("error-diaChi").textContent = "Vui lòng nhập địa chỉ";
} else {
    document.getElementById("error-diaChi").textContent = "";
}

    if (sdt.value.trim() === "") {
    sdt.style.borderColor = "red";
    document.getElementById("error-sdt").textContent = "Vui lòng nhập số điện thoại";
} else {
    document.getElementById("error-sdt").textContent = "";
}

    if (hoTen.value.trim() !== "" && diaChi.value.trim() !== "" && sdt.value.trim() !== "") {
    if (phuongThucThanhToan.value === "khiNhanHang") {
    alert(`Xác nhận đặt hàng cho ${hoTen.value} tại địa chỉ ${diaChi.value} với số điện thoại ${sdt.value}. Phương thức thanh toán: Thanh toán khi nhận hàng.`);
} else if (phuongThucThanhToan.value === "zaloPay") {
    alert(`Xác nhận đặt hàng cho ${hoTen.value} tại địa chỉ ${diaChi.value} với số điện thoại ${sdt.value}. Phương thức thanh toán: Thanh toán bằng ZaloPay.`);
}
}
});
    phieuGiamGiaSelect.addEventListener("change", function () {
    phieuGiamGiaValue = parseFloat(phieuGiamGiaSelect.value);
    updateTotal();
});

    diaChiInput.addEventListener("input", function () {
    const diaChi = diaChiInput.value.toLowerCase();
    phiGiaoHangValue = diaChi.includes("hà nội") ? 0 : 30000;
    phiGiaoHangLabel.textContent =
    phiGiaoHangValue === 0
    ? "Miễn phí giao hàng"
    : formatToVND(phiGiaoHangValue);
    updateTotal();
});
    // document.getElementById('doi').addEventListener('change', function(event) {
    //     // Lấy đối tượng form bên trong
    //     const form = event.target;
    //
    //     // Submit form
    //     form.submit();
    // });
    // function updatePrices(input) {
    //     var form = input.form;
    //     var quantity = input.value;
    //     var priceElement = form.querySelector('.subtotal');
    //     var unitPrice = parseFloat(form.querySelector('.unit-price').innerText); // Điều chỉnh class này để tìm giá của sản phẩm
    //     priceElement.innerText = (unitPrice * quantity).toFixed(2); // Cập nhật giá theo số lượng mới
    // }
    // const button = document.getElementById("myButton");
    // const note = document.getElementById("note");
    //
    // button.addEventListener("mouseover", () => {
    //     note.style.display = "block";
    // });
    //
    // button.addEventListener("mouseout", () => {
    //     note.style.display = "none";
    // });

    window.addEventListener("load", updateTotal);
    var cartItemCount = 0;

    // Lấy tham chiếu đến nút "Thêm vào giỏ hàng"
    var addToCartButton = document.getElementById("addToCartButton");

    // Gắn sự kiện click cho nút "Thêm vào giỏ hàng"
    addToCartButton.addEventListener("click", function () {
        // Thực hiện logic thêm sản phẩm vào giỏ hàng ở đây

        // Tăng số lượng sản phẩm trong giỏ hàng lên 1 (hoặc số lượng sản phẩm được thêm vào giỏ hàng)
        cartItemCount++;

        // Cập nhật số lượng sản phẩm trên giao diện người dùng
        // Đặt giá trị vào phần tử có id là cartItemCount
        document.getElementById("cartItemCount").innerText = cartItemCount;
    });
    function updateProductPrice() {
        var productId = $('input[name="productId"]').val();
        var color = $('input[name="color"]:checked').val();
        var size = $('input[name="size"]:checked').val();

        // Kiểm tra xem cả color và size đã được chọn hay chưa
        if (color && size) {

            console.log("ID sản phẩm:" + productId);
            console.log("ID màu:" + color);
            console.log("ID size:" + size);
            $.ajax({
                type: 'GET',
                url: '/product/detail/getProductPrice', // Điều hướng đến endpoint đã tạo trong DetailProductController
                data: {
                    productId: productId,
                    color: color,
                    size: size
                },
                success: function (response) {
                    // Xử lý dữ liệu giá sản phẩm từ response và cập nhật lên trang Thymeleaf
                    if (response != null) {
                        // Giá sản phẩm không rỗng, cập nhật giá
                        $('#product-price').text(response + 'đ');
                        console.log("Giá sản phẩm: " + response);
                    } else {
                        // Giá sản phẩm rỗng, hiển thị thông báo "Hết hàng"
                        $('#product-price').nullValue('Hết hàng');
                        console.log("Sản phẩm hết hàng");
                    }
                },
                error: function () {
                    alert('Đã xảy ra lỗi khi lấy giá sản phẩm.');
                }
            });
        }
    }

    $('input[name="color"]').change(function () {
        updateProductPrice();
    });

    $('input[name="size"]').change(function () {
        updateProductPrice();
    });
    $(document).ready(function () {
        updateProductPrice();
    });

    $(document).ready(function () {
        $(".addToCartBtn").click(function () {
            var productId = $(this).data("productId");
            var color = $(this).data("color");
            var size = $(this).data("size");
            var quantity = $("#quantity").val();


            // Gửi yêu cầu Ajax để thêm sản phẩm vào giỏ hàng
            $.ajax({
                type: "get",
                url: "/cart/getadd",
                data: {
                    productId: productId,
                    quantity: quantity,
                    color: color,
                    size: size
                },
                success: function (response) {

                    // Xử lý phản hồi từ server (nếu cần)
                    // Cập nhật danh sách sản phẩm trong giỏ hàng
                    $("#cartItems").html(response);
// console.log(cartItemCount);

                    var totalItems = calculateTotalItems(response);

                    // Cập nhật số sản phẩm trên biểu tượng giỏ hàng
                    updateCartIcon(totalItems);
                },

                error: function (error) {
                    console.log(error);
                }

            });

            // cartItemCount++;
            //
            // // Cập nhật số lượng sản phẩm trên giao diện người dùng
            // // Đặt giá trị vào phần tử có id là cartItemCount
            // document.getElementById("cartItemCount").innerText = cartItemCount;


        });
    });

    function calculateTotalItems(response) {
        // Thực hiện tính tổng số sản phẩm trong giỏ hàng từ dữ liệu phản hồi
        // Điều này phụ thuộc vào cách bạn trả về dữ liệu từ máy chủ
        // Ví dụ, bạn có thể tính tổng số sản phẩm từ danh sách sản phẩm trong phản hồi.

        // Trong ví dụ này, giả định rằng dữ liệu trả về là danh sách sản phẩm
        // và chúng ta đếm số sản phẩm trong danh sách.
        var cartItems = JSON.parse(response); // Chuyển dữ liệu thành đối tượng JavaScript
        return cartItems.length;
    }

    function updateCartIcon(totalItems) {
        // Cập nhật số sản phẩm trên biểu tượng giỏ hàng
        $("#cartItemCount").text(totalItems);
    }
