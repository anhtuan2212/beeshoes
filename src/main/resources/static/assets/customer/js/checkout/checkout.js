setTabsHeader('shop');

$(document).ready(function () {
    console.log(123)

    var tinhThanhPhoSelected, quanHuyenSelected, phuongXaSelected;
    var tinhThanhPho = $('#tinhThanhPho');
    var quanHuyen = $('#quanHuyen');
    var phuongXa = $('#phuongXa');

    $.ajax({
        type: "GET",
        url: "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province",
        contentType: "application/json",
        headers: {
            "Token": "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551"
        },
        success: function (response) {
            console.log(response)
            console.log(123)
            $.each(response.data, function (index, item) {
                let html = `<option value="${item.ProvinceID}">${String(item.ProvinceName)}</option>`;
                tinhThanhPho.append(html);
            });
        },
        error: function (error) {
            console.error('Xảy ra lỗi: ', error);
        }
    });

    tinhThanhPho.on('change', function () {
        tinhThanhPhoSelected = tinhThanhPho.val();
        quanHuyen.html('<option value="">Quận/Huyện</option>');
        phuongXa.html('<option value="">Phường/Xã</option>');
        $.ajax({
            type: "GET",
            url: "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district",
            contentType: "application/json",
            headers: {
                "Token": "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551"
            },
            success: function (response) {
                if(tinhThanhPhoSelected) {
                    response.data.forEach(function (item) {
                        if (item.ProvinceID == tinhThanhPhoSelected) {
                            let html = `<option value="${item.DistrictID}">${String(item.DistrictName)}</option>`;
                            quanHuyen.append(html);
                        }
                    })
                }
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error);
            }
        });
    })

    quanHuyen.on('change', function () {
        quanHuyenSelected = quanHuyen.val();
        phuongXa.html('<option value="">Phường/Xã</option>');
        $.ajax({
            type: "GET",
            url: "https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + quanHuyenSelected,
            contentType: "application/json",
            headers: {
                "Token": "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551"
            },
            success: function (response) {
                if(quanHuyenSelected) {
                    response.data.forEach(function (item) {
                        let html = `<option value="${item.WardCode}">${String(item.WardName)}</option>`;
                        phuongXa.append(html);
                    })
                }
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error);
            }
        })
    })

    phuongXa.on('change', function () {
        phuongXaSelected = phuongXa.val();
        $.ajax({
            type: "POST",
            url: "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee",
            contentType: "application/json",
            headers: {
                "Token": "68b8b44f-a88d-11ee-8bfa-8a2dda8ec551"
            },
            data: JSON.stringify({
                shop_id: "190713",
                weight: 100,
                service_id: 55320,
                to_ward_code: phuongXaSelected,
                to_district_id: parseInt(quanHuyenSelected)
            }),
            success: function (response) {
                console.log(phuongXaSelected);
                console.log(quanHuyenSelected);
                console.log(tinhThanhPhoSelected);
                var fee = response.data.total;
                var formattedMoney = parseFloat(fee).toLocaleString('en-US');
                console.log(formattedMoney);
                $('#shippingFee').text(formattedMoney);
            },
            error: function (error) {
                console.error('Xảy ra lỗi: ', error);
            }
        })
    })

    $('#placeOrder').on('click', function () {
        var orderNotes = $('#orderNotes').val();
        var totalAmount = parseInt($('#totalAmount').text().replace(/\./g, ''));
        console.log(orderNotes + "-" + totalAmount);
        var paymentDto = {
            notes: orderNotes,
            total: totalAmount
        }

        console.log(paymentDto);

        $.ajax({
            type: "POST",
            url: "/check-out/placeOrder",
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
    })

})