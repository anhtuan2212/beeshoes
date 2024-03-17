setTabsHeader('shop');
var provinceArr = [];
var districtArr = [];
var wardArr = [];
var ward;
var district;
var province;
var wardName;
var districtName;
var provinceName;
var houseNumber;
fetch('/assets/address-json/province.json')
    .then(response => response.json())
    .then(data => {
        provinceArr = data;
        data.forEach((item) => {
            let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
            $('#newProvince').append(html);
        })
        return fillAllTinh(data);
    })
    .then(() => {
        return fetch('/assets/address-json/district.json')
            .then(response => response.json())
            .then(data => {
                districtArr = data;
            });
    })
    .then(() => {
        return fetch('/assets/address-json/ward.json')
            .then(response => response.json())
            .then(data => {
                wardArr = data;
                console.log(data);
                if (username !== undefined) {
                    callApiShippingFee();
                }
            });
    })
    .catch(error => console.error('Error:', error));

function fillAllTinh(data) {
    let tinhThanhPho = $('#tinhThanhPho');
    $.each(data, function (index, item) {
        let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
        tinhThanhPho.append(html);
    });
}
function callApiShippingFee() {
    let defaultCheckedValue = $(".selected_product:checked").closest('.customerAddress');
    houseNumber = defaultCheckedValue.find('.customerHouseNumber').text().replace(/[,.]/g, '');
    wardName = defaultCheckedValue.find('.customerWard').text().replace(/[,.]/g, '');
    districtName = defaultCheckedValue.find('.customerDistrict').text().replace(/[,.]/g, '');
    provinceName = defaultCheckedValue.find('.customerProvince').text().replace(/[,.]/g, '');
    var totalAmount = $('#totalAmount').text().replace(/[,.]/g, '');
    console.log(wardName + ',' + districtName + ',' + provinceName);
    console.log(wardArr);
    console.log(districtArr);
    console.log(provinceArr)
    provinceArr.forEach((item) => {
        if (item.ProvinceName == provinceName) {
            console.log(item.ProvinceName)
            province = item.ProvinceID;
        }
    })
    districtArr.forEach((item) => {
        if (item.ProvinceID == province && item.DistrictName == districtName) {
            console.log(item.DistrictName)
            district = item.DistrictID;
        }
    })
    wardArr.forEach((item) => {
        if (item.DistrictID = district && item.Name == wardName) {
            console.log(item.Name)
            ward = item.Code;
        }
    })
    console.log(ward + ',' + district + ',' + province);

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
                to_ward_code: ward,
                to_district_id: district,
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
            console.log(response);
            $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
            $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(response.data.total_fee)).toLocaleString('en-US'));
            $('#leadTime').text(new Date(response.data.expected_delivery_time).toLocaleDateString('vi-VN'));
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error)
            $('#shippingFee').text('Không hỗ trợ giao');
        }
    })
}

$(document).on('click', '#addNewAddress', function () {
    $('#newAddress').modal('show');
    let ele_tinh = $('#newProvince');
    let ele_quanHuyen = $('#newDistrict');
    let ele_phuongXa = $('#newWard')
    let phuongXa;
    let quanHuyen;
    let tinhTP;
    ele_quanHuyen.empty()
    ele_phuongXa.empty()
    ele_tinh.on('change', function () {
        tinhTP = ele_tinh.val();
        provinceName = ele_tinh.find("option:selected").text();
        ele_quanHuyen.html('<option value="">Quận/Huyện</option>');
        ele_phuongXa.html('<option value="">Phường/Xã</option>');
        if (tinhTP) {
            districtArr.forEach(function (item) {
                if (item.ProvinceID == tinhTP) {
                    let html = `<option value="${item.DistrictID}">${String(item.DistrictName)}</option>`;
                    ele_quanHuyen.append(html);
                }
            })
        }
    })

    ele_quanHuyen.on('change', function () {
        quanHuyen = ele_quanHuyen.val();
        districtName = ele_quanHuyen.find("option:selected").text();
        phuongXa.html('<option value="">Phường/Xã</option>');
        wardArr.forEach((item) => {
            if (item.DistrictID == quanHuyen) {
                let html = `<option value="${item.Code}">${String(item.Name)}</option>`;
                phuongXa.append(html);
            }
        })
    })

    ele_phuongXa.on('change', function () {
        phuongXa = ele_phuongXa.val();
        wardName = ele_phuongXa.find("option:selected").text();
    })

})

