setTabsHeader('shop');

$(document).ready(function () {

    console.log(123)

    var tinhThanhPhoSelected, quanHuyenSelected, phuongXaSelected;
    var tinhThanhPho = $('#tinhThanhPho');
    var quanHuyen = $('#quanHuyen');
    var phuongXa = $('#phuongXa');
    var provinceArr = [];
    var districtArr = [];
    var wardArr = [];
    var ward;
    var district;
    var province;

    $.ajax({
        type: "GET",
        url: "/assets/address-json/province.json",
        contentType: "application/json",
        success: function (response) {
            provinceArr = response;
            console.log(response)
            console.log(123)
            $.each(response, function (index, item) {
                let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
                tinhThanhPho.append(html);
            });
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error);
        }
    });

    $.ajax({
        type: "GET",
        url: "/assets/address-json/district.json",
        contentType: "application/json",
        success: function (response) {
            districtArr = response;
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error);
        }
    });

    $.ajax({
        type: "GET",
        url: "/assets/address-json/ward.json",
        contentType: "application/json",
        success: function (response) {
            wardArr = response;
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error);
        }
    })

    var houseNumberOfCustomer = $('#houseNumber').text();
    $('#customerHouseNumber').val(houseNumberOfCustomer);
    var wardCodeOfCustomer = $('#ward').text();
    var districtOfCustomer = $('#district').text();
    var provinceOfCustomer = $('#province').text();
    console.log(houseNumberOfCustomer + ',' + wardCodeOfCustomer + ',' + districtOfCustomer + ',' + provinceOfCustomer);
    if(wardCodeOfCustomer != null &&
        districtOfCustomer != null &&
        provinceOfCustomer != null) {
        provinceArr.forEach(pro => {
            if(pro.ProvinceName == provinceOfCustomer) {
                tinhThanhPho.append(`<option value="${pro.provinceID}" selected>${pro.ProvinceName}</option>`);
            }
        })

        districtArr.forEach(dis => {
            if(dis.DistrictName == districtOfCustomer) {
                quanHuyen.append(`<option value="${dis.DistrictID}" selected>${dis.DistrictName}</option>`);
            }
        })

        wardArr.forEach(ward => {
            if(ward.Name == wardCodeOfCustomer) {
                phuongXa.append(`<option value="${ward.Code}" selected>${ward.Name}</option>`);
            }
        })
    }

    tinhThanhPho.on('change', function () {
        tinhThanhPhoSelected = tinhThanhPho.val();
        province = tinhThanhPho.find("option:selected").text();
        quanHuyen.html('<option value="">Quận/Huyện</option>');
        phuongXa.html('<option value="">Phường/Xã</option>');
        if(tinhThanhPhoSelected) {
            districtArr.forEach(function (item) {
                if (item.ProvinceID == tinhThanhPhoSelected) {
                    let html = `<option value="${item.DistrictID}">${String(item.DistrictName)}</option>`;
                    quanHuyen.append(html);
                }
            })
        }
    })

    quanHuyen.on('change', function () {
        quanHuyenSelected = quanHuyen.val();
        district = quanHuyen.find("option:selected").text();
        phuongXa.html('<option value="">Phường/Xã</option>');
        wardArr.forEach((item) => {
            if(item.DistrictID == quanHuyenSelected) {
                let html = `<option value="${item.Code}">${String(item.Name)}</option>`;
                phuongXa.append(html);
            }
        })
    })

    phuongXa.on('change', function () {
        phuongXaSelected = phuongXa.val();
        ward = phuongXa.find("option:selected").text();
        var total = document.getElementById("totalAmount").textContent.replace(/[.,]/g, '');
        $.ajax({
            type: "POST",
            url: "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create",
            contentType: "application/json",
            headers: {
                "Token": "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551"
            },
            data: JSON.stringify(
                {
                    shop_id: "190713",
                    from_name: "LightBee Shop",
                    from_phone: "0359966461",
                    from_address: "Trường Cao Đẳng FPT Polytechnic",
                    from_ward_name: "Phường Xuân Phương",
                    from_district_name: "Nam Từ Liêm",
                    from_province_name: "Hà Nội",
                    to_name: "test",
                    to_phone: "0359966461",
                    to_address: "Nam Đinh",
                    to_ward_code: phuongXaSelected,
                    to_district_id: quanHuyenSelected,
                    service_id: 55320,
                    service_type_id: 2,
                    payment_type_id: 2,
                    cod_amount: parseInt(200000),
                    required_note: "CHOXEMHANGKHONGTHU",
                    items: [
                        {
                            name:"Áo Polo",
                            code:"Polo123",
                            quantity: 1,
                            price: 200000,
                            length: 12,
                            width: 12,
                            height: 12,
                            weight: 1200,
                            category:
                                {
                                    level1:"Áo"
                                }
                        }
                    ],
                    weight: 2000,
                    length: 1,
                    width: 19,
                    height: 10
                }
            ),
            success: function (response) {
                $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
                $('#totalAmount').text(parseFloat(parseInt(total) + parseInt(response.data.total_fee)).toLocaleString('en-US'));
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error)
            }
        })
    })

    $('#placeOrder').on('click', function () {
        var total = parseInt($('#total').text().replace(/[,.]/g, ''));
        var shippingFee = parseInt($('#shippingFee').text().replace(/[,.]/g, ''));
        var houseNumber = $('#customerHouseNumber').val();
        console.log(houseNumber + ',' + ward + ',' + district + ',' + province);
        var customerPhone = $('#customerPhone').val();
        var customerName = $('#customerName').val();
        var typeOfPayment = $('input[name=paymentMethod]:checked').val();
        console.log(typeOfPayment);
        var voucherCode = $('#voucherCode').attr('voucher-code');
        var orderNotes = $('#orderNotes').val();
        if(orderNotes == '' || orderNotes == null) {
            orderNotes = 'Note';
        }
        var totalAmount = parseInt(document.getElementById('totalAmount').textContent.replace(/[,.]/g, ''));
        console.log(totalAmount);
        var productDetailList = [];
        var products = document.querySelectorAll('.productsInCart');
        products.forEach((product) => {
            var productDetailId = product.querySelector('.productDetailId').textContent;
            var quantity = product.querySelector('.quantityProduct').textContent;
            var productDetail = {
                productDetailId: productDetailId,
                quantity: quantity
            }
            productDetailList.push(productDetail);
        })
        console.log(orderNotes + "-" + totalAmount);

        var paymentDto = {
            notes: orderNotes,
            total: total,
            shippingFee: shippingFee,
            totalAmount: totalAmount,
            productDetail: productDetailList,
            voucher: voucherCode,
            customerPhone: customerPhone,
            customerName: customerName,
            addressReceive: houseNumber + "," + ward + "," + district + "," + province
        }
        console.log(paymentDto);

        if(typeOfPayment == 'whenReceive') {
            $.ajax({
                type: "POST",
                url: "/check-out/placeOrder-whenReceive",
                contentType: "application/json",
                data: JSON.stringify(paymentDto),
                success: function (response) {
                    console.log(response);
                    window.location.href = response;
                },
                error: function (error) {
                    console.error('Xảy ra lỗi: ', error);
                }
            })
        }
        if(typeOfPayment == 'online') {
            $.ajax({
                type: "POST",
                url: "/check-out/placeOrder-online",
                contentType: "application/json",
                data: JSON.stringify(paymentDto),
                success: function (response) {
                    console.log(response);
                    window.location.href = response;
                },
                error: function (error) {
                    console.error('Xảy ra lỗi: ', error);
                }
            })
        }
    })

})