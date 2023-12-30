document.addEventListener('DOMContentLoaded', function () {
    var colors = document.querySelectorAll('.color-label');
    var sizes = document.querySelectorAll('.size-label');

    var colorsSelected = false;
    var sizesSelected = false;

    colors.forEach(function (color) {
        color.addEventListener('click', function () {
            if (this.classList.contains('selected-color')) {
                this.classList.remove('selected-color');
                colorsSelected = false;
            } else {
                colors.forEach(function (color) {
                    color.classList.remove('selected-color');
                });
                this.classList.add('selected-color');
                colorsSelected = true;
            }
            showImage();
            checkAndSendData(); // Kiểm tra và gửi dữ liệu
        });
    });

    sizes.forEach(function (size) {
        size.addEventListener('click', function () {
            if (this.classList.contains('selected-size')) {
                this.classList.remove('selected-size');
                sizesSelected = false;
            } else {
                sizes.forEach(function (size) {
                    size.classList.remove('selected-size');
                });
                this.classList.add('selected-size');
                sizesSelected = true;
            }
            showImage();
            checkAndSendData(); // Kiểm tra và gửi dữ liệu
        });
    });

    function checkAndSendData() {
        if (colorsSelected && sizesSelected) {
            sendSelectedData(); // Gọi khi cả hai đều được chọn
        }
    }

    var xhr = new XMLHttpRequest();

    function showImage() {
        var selectedColor = document.querySelector('.selected-color');
        var selectedSize = document.querySelector('.selected-size');
        var productId = document.getElementById('product-id').textContent;
        var colorId = selectedColor ? selectedColor.getAttribute('for').replace('mauSac', '') : null;
        var sizeId = selectedSize ? selectedSize.getAttribute('for').replace('kichThuoc', '') : null;

        xhr.open('POST', '/product/detail/display', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 400) {
                document.getElementById('product-image').src = '/dashboard/images/' + JSON.parse(xhr.responseText).image + '.jpg';
            }
        };
        xhr.send('colorId=' + colorId + '&sizeId=' + sizeId + '&productId=' + productId);
    }

    function sendSelectedData() {
        var cartButton = document.getElementById('cart-button');
        var dataId = null;
        var selectedColor = document.querySelector('.selected-color');
        var selectedSize = document.querySelector('.selected-size');
        var productId = document.getElementById('product-id').textContent;
        var colorId = selectedColor ? selectedColor.getAttribute('for').replace('ms', '') : null;
        var sizeId = selectedSize ? selectedSize.getAttribute('for').replace('kt', '') : null;

        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/product/detail/chi-tiet', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 400) {
                var data = JSON.parse(xhr.responseText);

                cartButton.removeAttribute("disabled");
                dataId = data.id !== null ? data.id : null;
// // Cập nhật thuộc tính ng-click
                cartButton.setAttribute("ng-click", "cart.addToCart('" + dataId + "')");
//
// Biên dịch lại nút "Cart"
                var $injector = angular.injector(['ng']);
                $injector.invoke(['$compile', function ($compile) {
                    var $scope = angular.element(cartButton).scope();
                    $compile(cartButton)($scope);
                }]);
                updateProductDetails(data);

            } else if (xhr.status === 404 || JSON.parse(xhr.responseText).soLuong < 1) {
                showImage();
                cartButton.setAttribute("disabled", "true");
                cartButton.removeAttribute("ng-click");
                document.getElementById('product-quantity').innerText = 'Số lượng: Hết hàng';
            }

        };
        xhr.send('colorId=' + colorId + '&sizeId=' + sizeId + '&productId=' + productId);

    }

    function updateProductDetails(data) {
        var sl = data;
        document.getElementById('product-price').innerText = sl.sellPrice;
        document.getElementById('product-quantity').innerText = 'Số lượng: ' + sl.quantity;
    }
});


const app =angular.module('shopping-cart-app', []);