$(document).ready(function () {

    console.log(123)

    var tinhThanhPhoSelected, quanHuyenSelected, phuongXaSelected;
    var tinhThanhPho = $('#tinhThanhPho');
    var quanHuyen = $('#quanHuyen');
    var phuongXa = $('#phuongXa');

    // $.ajax({
    //     type: "GET",
    //     url: "/assets/address-json/province.json",
    //     contentType: "application/json",
    //     success: function (response) {
    //         provinceArr = response;
    //         console.log(response)
    //         console.log(123)
    //         $.each(response, function (index, item) {
    //             let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
    //             tinhThanhPho.append(html);
    //         });
    //     },
    //     error: function (error) {
    //         console.error('Xảy ra lỗi: ', error);
    //     }
    // });
    //
    // $.ajax({
    //     type: "GET",
    //     url: "/assets/address-json/district.json",
    //     contentType: "application/json",
    //     success: function (response) {
    //         districtArr = response;
    //     },
    //     error: function (error) {
    //         console.error('Xảy ra lỗi: ', error);
    //     }
    // });
    //
    // $.ajax({
    //     type: "GET",
    //     url: "/assets/address-json/ward.json",
    //     contentType: "application/json",
    //     success: function (response) {
    //         wardArr = response;
    //     },
    //     error: function (error) {
    //         console.error('Xảy ra lỗi: ', error);
    //     }
    // })

    tinhThanhPho.on('change', function () {
        tinhThanhPhoSelected = tinhThanhPho.val();
        provinceName = tinhThanhPho.find("option:selected").text();
        quanHuyen.html('<option value="">Quận/Huyện</option>');
        phuongXa.html('<option value="">Phường/Xã</option>');
        if (tinhThanhPhoSelected) {
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
        districtName = quanHuyen.find("option:selected").text();
        phuongXa.html('<option value="">Phường/Xã</option>');
        wardArr.forEach((item) => {
            if (item.DistrictID == quanHuyenSelected) {
                let html = `<option value="${item.Code}">${String(item.Name)}</option>`;
                phuongXa.append(html);
            }
        })
    })

    phuongXa.on('change', function () {
        phuongXaSelected = phuongXa.val();
        wardName = phuongXa.find("option:selected").text();
        var totalAmount = parseFloat(document.getElementById("totalAmount").textContent.replace(/[.,]/g, ''));
        var shippingFee = document.getElementById("totalAmount").textContent.replace(/[.,]/g, '');
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
                            name: "Áo Polo",
                            code: "Polo123",
                            quantity: 1,
                            price: 200000,
                            length: 12,
                            width: 12,
                            height: 12,
                            weight: 1200,
                            category:
                                {
                                    level1: "Áo"
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
                $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(response.data.total_fee)).toLocaleString('en-US'));
                $('#leadTime').text(new Date(response.data.expected_delivery_time).toLocaleDateString('vi-VN'));
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error)
                $('#shippingFee').text('Không hỗ trợ giao');
            }
        })
    })

    $('.selected_product').on('change', function () {
        var totalAmount = $('#totalAmount').text().replace(/[,.]/g, '');
        var shippingFee = $('#shippingFee').text().replace(/[,.]/g,'');
        totalAmount = parseInt(totalAmount) - parseInt(shippingFee);
        var customerAddress = $(this).closest('.customerAddress')
        houseNumber = customerAddress.find('.customerHouseNumber').text().replace(/[,.]/g, '');
        wardName = customerAddress.find('.customerWard').text().replace(/[,.]/g, '');
        districtName = customerAddress.find('.customerDistrict').text().replace(/[,.]/g, '');
        provinceName = customerAddress.find('.customerProvince').text().replace(/[,.]/g, '');
        wardArr.forEach((item) => {
            if (item.Name == wardName) {
                ward = item.Code;
            }
        })
        districtArr.forEach((item) => {
            if (item.DistrictName == districtName) {
                district = item.DistrictID;
            }
        })
        provinceArr.forEach((item) => {
            if (item.ProvinceName == provinceName) {
                province = item.ProvinceID;
            }
        })
        console.log(ward + ',' + province + ',' + district);

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
                    to_ward_code: ward,
                    to_district_id: district,
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
                console.log(response);
                $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
                $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(response.data.total_fee)).toLocaleString('en-US'));
                $('#leadTime').text(new Date(response.data.expected_delivery_time).toLocaleDateString('vi-VN'));
                },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error)
                $('#shippingFee').text('Không hỗ trợ giao');
            }
        })
    })

    $('#placeOrder').on('click', function () {
        var total = parseInt($('#total').text().replace(/[,.]/g, ''));
        var voucherValue = 0;
        if(!isNaN(parseInt($('#voucherValue').text().replace(/[,.]/g, '')))) {
            voucherValue = parseInt($('#voucherValue').text().replace(/[,.]/g, ''));
        }
        var shippingFee = parseInt($('#shippingFee').text().replace(/[,.]/g, ''));
        console.log(voucherValue);
        if($('#customerHouseNumber').val() != null || $('#customerHouseNumber').val() != undefined) {
            houseNumber = $('#customerHouseNumber').val();
        }
        console.log(houseNumber + ',' + ward + ',' + district + ',' + province);
        var customerPhone = $('#customerPhone').val();
        var customerName = $('#customerName').val();
        var typeOfPayment = $('input[name=paymentMethod]:checked').val();
        console.log(typeOfPayment);
        var voucherCode = $('#voucherCode').attr('voucher-code');
        var orderNotes = $('#orderNotes').val();
        if (orderNotes == '' || orderNotes == null) {
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
            voucherValue: voucherValue,
            customerPhone: customerPhone,
            customerName: customerName,
            addressReceive: houseNumber + "," + wardName + "," + districtName + "," + provinceName
        }
        console.log(paymentDto);

        if (typeOfPayment == 'whenReceive') {
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
        if (typeOfPayment == 'online') {
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