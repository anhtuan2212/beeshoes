setTabsHeader('pages');
$(document).ready(function () {

    $('#updateInformationModal').modal('show');

    $(document).on('click', '#btn-show-update', function () {
        if ($(this).data('update') == 0) {
            $('.content_product').removeClass('col-7').addClass('col-6')
            $('.btn-del-group').removeClass('d-none').addClass('d-flex');
            $('#btn-add-product').removeClass('d-none');
            $(this).data('update', 1).text('Xong');
        } else {
            $('.content_product').addClass('col-7').removeClass('col-6')
            $('.btn-del-group').addClass('d-none').removeClass('d-flex');
            $('#btn-add-product').addClass('d-none');
            $(this).data('update', 0).text('Cập Nhật');
        }
    })
    $(document).on('click', '.btn-cancel-oders', function () {
        let id = $(this).data('id');
        $('#id_oders_cancel').val(id);
        $('#staticBackdrop').modal('show');
    })

    $(document).on('change', '[name="cancel"]', function () {
        let val = $(this).val()
        let element = $('#lydo_cancel');
        if (val == '#') {
            element.css('animation-name', 'showForm');
            element.removeClass('d-none').addClass('showForm');
        } else {
            element.css('animation-name', 'hideForm');
            element.removeClass('showForm').addClass('hideForm');
            setTimeout(function () {
                element.addClass('d-none').removeClass('hideForm');
            }, 400);
        }
    })

    $(document).on('click', '#btn-huy', function () {
        let id = $('#id_oders_cancel').val();
        let lydo = $('[name="cancel"]:checked').val();
        if (lydo == '#') {
            lydo = $('#lydo_cancel').text();
        }
        $.ajax({
            url: '/api/hoa-don/huy-detail',
            type: 'POST',
            data: {
                id: id,
                lydo: lydo
            },
            success: function (data) {
                console.log(data)
                ToastSuccess('Thành công.')
                $(`.show_trang_thai`).text('Đã Hủy');
                $(`.btn-cancel-oders`).remove();
                $(`#btn-show-update`).remove();
                let element = document.getElementById('history-oder');
                let firstChild = element.firstChild;
                let div = document.createElement('div');
                div.className = 'w-100 row justify-content-center';
                let html = `
                    <div class="col-3 times">
                        ${formatServerTime(data.thoiGian)}
                    </div>
                    <div class="col-9 actions">
                        ${data.hanhDong}
                    </div>`;
                div.innerHTML = html;
                element.insertBefore(div, firstChild);
                let hr = document.createElement('hr');
                div.insertAdjacentElement('afterend', hr);

                $('.step').each((index, ele) => {
                    if (!$(ele).hasClass('active')) {
                        $(ele).remove();
                    }
                })
                $('.track').append(`
                <div class="step active">
                            <span class="icon">
                                <i class="fa fa-times" aria-hidden="true"></i>
                            </span>
                        <span class="text">Đã Hủy</span>
                    </div>
                `)
                $('#staticBackdrop').modal('hide');
            },
            error: function (e) {
                console.log(e)
                $('#staticBackdrop').modal('hide');
            }
        })
    })
    $(document).on('change', '#tenSanPham', function () {
        let id = $(this).val();
        let dataS = dataShop;
        let arr = [];
        let mauSac = $('#mauSac');
        mauSac.empty();
        let kichCo = $('#kichCo');
        kichCo.empty();
        let url = '';
        dataS.forEach((item) => {
            if (item.id == id) {
                item.chiTietSanPham.forEach((chil, ind) => {
                    if (ind === 0) {
                        url = chil.anh;
                        item.chiTietSanPham.forEach((ctsp, i) => {
                            let kc = item.kichCo.find((k) => k.ten == ctsp.kichCo)
                            kichCo.append(`<option value="${kc.id}" ${i === 0 ? 'selected' : ''}>${kc.ten}</option>`)
                        })
                    }
                    if (!arr.includes(chil.mauSac)) {
                        mauSac.append(`<option value="${chil.mauSac}" ${ind === 0 ? 'selected' : ''}>${chil.tenMau}</option>`)
                        arr.push(chil.mauSac);
                    }
                })
            }
        })
        $('#preview-img').attr('src', url);
        $(mauSac).niceSelect('update');
        $(kichCo).niceSelect('update');
    })
    $('#mauSac').on('change', function () {
        let data = dataShop;
        let id_sp = $('#tenSanPham').val();
        let mauSac = $('#mauSac');
        let kichCo = $('#kichCo');
        let mau = mauSac.val();
        let url = '';
        kichCo.empty();
        let object = {};
        for (let i = 0; i < data.length; i++) {
            if (id_sp == data[i].id) {
                object = data[i];
                break;
            }
        }
        object.chiTietSanPham.forEach((item, i) => {
            if (mau == item.mauSac) {
                url = item.anh;
                let size = object.kichCo.find((oj) => oj.ten == item.kichCo);
                console.log(size)
                kichCo.append(`<option value="${size.id}" ${i === 0 ? 'selected' : ''}>${size.ten}</option>`)
            }
        })
        $(kichCo).niceSelect('update');
        if (mauSac.val() === '#') {
            $('#preview-img').attr('src', '/assets/customer/img/icon/user.png');
        } else {
            $('#preview-img').attr('src', url);
        }
    })
    $(document).on('click', '.btn-delete', function () {
        let li = $(this).closest('li.row');
        let id = $(this).data('id-hdct');
        Swal.fire({
            title: "Bạn chắc chứ?",
            text: "Thay đổi sẽ không thể hoàn tác !",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            cancelButtonText: "Hủy",
            confirmButtonText: "Xác Nhận",
            customClass: {
                confirmButton: 'btn-custom-black',
                cancelButton: 'btn-custom-info'
            },
        }).then(async (result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '/api/hoa-don/delete-product',
                    type: 'POST',
                    data: {
                        id: id
                    }, success: function (data) {
                        console.log(data);
                        li.remove();
                        let element = document.getElementById('history-oder');
                        let firstChild = element.firstChild;
                        let div = document.createElement('div');
                        div.className = 'w-100 row justify-content-center';
                        let ht = `
                            <div class="col-3 times">
                                ${formatServerTime(data.times)}
                            </div>
                            <div class="col-9 actions">
                                ${data.message}
                            </div>`;
                        div.innerHTML = ht;
                        element.insertBefore(div, firstChild);
                        let hr = document.createElement('hr');
                        div.insertAdjacentElement('afterend', hr);
                        $('#total-money').text(addCommasToNumber(data.tongTien) + 'đ')
                        $('#discount-money').text(addCommasToNumber(data.giamGia == null ? 0 : data.giamGia) + 'đ')
                        $('#payment-money').text(addCommasToNumber(data.thucThu) + 'đ')
                        ToastSuccess('Thành công.')
                    }, error: function (e, x, h) {
                        switch (e.getResponseHeader('status')) {
                            case 'NotAuth':
                                ToastError('Vui lòng đăng nhập.');
                                break;
                            default:
                                ToastError('Lỗi.')
                        }
                        console.log(e)
                        console.log(x)
                        console.log(h)
                    }
                })
            }
        })

    })
    $('#form-tracking-1,#form-tracking-2').on('submit', function (e) {
        let vl1 = $('#form-value-1').val();
        let vl2 = $('#form-value-2').val();
        if (vl1 !== undefined) {
            if (vl1.length === 0) {
                ToastError('Vui lòng nhập mã hóa đơn.')
                e.preventDefault();
            }
        }
        if (vl2 !== undefined) {
            if (vl2.length === 0) {
                ToastError('Vui lòng nhập mã hóa đơn.')
                e.preventDefault();
            }
        }
    })
    $('#btn-select').on('click', function () {
        let sanPham = $('#tenSanPham').val();
        let mauSac = $('#mauSac').val();
        let kicCo = $('#kichCo').val();
        let soLuong = $('#soLuong').val();
        let id = $('#oder-id-update').val();
        if (mauSac === '#' || mauSac.length === 0) {
            ToastError('Vui lòng chọn màu.')
        }
        if (kicCo.length === 0 || kicCo === '#') {
            ToastError('Vui lòng chọn cỡ.')
        }
        if (soLuong.length === 0 || Number(soLuong) < 1) {
            ToastError('Vui lòng nhập số lượng.')
        }
        if (id.length === 0) {
            ToastError('ID Rỗng.')
        }
        $.ajax({
            url: '/api/hoa-don/add-product',
            type: 'POST',
            data: {
                id: id,
                mauSac: mauSac,
                sanPham: sanPham,
                kichCo: kicCo,
                soLuong: soLuong
            }, success: function (data) {
                console.log(data)
                if (data.sale) {
                    let html = `
                    <li class="row">
                        <div class="col-2 p-0">
                            <img class="product_img" src="${data.anh}">
                        </div>
                        <div id="product-detail-${data.id_hdct}" class="content_product p-0 col-6">
                            <div>
                                <h6 class="product_name mb-2">${data.ten}</h6>
                                <div class="w-100">
                                    Màu :<label>${data.mauSac}</label>,
                                    Kích Cỡ :<label>${data.kichCo}</label>
                                </div>
                                <label class="quantity-product">Số Lượng :${data.soLuong}</label>
                            </div>
                        </div>
                        <div class="col-3 d-flex align-items-center">
                            <h5 class="product_price_cu">${addCommasToNumber(data.giaGoc)}đ</h5>
                            <h5 class="product_price">${addCommasToNumber(data.giaBan)}đ</h5>
                        </div>
                        <div class="col-1 align-items-center btn-del-group d-flex">
                            <button class="btn-delete" data-toggle="tooltip" title="Xóa Sản Phẩm" data-id-hdct="${data.id_hdct}">
                                <i class="fa fa-close"></i>
                            </button>
                        </div>
                    </li>
                    `;
                    $('#show-all-product').append(html);

                    let element = document.getElementById('history-oder');
                    let firstChild = element.firstChild;
                    let div = document.createElement('div');
                    div.className = 'w-100 row justify-content-center';
                    let ht = `
                    <div class="col-3 times">
                        ${formatServerTime(data.times)}
                    </div>
                    <div class="col-9 actions">
                        ${data.message}
                    </div>`;
                    div.innerHTML = ht;
                    element.insertBefore(div, firstChild);
                    let hr = document.createElement('hr');
                    div.insertAdjacentElement('afterend', hr);
                } else {
                    $(`#product-detail-${data.id_hdct}`).find('.quantity-product').text('Số Lượng :' + data.soLuong)
                }
                $('#total-money').text(addCommasToNumber(data.tongTien) + 'đ')
                $('#discount-money').text(addCommasToNumber(data.giamGia == null ? 0 : data.giamGia) + 'đ')
                $('#payment-money').text(addCommasToNumber(data.thucThu) + 'đ')
                ToastSuccess('Chọn Thành Công.')
            }, error: function (e, x, h) {
                switch (e.getResponseHeader('status')) {
                    case 'HoaDonNull':
                        ToastError('Hóa đơn không tồn tại.');
                        break;
                    case 'MaxQuantity':
                        ToastError('Số lượng sản phẩm lớn hơn số lượng tồn.');
                        break;
                    case 'NotAuth':
                        ToastError('Vui lòng đăng nhập.');
                        break;
                    case 'MinQuantity':
                        ToastError('Số lượng sản phẩm phải lớn hơn 0.');
                        break;
                    case 'CTSPNull':
                        ToastError('Sản phẩm không tồn tại.');
                        break;
                    default:
                        ToastError('Lỗi.');
                }
                console.log(x)
                console.log(h)
            }
        })
        console.log(mauSac, kicCo)
    })

    $('#select_product').on('show.bs.modal', function (e) {
        let dataS = dataShop;
        let element = $('#tenSanPham');
        let mauSac = $('#mauSac');
        let kichCo = $('#kichCo');
        $('#oder-id-update').val($('#btn-show-update').data('id'));
        element.empty();
        kichCo.empty();
        mauSac.html(`<option value="#" selected>Chọn Màu</option>`)
        let arr = [];
        if (dataS !== null && Array.isArray(dataS)) {
            dataS.forEach((item, index) => {
                element.append(`<option value="${item.id}" ${index === 0 ? 'selected' : ''}>${item.ten}</option>`)
                if (index === 0) {
                    item.chiTietSanPham.forEach((chil) => {
                        if (!arr.includes(chil.mauSac)) {
                            mauSac.append(`<option value="${chil.mauSac}">${chil.tenMau}</option>`)
                            arr.push(chil.mauSac);
                        }
                    })
                }
            })
            $(element).niceSelect('update');
            $(mauSac).niceSelect('update');
            $(kichCo).niceSelect('update');
        }
        $('#soLuong').val(1);
        $('#preview-img').attr('src', dataS[0].chiTietSanPham[0].anh);
        // let ms = mauSac.val();
        // let kc = kichCo.find('option:selected').text();

    });
})

function formatServerTime(serverTimeString) {
    var serverTime = new Date(serverTimeString);
    var day = serverTime.getDate();
    day = (day < 10) ? '0' + day : day;
    var month = serverTime.getMonth() + 1;
    month = (month < 10) ? '0' + month : month;
    var year = serverTime.getFullYear();
    var hours = serverTime.getHours();
    hours = (hours < 10) ? '0' + hours : hours;
    var minutes = serverTime.getMinutes();
    minutes = (minutes < 10) ? '0' + minutes : minutes;
    var seconds = serverTime.getSeconds();
    seconds = (seconds < 10) ? '0' + seconds : seconds;
    var formattedDateTime = day + '/' + month + '/' + year + ' ' + hours + ':' + minutes + ':' + seconds;
    return formattedDateTime;
}