app.controller('shopping-cart-ctrl', function ($scope, $http) {
    $scope.dataId = null;
    $scope.totalQuantity = 0;
    $scope.kmMa = "";
    $scope.totalPrice = 0;
    // $scope.tongTienTT = 0;
    $scope.total = 0;
    $scope.KM = null;

    // Khởi tạo biến để điều khiển hiển thị link Admin
    $scope.showAdminLink = false;

    // Hàm để kiểm tra vai trò và hiển thị link nếu là ADMIN hoặc STAFF
    function checkUserRole(roles) {
        if (roles.includes('ADMIN') || roles.includes('ROLE')) {
            $scope.showAdminLink = true;
        }
    }

    // Gọi API để lấy vai trò của người dùng
    function getAuthor() {
        $http.get('/customer/all')
            .then(function(response) {
                var userRoles = response.data;
                checkUserRole(userRoles);
            })
            .catch(function(error) {
                console.error('Error fetching user roles:', error);
            });
    }
    getAuthor();
    function reloadCartItems() {
        $http.get('/rest/chitietgiohang')
            .then(function (response) {
                $scope.cartItems = response.data;
                calculateTotals();
            });
    }

    function calculateTotals() {
        $scope.totalQuantity = 0;
        $scope.total = 0;

        for (let i = 0; i < $scope.cartItems.length; i++) {
            let sl = $scope.cartItems[i];
            $scope.totalQuantity++; // Tăng totalQuantity sau mỗi vòng lặp
            $scope.total += (sl.producDetail.sellPrice * sl.quantity);
        }

        $scope.totalPrice = $scope.total - ($scope.KM !== null ? $scope.KM.giaTri : 0);
    }
    //
    //
    // $scope.cart = {
    //     addToCart(productId) {
    //         $http.post('/rest/cart/add', {productId: productId})
    //             .then(function (response) {
    //                 console.log(productId)
    //                 if (response.data) {
    //                     reloadCartItems(); // Tải lại danh sách sau khi thêm thành công
    //                 } else {
    //                     alert("Thêm vào giỏ hàng không thành công!");
    //                 }
    //
    //             })
    //             .catch(function (error) {
    //                 alert("Vui lòng đăng nhập");
    //                 console.error(error);
    //             });
    //
    //     },
    //
    //     remove(productId) {
    //         $http.post('/rest/cart/remove', {productId: productId})
    //             .then(function (response) {
    //                 alert("Xóa sản phẩm khỏi giỏ hàng thành công!");
    //                 reloadCartItems(); // Tải lại danh sách sau khi xóa thành công
    //             })
    //             .catch(function (error) {
    //                 alert("Có lỗi xảy ra khi gọi API!");
    //                 console.error(error);
    //             });
    //     },
    //
    //     update(productId, quantity) {
    //         $http.post('/rest/cart/update', {productId: productId, quantity: quantity})
    //             .then(function (response) {
    //                 if (response.data) {
    //                     alert("Cập nhật giỏ hàng thành công!");
    //                     reloadCartItems(); // Tải lại danh sách sau khi cập nhật thành công
    //                 } else {
    //                     alert("Cập nhật giỏ hàng không thành công!");
    //                 }
    //             })
    //             .catch(function (error) {
    //                 alert("Có lỗi xảy ra khi gọi API!");
    //                 console.error(error);
    //             });
    //     },
    //     clear() {
    //         $http.post('/rest/cart/clear')
    //             .then(function (response) {
    //                 alert("Xóa tất cả sản phẩm khỏi giỏ hàng thành công!");
    //                 reloadCartItems(); // Tải lại danh sách sau khi xóa thành công
    //             })
    //             .catch(function (error) {
    //                 alert("Có lỗi xảy ra khi gọi API!");
    //                 console.error(error);
    //             });
    //     },
    //
    // }
    // reloadCartItems();

    $scope.orders = {
        createDate: new Date(),
        updateDate: new Date(),
        shippingFee : 0,
        // tongTienTT: 0,  // Khởi tạo giá trị ban đầu
        // tongTienKhuyenMai: 0,  // Khởi tạo giá trị ban đầu
        total: 0,  // Khởi tạo giá trị ban đầu
        fullName:"",
        phone: "",
        address: "",
        orderStatus: 2,
        // get orderItem() {
        //     return $scope.cartItems.map(sl => {
        //         return {
        //             sellPrice: sl.producDetail.sellPrice,
        //             quantity: sl.quantity,
        //             producDetail: {id: sl.producDetail.id}
        //         }
        //     });
        // },
        purchase() {
            var order = angular.copy(this);

            // Cập nhật giá trị
            // order.tongTienTT = $scope.totalPrice;
            order.shippingFee=$scope.shippingFee;
            order.total = $scope.total;

            console.log(order);

            $http.post('http://localhost:8080/rest/order/add', order).then(resp => {

                alert("Dat hang thanh cong");
                console.log(resp.data);
                location.href = "/product/hien-thi";

            }).catch(error => {
                alert("Dat hang loi");
                console.log(error);
            });
        }
    }


});