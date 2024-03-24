setTabsHeader('shop');
var provinceArr = [];
var districtArr = [];
var wardArr = [];
let arrXa = [];
var ward;
var district;
var province;
var wardName;
var districtName;
var provinceName;
var houseNumber;
var orderCode;
fetch('/assets/address-json/province.json')
    .then(response => response.json())
    .then(data => {
        provinceArr = data;
        data.forEach((item) => {
            let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
            $('#newProvince').append(html);
           $('#tinhThanhPho').append(html);
        })
        $('#newProvince').niceSelect('update');
        $('#tinhThanhPho').niceSelect('update');
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
                arrXa = [...data];
                localStorage.setItem('arrXa', JSON.stringify(arrXa))
                console.log(data);
                if (username !== undefined) {
                    callApiShippingFee();
                }
            });
    })
    .catch(error => console.error('Error:', error));


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
            orderCode = response.data.order_code;
            var shippingFee = response.data.total_fee;
            if(totalAmount > 2000000) {
                $('#shippingFee').text('Miễn phí');
                shippingFee = 0;
            } else {
                $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
            }
            $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(shippingFee)).toLocaleString('en-US'));
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
})

$(document).on('click', '#btn-addAddress', function () {
    var idCustomer = $('#idCustomer').attr('idCustomer');
    var newHouseNumber = $('#newHouseNumber').val();
    var newProvinceText = $('#newProvince').find('option:selected').text();
    var newProvinceId = $('#newProvince').val();
    var newDistrictText = $('#newDistrict').find('option:selected').text();
    var newDistrictId = $('#newDistrict').val();
    var newWardText = $('#newWard').find('option:selected').text();
    var newWardCode = $('#newWard').val();
    var diaChiDto = {
        soNhaDto: newHouseNumber,
        phuongXaDto: newWardText,
        quanHuyenDto: newDistrictText,
        tinhThanhPhoDto: newProvinceText,
        idKH: idCustomer
    }

    $.ajax({
        type: "POST",
        url: "/cms/khach-hang/update/add-diachi",
        data: {
            soNhaDto: newHouseNumber,
            phuongXaDto: newWardText,
            quanHuyenDto: newDistrictText,
            tinhThanhPhoDto: newProvinceText,
            idKH: idCustomer
        },
        success: function (response) {
            console.log(response)
            ToastSuccess('Thêm mới địa chỉ thành công');
            $('#newAddress').modal('hide');
            let wrapper_address = $('.wrapper_address');
            wrapper_address.append(`<div class="item_address mb-1">
                                    <div class="content_dress row m-0 customerAddress">
                                        <div class="checkbox-wrapper-30 col-1">
                                   <span class="checkbox">
                                     <input class="selected_product" type="radio" name="address" data-cart-id=""/>
                                     <svg>
                                       <use xlink:href="#checkbox-30" class="checkbox"></use>
                                     </svg>
                                   </span>
                                            <svg xmlns="http://www.w3.org/2000/svg" style="display:none">
                                                <symbol id="checkbox-30" viewBox="0 0 22 22">
                                                    <path fill="none" stroke="currentColor"
                                                          d="M5.5,11.3L9,14.8L20.2,3.3l0,0c-0.5-1-1.5-1.8-2.7-1.8h-13c-1.7,0-3,1.3-3,3v13c0,1.7,1.3,3,3,3h13 c1.7,0,3-1.3,3-3v-13c0-0.4-0.1-0.8-0.3-1.2"/>
                                                </symbol>
                                            </svg>
                                        </div>
                                        <div class="col-11 d-flex justify-content-between">
                                            <div class="pt-3">
                                                <label class="w-100 txt_address mb-0">Địa Chỉ: <label class="mb-0 customerHouseNumber">${newHouseNumber}</label></label>
                                                <div class="d-flex flex-row">
                                                    <label class="txt_address customerWard">${newWardText},</label>
                                                    <label class="txt_address customerDistrict">${newDistrictText},</label>
                                                    <label class="txt_address customerProvince">${newProvinceText}</label>
                                                </div>
                                                <div>
                                                    <span class="address_default">Mặc Định</span>
                                                </div>
                                            </div>
                                            <div class="btn-action-address">
                                                <div class="d-flex justify-content-center">
                                                    <a class="mr-2" href="javascript:;">Cập Nhật</a>
                                                    <a href="javascript:;">Xóa</a>
                                                </div>
                                                <button type="button" class="info-btn disabled">Đặt Mặc Định</button>
                                            </div>
                                        </div>

                                    </div>
                                </div>`);
            $.ajax({
                type: "POST",
                url: "/cms/khach-hang/set-default-address",
                data: {
                    idDiaChi: response.id,
                    idKhachHang: idCustomer
                },
                success: function (response) {
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
                                to_ward_code: newWardCode,
                                to_district_id: newDistrictId,
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
                            orderCode = response.data.order_code;
                            var totalAmount = parseFloat(document.getElementById("totalAmount").textContent.replace(/[.,]/g, ''));
                            var shippingFee = response.data.total_fee;
                            if(totalAmount > 2000000) {
                                $('#shippingFee').text('Miễn phí');
                                shippingFee = 0;
                            } else {
                                $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
                            }
                            $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(shippingFee)).toLocaleString('en-US'));
                            $('#leadTime').text(new Date(response.data.expected_delivery_time).toLocaleDateString('vi-VN'));
                        },
                        error: function (error) {
                            console.error('Xảy ra lỗi: ', error)
                            $('#shippingFee').text('Không hỗ trợ giao');
                        }
                    })
                },
                error: function (error) {
                    console.error('Xảy ra lỗi: ', error)
                }
            })
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error)
            ToastError('Thêm mói địa chỉ thất bại')
        }
    })
})

