let app = angular.module("myApp", []);
app.controller("ctrl", function ($scope, $http, $location) {
    //trang thanh toán
    $scope.checkOut = function () {
        $scope.giamGia = 0;
        $scope.phiShip = 0;
        let idGioHang = document.getElementById("idGioHang").value;
        let idKhachHang = document.getElementById("idKhachHang").value;
        let urlcolor = "http://localhost:8080/api/mausac";
        let urlsize = "http://localhost:8080/api/kichthuoc";
        // load color
        $scope.listColor = [];
        $http.get(urlcolor).then(function (response) {
            $scope.listColor = response.data;
        });
        // load size
        $scope.listSize = [];
        $http.get(urlsize).then(function (response) {
            $scope.listSize = response.data;
        });
        let checkTT = null;
        if(location.search.length > 0){
            checkTT = location.search.split("&")[9].split("=")[1];
        }

        // console.log(checkTT[9].split('=')[1]);
        //check trạng thái thanh toán online khi trả về
        if (checkTT === "00") {

            // $http.put(
            //     "http://localhost:8080/api/hoa-don/update/" + $location.search().vnp_OrderInfo
            // );

            // let amount = parseInt($location.search().vnp_Amount) / 100;
            Swal.fire(
                "Thanh toán thành công !",
                "Bạn đã thanh toán thành công cho đơn hàng " +
                location.search.split("&")[3].split("=")[1],
                "success"
            );
            setTimeout(() => {
                location.href = "/client/home";
            }, 2500);


        }
        if (checkTT === "02") {
            Swal.fire(
                "Đơn hàng " +
                location.search.split("&")[3].split("=")[1] +
                " chưa được thanh toán",
                "Vui lòng thanh toán trong vòng 24h ",
                "warning"
            );
            setTimeout(() => {
                location.href = "/client/home";
            }, 2500);
        }
        $scope.chondiachi = function (){
            if(document.getElementById("dccs").checked === true){
                document.getElementById("diachicosan").style.display = 'block';
                document.getElementById("diachichon").style.display = 'none';
            }
            else{
                document.getElementById("diachicosan").style.display = 'none';
                document.getElementById("diachichon").style.display = 'block';
            }
        }
        // dat hang

        $scope.buy = function () {
            let id= document.getElementById("address").value;
            if(document.getElementById("address").checked === true){
                // Get the select element by its id
                const selectElement = document.getElementById('tinh');
                // Get the selected option's text content (ProvinceName)
                const cityName = selectElement.options[selectElement.selectedIndex].textContent;
                const selectElement1 = document.getElementById('huyen');
                // Get the selected option's text content (ProvinceName)
                const districtName = selectElement1.options[selectElement1.selectedIndex].textContent;
                const selectElement2 = document.getElementById('xa');
                // Get the selected option's text content (ProvinceName)
                const wardName = selectElement2.options[selectElement2.selectedIndex].textContent;
                let tenNguoiNhan = document.getElementById("tenguoinhan").value;
                let sdtNhan = document.getElementById("sdtnhan").value;
                let diaChiCuThe = document.getElementById("diachicuthe").value;
                $http.post("http://localhost:8080/api/diachi",{
                    ghiChu : diaChiCuThe,
                    tinhThanh : cityName,
                    quanHuyen : districtName,
                    phuongXa : wardName,
                    tenNguoiNhan : tenNguoiNhan,
                    sdtNhan : sdtNhan,
                    idKhachHang : null
                }).then(function (resp){
                    id = resp.data.id
                })

            }
            Swal.fire({
                title: "Xác nhận đặt hàng ?",
                showCancelButton: true,
                confirmButtonText: "Đặt",
            }).then((result) => {
                /* Read more about isConfirmed, isDenied below */
                if (result.isConfirmed) {
                    // var typePay = document.getElementById("typePay").value;
                    //nếu chọn thanh toán tại nhà
                    if (document.getElementById("pay1").checked === true) {
                        //check số lượng còn hàng trước khi cho đặt hàng
                        $http
                            .get("http://localhost:8080/api/cart/"+idKhachHang)
                            .then(function (CheckCart) {
                                for (let i = 0; i < CheckCart.data.length; i++) {
                                    //get số lượng sản phẩm đang có
                                    var getPram = {
                                        IdProduct: CheckCart.data[i].chiTietSanPham.idCTSP,
                                        IdColor: CheckCart.data[i].mauSac,
                                        IdSize: CheckCart.data[i].kichThuoc,
                                    };

                                    $http({
                                        method: "GET",
                                        url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/getQuantityProductAndColorAndSize",
                                        params: getPram,
                                    }).then(function (resp) {
                                        if (CheckCart.data[i].soLuong > resp.data) {
                                            Swal.fire(
                                                "Số lượng sản phẩm " +
                                                CheckCart.data[i].chiTietSanPham.sanPham
                                                    .tenSanPham +
                                                " không đủ ! Số lượng sản phẩm này trong giỏ hàng của bạn sẽ được cập nhật về số lượng hiện có !",
                                                "",
                                                "error"
                                            );
                                            // cập lại số lượng hiện có vào giỏ hàng
                                            $http
                                                .put(
                                                    "http://localhost:8080/api/cart/updateCart/" +
                                                    CheckCart.data[i].id,
                                                    {
                                                        idCart: idGioHang,
                                                        idProductDetail:
                                                        CheckCart.data[i].chiTietSanPham.idCTSP,
                                                        idColor: CheckCart.data[i].mauSac,
                                                        idSize: CheckCart.data[i].kichThuoc,
                                                        soLuong: resp.data,
                                                        donGia: CheckCart.data[i].donGia,
                                                    }
                                                )
                                                .then(function (cart) {
                                                    if (cart.status === 200) {
                                                        //load lại sau khi tăng thành công !
                                                        $scope.loadCart();
                                                    }
                                                });
                                            return;
                                        }
                                        // nếu số lượng hết thì xóa khỏi giỏ hàng
                                        if (resp.data === 0) {
                                            Swal.fire(
                                                "Số lượng sản phẩm " +
                                                CheckCart.data[i].chiTietSanPham.sanPham
                                                    .tenSanPham +
                                                " đã hết ! Sản phẩm sẽ được xóa khỏi giỏ hàng",
                                                "",
                                                "success"
                                            );
                                            $http.delete(
                                                "http://localhost:8080/api/cart/" +
                                                CheckCart.data[i].id
                                            );
                                            $scope.loadCart();
                                            return;
                                        }


                                    });
                                }
                                // add bill

                                $http
                                    .post("http://localhost:8080/api/hoa-don", {
                                        tongTien: $scope.tongTien,
                                        phiShip: 0,
                                        tongTienKhiGiam:
                                            0,
                                        ghiChu: document.getElementById("note").value,
                                        // payType: 0,
                                        // payStatus: 0,
                                        // idCoupon: 0,
                                        // idAddress: idAddress,
                                        idKhachHang: idKhachHang,
                                        trangThai: 0,
                                        idDiaChi : idDiaChi
                                    })
                                    .then(function (bill) {
                                        $http
                                            .get("http://localhost:8080/api/cart/"+idKhachHang)
                                            .then(function (CartToBill) {
                                                for (
                                                    let i = 0;
                                                    i < CartToBill.data.length;
                                                    i++
                                                ) {
                                                    $http
                                                        .post(
                                                            "http://localhost:8080/api/hdct",
                                                            {
                                                                // add bill detail
                                                                idBill: bill.data.idHoaDon,
                                                                idProductDetail:
                                                                CartToBill.data[i].chiTietSanPham
                                                                    .idCTSP,
                                                                idColor:
                                                                CartToBill.data[i].mauSac,
                                                                idSize: CartToBill.data[i].kichThuoc,
                                                                soLuong:
                                                                CartToBill.data[i].soLuong,
                                                                donGia:
                                                                CartToBill.data[i].donGia,
                                                            }
                                                        )
                                                        .then(function (billdetail) {
                                                            //xóa giỏ hàng by user
                                                            $http.delete(
                                                                "http://localhost:8080/api/cart/deleteAllCart/" + idKhachHang
                                                            );

                                                            //get số lượng sản phẩm đang có
                                                            var getPram = {
                                                                IdProduct:
                                                                CartToBill.data[i].chiTietSanPham
                                                                    .idCTSP,
                                                                IdColor:
                                                                CartToBill.data[i].mauSac,
                                                                IdSize: CartToBill.data[i].kichThuoc,
                                                            };
                                                            $http({
                                                                method: "GET",
                                                                url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/getQuantityProductAndColorAndSize",
                                                                params: getPram,
                                                            }).then(function (resp) {
                                                                var param2 = {
                                                                    IdProduct:
                                                                    CartToBill.data[i]
                                                                        .chiTietSanPham.idCTSP,
                                                                    IdColor:
                                                                    CartToBill.data[i].mauSac,
                                                                    IdSize:
                                                                    CartToBill.data[i].kichThuoc,
                                                                    Quantity:
                                                                        parseInt(resp.data) -
                                                                        parseInt(
                                                                            CartToBill.data[i].soLuong
                                                                        ),
                                                                };
                                                                $http({
                                                                    method: "PUT",
                                                                    url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/updateQuantity",
                                                                    params: param2,
                                                                }).then(function (resp) {
                                                                    Swal.fire(
                                                                        "Đặt hàng thành công !",
                                                                        "",
                                                                        "success"
                                                                    );

                                                                    setTimeout(() => {
                                                                        location.href = "/client/home";
                                                                    }, 2500);
                                                                    // $http.post('http://localhost:8080/api/billhistory',{
                                                                    //     createBy : null,
                                                                    //     note : null,
                                                                    //     status : 0,
                                                                    //     idBill : bill.data.id
                                                                    // }).then(function(resp){
                                                                    //
                                                                    // })

                                                                });
                                                            });
                                                        });
                                                }
                                            });
                                    });
                            });
                    } else if (document.getElementById("pay2").checked === true) {
                        //thanh toán qua vnpay
                        //check số lượng còn hàng trước khi cho đặt hàng
                        $http
                            .get("http://localhost:8080/api/cart/" + idKhachHang)
                            .then(function (CheckCart) {
                                for (let i = 0; i < CheckCart.data.length; i++) {
                                    //get số lượng sản phẩm đang có
                                    var getPram = {
                                        IdProduct: CheckCart.data[i].chiTietSanPham.idCTSP,
                                        IdColor: CheckCart.data[i].mauSac,
                                        IdSize: CheckCart.data[i].kichThuoc,
                                    };

                                    $http({
                                        method: "GET",
                                        url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/getQuantityProductAndColorAndSize",
                                        params: getPram,
                                    }).then(function (resp) {
                                        if (CheckCart.data[i].soLuong > resp.data) {
                                            Swal.fire(
                                                "Số lượng sản phẩm " +
                                                CheckCart.data[i].chiTietSanPham.sanPham
                                                    .tenSanPham +
                                                " không đủ ! Số lượng sản phẩm này trong giỏ hàng của bạn sẽ được cập nhật về số lượng hiện có !",
                                                "",
                                                "error"
                                            );
                                            // cập lại số lượng hiện có vào giỏ hàng
                                            $http
                                                .put(
                                                    "http://localhost:8080/api/cart/updateCart/" +
                                                    CheckCart.data[i].id,
                                                    {
                                                        idCart: idGioHang,
                                                        idProductDetail:
                                                        CheckCart.data[i].chiTietSanPham.idCTSP,
                                                        idColor: CheckCart.data[i].mauSac,
                                                        idSize: CheckCart.data[i].kichThuoc,
                                                        soLuong: resp.data,
                                                        donGia: CheckCart.data[i].donGia,
                                                    }
                                                )
                                                .then(function (cart) {
                                                    if (cart.status === 200) {
                                                        //load lại sau khi tăng thành công !
                                                        $scope.loadCart();
                                                    }
                                                });
                                            return;
                                        }
                                        // nếu số lượng hết thì xóa khỏi giỏ hàng
                                        if (resp.data === 0) {
                                            Swal.fire(
                                                "Số lượng sản phẩm " +
                                                CheckCart.data[i].chiTietSanPham.sanPham
                                                    .tenSanPham +
                                                " đã hết ! Sản phẩm sẽ được xóa khỏi giỏ hàng",
                                                "",
                                                "success"
                                            );
                                            $http.delete(
                                                "http://localhost:8080/api/cart/" +
                                                CheckCart.data[i].id
                                            );
                                            $scope.loadCart();
                                            return;
                                        }

                                    });
                                }
                                if(document.getElementById("dcc").checked === true){
                                    // Get the select element by its id
                                    const selectElement = document.getElementById('tinh');
                                    // Get the selected option's text content (ProvinceName)
                                    const cityName = selectElement.options[selectElement.selectedIndex].textContent;
                                    const selectElement1 = document.getElementById('huyen');
                                    // Get the selected option's text content (ProvinceName)
                                    const districtName = selectElement1.options[selectElement1.selectedIndex].textContent;
                                    const selectElement2 = document.getElementById('xa');
                                    // Get the selected option's text content (ProvinceName)
                                    const wardName = selectElement2.options[selectElement2.selectedIndex].textContent;
                                    let tenNguoiNhan = document.getElementById("tenguoinhan").value;
                                    let sdtNhan = document.getElementById("sdtnhan").value;
                                    let diaChiCuThe = document.getElementById("diachicuthe").value;
                                    $http.post("http://localhost:8080/api/diachi",{
                                        ghiChu : diaChiCuThe,
                                        tinhThanh : cityName,
                                        quanHuyen : districtName,
                                        phuongXa : wardName,
                                        tenNguoiNhan : tenNguoiNhan,
                                        sdtNhan : sdtNhan,
                                        idKhachHang : null
                                    }).then(function (resp){
                                        idDiaChi = resp.data.idDiaChi
                                    })

                                }
                                // add bill
                                $http.post("http://localhost:8080/api/hoa-don", {
                                    tongTien: $scope.tongTien,
                                    phiShip: 0,
                                    tongTienKhiGiam:
                                        0,
                                    ghiChu: document.getElementById("note").value,
                                    idKhachHang: idKhachHang,
                                    status: 0,
                                    idDiaChi : idDiaChi
                                })
                                    .then(function (bill) {
                                        $http
                                            .get("http://localhost:8080/api/cart/"+idKhachHang)
                                            .then(function (CartToBill) {
                                                for (
                                                    let i = 0;
                                                    i < CartToBill.data.length;
                                                    i++
                                                ) {
                                                    $http
                                                        .post(
                                                            "http://localhost:8080/api/hdct",
                                                            {
                                                                // add bill detail
                                                                idBill: bill.data.idHoaDon,
                                                                idProductDetail:
                                                                CartToBill.data[i].chiTietSanPham
                                                                    .idCTSP,
                                                                idColor:
                                                                CartToBill.data[i].mauSac,
                                                                idSize: CartToBill.data[i].kichThuoc,
                                                                soLuong:
                                                                CartToBill.data[i].soLuong,
                                                                donGia:
                                                                CartToBill.data[i].donGia,
                                                            }
                                                        )
                                                        .then(function (billdetail) {
                                                            //xóa giỏ hàng by user
                                                            $http.delete(
                                                                "http://localhost:8080/api/cart/deleteAllCart/"+idKhachHang
                                                            );

                                                            //get số lượng sản phẩm đang có
                                                            var getPram = {
                                                                IdProduct:
                                                                CartToBill.data[i].chiTietSanPham
                                                                    .idCTSP,
                                                                IdColor:
                                                                CartToBill.data[i].mauSac,
                                                                IdSize: CartToBill.data[i].kichThuoc,
                                                            };
                                                            $http({
                                                                method: "GET",
                                                                url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/getQuantityProductAndColorAndSize",
                                                                params: getPram,
                                                            }).then(function (resp) {
                                                                var param2 = {
                                                                    IdProduct:
                                                                    CartToBill.data[i]
                                                                        .chiTietSanPham.idCTSP,
                                                                    IdColor:
                                                                    CartToBill.data[i].mauSac,
                                                                    IdSize:
                                                                    CartToBill.data[i].kichThuoc,
                                                                    Quantity:
                                                                        parseInt(resp.data) -
                                                                        parseInt(
                                                                            CartToBill.data[i].soLuong
                                                                        ),
                                                                };
                                                                $http({
                                                                    method: "PUT",
                                                                    url: "http://localhost:8080/api/kichthuoc-mausac-ctsp/updateQuantity",
                                                                    params: param2,
                                                                }).then(function (resp) {
                                                                    let params = {
                                                                        totalPrice:
                                                                        $scope.tongTien,
                                                                        code: bill.data.maHoaDon,
                                                                    };
                                                                    $http({
                                                                        method: "GET",
                                                                        url: "http://localhost:8080/api/vnpay",
                                                                        params: params,
                                                                        transformResponse: [
                                                                            function (data) {
                                                                                location.href = data;
                                                                            },
                                                                        ],
                                                                    });
                                                                });
                                                            });
                                                        });
                                                }
                                            });
                                    });
                            });

                    } else {
                        Swal.fire("Có lỗi xảy ra !", "", "error");
                    }
                }
            });
        };

        //load cart by user
        $scope.listCart = [];
        $http.get("http://localhost:8080/api/cart/"+idKhachHang).then(function (cart) {
            $scope.listCart = cart.data;

            $scope.tongTien = 0;
            for (let i = 0; i < $scope.listCart.length; i++) {
                $scope.tongTien +=
                    $scope.listCart[i].donGia * $scope.listCart[i].soLuong;
            }
        });
        $scope.listAddress = [];
        //load address by user
        $http.get("http://localhost:8080/api/diachi/"+idKhachHang).then(function (address) {
            $scope.listAddress = address.data;
            let idAddress = address.data[0].id;
            //load cart by user
            $http.get("http://localhost:8080/api/cart/"+idKhachHang).then(function (cart) {
                let TotalPrice = 0;

                for (let i = 0; i < cart.data.length; i++) {
                    TotalPrice +=
                        parseFloat(cart.data[i].unitPrice) *
                        parseFloat(cart.data[i].quantity);
                }


                // lấy thông tin địa chỉ giao hàng
                $http
                    .get("http://localhost:8080/api/address/get/" + idAddress)
                    .then(function (resp) {
                        var params = {
                            service_type_id: 2,
                            insurance_value: parseInt(TotalPrice),
                            coupon: null,
                            from_district_id: 1482,
                            to_district_id: resp.data.districtId,
                            to_ward_code: resp.data.wardId,
                            height: 0,
                            length: 0,
                            weight: parseInt(TotalGam),
                            width: 0,
                        };
                        // get phí ship từ GHN
                        $http({
                            method: "GET",
                            url: "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee",
                            params: params,
                            headers: {
                                "Content-Type": undefined,
                                token: "f22a8bb9-632c-11ee-b394-8ac29577e80e",
                                shop_id: 4603004,
                            },
                        }).then(function (ship) {
                            $scope.phiShip = ship.data.data.total;
                            $scope.tienThanhToan = TotalPrice + ship.data.data.total;


                        });
                    });
            });

            //thay đổi địa chỉ giao hàng
            $scope.changeAddress = function () {
                let idAddress = document.getElementById("idAddress").value;
                //load cart by user
                $http.get("http://localhost:8080/api/cart/1").then(function (cart) {
                    let TotalPrice = 0;

                    let TotalGam = 0;
                    for (let i = 0; i < cart.data.length; i++) {
                        TotalPrice +=
                            parseFloat(cart.data[i].unitPrice) *
                            parseFloat(cart.data[i].quantity);
                    }
                    for (let i = 0; i < cart.data.length; i++) {
                        TotalGam +=
                            cart.data[i].productDetail.weight * cart.data[i].quantity;
                    }

                    // lấy thông tin địa chỉ giao hàng
                    $http
                        .get("http://localhost:8080/api/address/get/" + idAddress)
                        .then(function (resp) {
                            var params = {
                                service_type_id: 2,
                                insurance_value: parseInt(TotalPrice),
                                coupon: null,
                                from_district_id: 1482,
                                to_district_id: resp.data.districtId,
                                to_ward_code: resp.data.wardId,
                                height: 0,
                                length: 0,
                                weight: parseInt(TotalGam),
                                width: 0,
                            };
                            // get phí ship từ GHN
                            $http({
                                method: "GET",
                                url: "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee",
                                params: params,
                                headers: {
                                    "Content-Type": undefined,
                                    token: "f22a8bb9-632c-11ee-b394-8ac29577e80e",
                                    shop_id: 4603004,
                                },
                            }).then(function (resp) {
                                $scope.phiShip = resp.data.data.total;
                                $scope.tienThanhToan = TotalPrice + resp.data.data.total - $scope.giamGia;
                            });
                        });
                });
            };
        });

        $scope.listCheck = [];
        $scope.listCheck1 = [];
        $scope.voucherGiamGia = 0;
        $scope.couponGiamGia = 0;
        $scope.apMa = function(){

            let code =document.getElementById('coupon-code').value;
            if(code.trim() === ''){
                Swal.fire('Mã không được để trống !',"","error");
                return;
            }

            $http.get("http://localhost:8080/api/cart/1").then(function (cart) {
                let TotalPrice = 0;
                for (let i = 0; i < cart.data.length; i++) {
                    TotalPrice +=
                        parseFloat(cart.data[i].unitPrice) *
                        parseFloat(cart.data[i].quantity);
                }




                $scope.listVoucher = [];
                $scope.listCoupon = [];

                var checkCode = {
                    code : ''
                }
                var checkCode1 = {
                    code : ''
                }
                //check voucher

                let checkk = 0;
                $http.get('http://localhost:8080/api/getvoucher').then(function(resp){
                    $scope.listVoucher = resp.data
                    for(let i = 0; i < $scope.listVoucher.length; i++){
                        if(code === $scope.listVoucher[i].code){

                            if($scope.listCheck.length > 0){
                                Swal.fire('Bạn chỉ được áp dụng 1 mã khuyến mãi !',"","error");
                                return;
                            }
                            $scope.voucherName = $scope.listVoucher[i].name;
                            $scope.voucherType = $scope.listVoucher[i].typeVoucher;
                            $scope.voucherIs = $scope.listVoucher[i].isVoucher;
                            $scope.discountVoucher = $scope.listVoucher[i].discount + '%';
                            $scope.cashVoucher = $scope.listVoucher[i].cash.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
                            if($scope.listVoucher[i].isVoucher === false){
                                if($scope.listVoucher[i].typeVoucher === false){

                                    if($scope.listVoucher[i].cash > TotalPrice){
                                        $scope.giamGia += TotalPrice;
                                        $scope.voucherGiamGia += TotalPrice;

                                    }
                                    else{
                                        $scope.giamGia += $scope.listVoucher[i].cash;
                                        $scope.voucherGiamGia += $scope.listVoucher[i].cash;
                                    }

                                }
                                else{
                                    $scope.giamGia += (TotalPrice * ($scope.listVoucher[i].discount * 0.01));
                                    $scope.voucherGiamGia += (TotalPrice * ($scope.listVoucher[i].discount * 0.01));
                                }
                                $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);
                                checkCode = {
                                    code : code
                                }




                                checkk++;
                                $scope.listCheck.push(checkCode);
                                $scope.checkVoucher = true;
                                Swal.fire("Áp mã thành công !","","success");
                                document.getElementById('coupon-code').value = '';
                                if(TotalPrice < $scope.giamGia){
                                    Swal.fire('Số tiền giảm đã ở mức tối đa','','error');
                                    $scope.checkVoucher = false;
                                    $scope.listCheck = [];
                                    $scope.giamGia = $scope.couponGiamGia;
                                    $scope.voucherGiamGia = 0;
                                    $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);
                                }
                            }

                            else{

                                $scope.listSPVoucher = [];

                                for (let j = 0; j < cart.data.length; j++) {
                                    $http.get("http://localhost:8080/api/productvoucher/getbyproduct/"+cart.data[j].productDetail.product.id).then(function(resp){


                                        if(resp.data.length > 0){
                                            let Price = cart.data[j].quantity * cart.data[j].unitPrice;

                                            for(let i = 0; i < resp.data.length; i++){


                                                if(resp.data[i].voucher.code == code){

                                                    $scope.listSPVoucher.push(cart.data[j]);


                                                    if(resp.data[i].voucher.typeVoucher === false){

                                                        if(resp.data[i].voucher.cash > Price){

                                                            $scope.giamGia += Price;
                                                            $scope.voucherGiamGia += Price;

                                                        }
                                                        else{
                                                            $scope.giamGia += resp.data[i].voucher.cash;
                                                            $scope.voucherGiamGia += resp.data[i].voucher.cash;
                                                        }

                                                    }
                                                    else{
                                                        $scope.giamGia += (Price * (resp.data[i].voucher.discount * 0.01));
                                                        $scope.voucherGiamGia += (Price * (resp.data[i].voucher.discount * 0.01));
                                                    }








                                                }


                                            }


                                        }


                                        checkCode = {
                                            code : code
                                        }

                                        if($scope.listSPVoucher.length === 0 ){
                                            checkk++;
                                            $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + 0);

                                            Swal.fire("Rất tiếc voucher này không áp dụng cho sản phẩm nào trong giỏ hàng của bạn !","","error");
                                        }
                                        else{
                                            $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);
                                            checkk++;
                                            $scope.listCheck.push(checkCode);
                                            $scope.checkVoucher = true;
                                            Swal.fire("Áp mã thành công !","","success");
                                            document.getElementById('coupon-code').value = '';
                                        }
                                        if(TotalPrice < $scope.giamGia){
                                            Swal.fire('Số tiền giảm đã ở mức tối đa','','error');
                                            $scope.checkVoucher = false;
                                            $scope.listCheck = [];
                                            $scope.giamGia = $scope.couponGiamGia;
                                            $scope.voucherGiamGia = 0;
                                            $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);
                                        }

                                    })




                                }





                            }













                        }


                    }




                })

                //check coupon
                $http.get('http://localhost:8080/api/getcoupon/1').then(function(resp){
                    $scope.listCoupon = resp.data
                    for(let i = 0; i < $scope.listCoupon.length; i++){
                        if(code === $scope.listCoupon[i].code){

                            if($scope.listCheck1.length > 0){
                                Swal.fire('Bạn chỉ được áp dụng 1 phiếu giảm giá !',"","error");
                                return;
                            }

                            $scope.couponName = $scope.listCoupon[i].name;
                            $scope.couponType = $scope.listCoupon[i].isType;
                            $scope.discountCoupon = $scope.listCoupon[i].discount + '%';
                            $scope.cashCoupon = $scope.listCoupon[i].cash.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
                            if($scope.listCoupon[i].isType === 0){

                                if($scope.listCoupon[i].cash > TotalPrice){
                                    $scope.giamGia += TotalPrice;
                                    $scope.couponGiamGia = TotalPrice;

                                }
                                else{
                                    $scope.giamGia += $scope.listCoupon[i].cash
                                    $scope.couponGiamGia = $scope.listCoupon[i].cash;
                                }

                            }
                            else{
                                $scope.giamGia += (TotalPrice * ($scope.listCoupon[i].discount * 0.01));
                                $scope.couponGiamGia = (TotalPrice * ($scope.listCoupon[i].discount * 0.01));
                            }

                            checkCode1 = {
                                code : code
                            }
                            checkk++;
                            $scope.listCheck1.push(checkCode1);
                            $scope.checkCoupon = true;
                            Swal.fire("Áp mã thành công !","","success");
                            document.getElementById('coupon-code').value = '';
                            $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);



                        }


                    }
                    if(TotalPrice < $scope.giamGia){
                        Swal.fire('Số tiền giảm đã ở mức tối đa','','error');
                        $scope.checkCoupon = false;
                        $scope.listCheck1 = [];
                        $scope.giamGia = $scope.voucherGiamGia;
                        $scope.couponGiamGia = 0;
                        $scope.tienThanhToan = TotalPrice + $scope.phiShip - ($scope.couponGiamGia + $scope.voucherGiamGia);
                    }


                })


                if(checkk === 0){
                    Swal.fire('Mã không tồn tại !',"","error");
                    // document.getElementById('voucher').style.display = 'none';
                    return;
                }

            })



        }


        $scope.removeVoucher = function(){
            $scope.voucherGiamGia = 0;
            $scope.listCheck = [];
            $scope.giamGia = 0;
            $scope.voucherType = false;
            document.getElementById('coupon-code').value = '';
            $http.get("http://localhost:8080/api/cart/1").then(function (cart) {
                let TotalPrice = 0;
                for (let i = 0; i < cart.data.length; i++) {
                    TotalPrice +=
                        parseFloat(cart.data[i].unitPrice) *
                        parseFloat(cart.data[i].quantity);
                }
                $scope.checkVoucher = false;
                $scope.giamGia = $scope.couponGiamGia - $scope.giamGia;
                $scope.tienThanhToan = TotalPrice + $scope.phiShip - $scope.couponGiamGia + $scope.voucherGiamGia;

            })

        }
        $scope.removeCoupon = function(){
            $scope.couponGiamGia = 0;
            $scope.listCheck1 = [];
            $scope.giamGia = 0;
            document.getElementById('voucher1').style.display = 'none';
            document.getElementById('coupon-code').value = '';
            $http.get("http://localhost:8080/api/cart/1").then(function (cart) {
                let TotalPrice = 0;
                for (let i = 0; i < cart.data.length; i++) {
                    TotalPrice +=
                        parseFloat(cart.data[i].unitPrice) *
                        parseFloat(cart.data[i].quantity);
                }
                $scope.checkCoupon = false;
                $scope.giamGia = $scope.voucherGiamGia - $scope.giamGia;
                $scope.tienThanhToan = TotalPrice + $scope.phiShip - $scope.voucherGiamGia +  $scope.couponGiamGia;

            })

        }
    };





})