$('#newDistrict').on('change', function () {
    let qh = $('#newDistrict').val();
    console.log(qh)
    let arr = JSON.parse(localStorage.getItem('arrXa'))
    console.log(arr)
    $('#newWard').html('<option value="">Phường/Xã</option>');
    arr.forEach((item) => {
        if (item.DistrictID == qh) {
            console.log(item)
            let html = `<option value="${item.Code}">${String(item.Name)}</option>`;
            $('#newWard').append(html);
        }
    })
    $('#newWard').niceSelect('update');
    districtName = $('#newDistrict').find("option:selected").text();
})
$(document).ready(function () {
    let ele_quanHuyen = $('#newDistrict');
    let ele_phuongXa = $('#newWard')
    let ele_tinh = $('#newProvince');
    let phuongXaVal;
    let quanHuyenVal;
    let tinhTP;

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
        $(ele_quanHuyen).niceSelect('update')
    })

    ele_phuongXa.on('change', function () {
        phuongXaVal = ele_phuongXa.val();
        wardName = ele_phuongXa.find("option:selected").text();
    })
    console.log(123)

    var tinhThanhPhoSelected, quanHuyenSelected, phuongXaSelected;
    var tinhThanhPho = $('#tinhThanhPho');
    var quanHuyen = $('#quanHuyen');
    var phuongXa = $('#phuongXa');

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
        quanHuyen.niceSelect('update')
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
        phuongXa.niceSelect('update')
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
                orderCode = response.data.order_code;
                var shippingFee = response.data.total_fee;
                if(totalAmount > 2000000) {
                    $('#shippingFee').text('Miễn phí');
                } else {
                    $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
                }
                $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(shippingFee)).toLocaleString('en-US'));
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
        var shippingFee = $('#shippingFee').text().replace(/[,.]/g, '');
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
                orderCode = response.data.order_code;
                var shippingFee = response.data.total_fee;
                if(totalAmount > 2000000) {
                    $('#shippingFee').text('Miễn phí');
                    shippingFee = 0;
                } else {
                    $('#shippingFee').text(parseFloat(response.data.total_fee).toLocaleString('en-US'));
                }
                $('#totalAmount').text(parseFloat(parseInt(totalAmount) + parseInt(shippingFee)).toLocaleString('en-US'));
                $('#leadTime').text(new Date(response.data.expected_delivery_time).toLocaleDateString('vi-VN'));
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error)
                $('#shippingFee').text('Không hỗ trợ giao');
            }
        })
    })

    $('#placeOrder').on('click', function () {
        const regex = /^[0-9]*$/;

        var total = parseInt($('#total').text().replace(/[,.]/g, ''));
        var voucherValue = 0;
        if (!isNaN(parseInt($('#voucherValue').text().replace(/[,.]/g, '')))) {
            voucherValue = parseInt($('#voucherValue').text().replace(/[,.]/g, ''));
        }
        var shippingFeeText = $('#shippingFee').text();
        var shippingFee = 0;
        if(shippingFeeText != 'Miễn phí') {
            shippingFee = parseInt(shippingFeeText.replace(/[,.]/g, ''));
        }
        console.log(voucherValue);
        if ($('#customerHouseNumber').val() != null || $('#customerHouseNumber').val() != undefined) {
            houseNumber = $('#customerHouseNumber').val();
        }
        console.log(houseNumber + ',' + ward + ',' + district + ',' + province);
        var customerPhone = $('#customerPhone').val();
        var customerEmail = $('#customerEmail').val();
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
            orderCode: orderCode,
            notes: orderNotes,
            total: total,
            shippingFee: shippingFee,
            totalAmount: totalAmount,
            productDetail: productDetailList,
            voucher: voucherCode,
            voucherValue: voucherValue,
            customerPhone: customerPhone,
            customerName: customerName,
            customerEmail: customerEmail,
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