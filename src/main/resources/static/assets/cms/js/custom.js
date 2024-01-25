
function Toast(status, message) {
    $('#systoast').toast({delay: 5000, autohide: true, animation: true,});
    $('#systoast').toast('show');
    $('#system-toast-mesage').text(message);
    if (status == 'success') {
        $('#img-toast').attr('src', '/assets/cms/img/icon/success.svg')
        $('#toast-status').text("Thành Công !");
    } else if (status == 'error') {
        $('#img-toast').attr('src', '/assets/cms/img/icon/error.svg')
        $('#toast-status').text("Thất Bại !");
    } else {
        $('#toast-status').text("Sai Giá trị status !");
    }
}

function ToastSuccess(message) {
    Toast('success', message)
}

function ToastError(message) {
    Toast('error', message)
}

function setFormSendData(url, name, id) {
    $('#titleModalLabel').text("Thêm Mới " + name);
    $('#form-modal-add-attr-label').text("Tên " + name + ":");
    $('#url-post-data').val(url)
    $('#id-element-data').val(id)
    $('#form-add-product-attr').modal('show')
    $('#product-name-modal').val('');
}

$(document).on('ready', function () {
    $('#save-product-detail').on('click', function () {
        let sanPham = $('#sanPham').val();
        let theLoai = $('#theLoai').val();
        let thuongHieu = $('#thuongHieu').val();
        let chatLieu = $('#chatLieu').val();
        let deGiay = $('#deGiay').val();
        let coGiay = $('#coGiay').val();
        let muiGiay = $('#muiGiay').val();
        let giaNhap = $('#giaNhap').val();
        let giaGoc = $('#giaGoc').val();
        let sales = $('#sales').is(":checked");
        let trangThai = $('#status').is(":checked");
        let mota = $('.ql-editor').html();
        let product_details = [];
        $('.row-data-detail').each(function () {
            let inputs = $(this).find('.form-control');
            let my_obj = {};
            let img = $(this).find('.avatar')[0];
            inputs.each(function () {
                let name = $(this).attr('name');
                let value = $(this).val();
                my_obj[name] = value;
                my_obj['img'] = $(img).attr('src');
            });
            product_details.push(my_obj);

        });
        if (isEmpty(sanPham)) {
            ToastError("Vui lòng chọn Sản Phẩm !")
            $('#sanPham').focus();
            return;
        }
        if (isEmpty(theLoai)) {
            ToastError("Vui lòng chọn Thể Loại !")
            $('#theLoai').focus();
            return;
        }
        if (isEmpty(thuongHieu)) {
            ToastError("Vui lòng chọn Thể Loại !")
            $('#thuongHieu').focus();
            return;
        }
        if (isEmpty(chatLieu)) {
            ToastError("Vui lòng chọn Chất Liệu !")
            $('#chatLieu').focus();
            return;
        }
        if (isEmpty(deGiay)) {
            ToastError("Vui lòng chọn Đế Giày !")
            $('#deGiay').focus();
            return;
        }
        if (isEmpty(coGiay)) {
            ToastError("Vui lòng chọn Cổ Giày !")
            $('#coGiay').focus();
            return;
        }
        if (isEmpty(muiGiay)) {
            ToastError("Vui lòng nhập Mũi Giày !")
            $('#muiGiay').focus();
            return;
        }
        if (giaNhap == 'đ' || isEmpty(giaNhap) || convertToNumber($('#giaNhap').val()) < 0) {
            ToastError("Vui lòng nhập Giá Nhập !")
            $('#giaNhap').focus();
            return;
        }
        if (giaGoc == 'đ' || isEmpty(giaGoc) || convertToNumber($('#giaGoc').val()) < 0) {
            ToastError("Vui lòng nhập Giá Gốc !")
            $('#giaGoc').focus();
            return;
        }
        if ($('#giaBan').val() == 'đ' || isEmpty($('#giaBan').val()) || convertToNumber($('#giaBan').val()) < 0) {
            ToastError("Vui lòng nhập Giá Bán !")
            $('#giaBan').focus();
            return;
        }
        if (Number($('#soLuong').val()) <= 0) {
            ToastError("Vui lòng nhập Số Lượng !")
            console.log($('#soLuong').val())
            $('#soLuong').focus();
            return;
        }
        if (isEmpty(mota) || mota == '<p><br></p>') {
            ToastError("Vui lòng nhập Giới Thiệu Sản Phẩm !")
            return;
        }
        if (product_details.length === 0) {
            ToastError('Vui lòng chọn các option sản phẩm');
            return;
        }
        let message = '';
        let check = true;
        for (let i = 0; i < product_details.length; i++) {
            if (product_details[i].img == '/assets/cms/img/400x400/img2.jpg') {
                product_details[i].img = null;
                message = "Vui lòng chọn ảnh !";
                check = false;
                break;
            }
            if (isEmpty(product_details[i].kichCo)) {
                message = "Vui lòng nhập Size !";
                check = false;
                break;
            }
            if (isEmpty(product_details[i].mauSac)) {
                message = "Vui lòng chọn Màu Sắc !";
                check = false;
                break;
            }
            if (convertToNumber(product_details[i].giaBan) < 0) {
                message = "Vui lòng nhập Giá Bán !";
                check = false;
                break;
            }
            if (isEmpty(product_details[i].soLuong) || Number(product_details[i].soLuong) < 0) {
                message = "Vui lòng Số Lượng !";
                check = false;
                break;
            }
        }
        if (check) {
            // post data lên thôi
            $.ajax({
                type: "POST",
                url: "/api/chi-tiet-san-pham",
                data: {
                    sanPham: sanPham,
                    theLoai: theLoai,
                    thuongHieu: thuongHieu,
                    chatLieu: chatLieu,
                    deGiay: deGiay,
                    coGiay: coGiay,
                    moTa: mota,
                    muiGiay: muiGiay,
                    giaNhap: giaNhap,
                    giaGoc: giaGoc,
                    sales: sales,
                    trangThai: trangThai,
                    product_details: JSON.stringify(product_details)
                }, success: (data, status, xhr) => {
                    ToastSuccess('OKE')
                    let files = JSON.parse(sessionStorage.getItem('fileImg'));
                    product_details.forEach((pro) => {
                        console.log("pro")
                        console.log(pro)
                        if (pro.img.length > 0) {
                            files.forEach((storage) => {
                                console.log("storage")
                                console.log(storage.getName())
                                if (pro.img == storage.url) {
                                    console.log("Bằng")
                                }
                            })
                        }
                    })
                }, error: (e) => {
                    ToastError(e.getResponseHeader('error'));
                }
            })
        } else (
            ToastError(message)
        )
    })
    //generate version product
    $('#mauSac, #kichCo').on('change', function () {
        let mausac = $('#mauSac').val();
        let giaban = $('#giaBan').val();
        let kichco = $('#kichCo').val();
        let soLuong = $('#soLuong').val();
        let html = '';
        mausac.forEach((mau, index1) => {
            html += `<tr><th >${mau}</th></tr>`
            kichco.forEach((co, index2) => {
                let size = kichco.length;
                let img = `<th rowspan="${size}">
                                    <div class="spinner-border text-primary d-none" role="status">
                                      <span class="sr-only">Loading...</span>
                                    </div>
                                    <label for="fileimgselected${index1 + '' + index2}">
                                         <img class="avatar" src="/assets/cms/img/400x400/img2.jpg" alt="Image Description">
                                    </label>
                                         <i class="tio-delete btn-del-img" ></i>
                                    <input class="formAddImg form-control"  type="file" name="img" id="fileimgselected${index1 + '' + index2}" hidden="">
                                  </th>`;
                html += `
                <tr class="row-data-detail">
                    <td class="table-column-pr-0">
                        <div class="custom-control custom-checkbox">
                                <input type="text" class="form-control" name="id" value="0" hidden="">
                                <input type="checkbox" class="custom-control-input"
                                       id="productVariationsCheck${index1 + '' + index2}">
                                <label class="custom-control-label" for="productVariationsCheck${index1 + '' + index2}"></label>
                            </div>
                        </td>
                        ${index2 == 0 ? img : ''}
                        <th class="table-column-pl-0">
                            <input type="text" class="form-control" name="kichCo" value="${co}">
                        </th>
                        <th class="table-column-pl-0">
                            <label class="form-control-label" style="background-color: ${mau}"></label>
                            <input type="text" class="form-control" name="mauSac" value="${mau}" hidden="">
                        </th>
                        <th class="table-column-pl-0">
                             <input type="text" class="form-control" name="giaBan" value="${giaban}">
                        </th>
                        <th class="table-column-pl-0">
                            <!-- Quantity Counter -->
                            <div class="js-quantity-counter input-group-quantity-counter">
                                <input type="number" name="soLuong"
                                       class="js-result form-control input-group-quantity-counter-control"
                                       value="${soLuong}">

                                <div class="input-group-quantity-counter-toggle">
                                    <a class="js-minus input-group-quantity-counter-btn"
                                       href="javascript:;">
                                        <i class="tio-remove"></i>
                                    </a>
                                    <a class="js-plus input-group-quantity-counter-btn" href="javascript:;">
                                        <i class="tio-add"></i>
                                    </a>
                                </div>
                            </div>
                            <!-- End Quantity Counter -->
                        </th>
                        <th class="table-column-pl-0">
                            <div class="btn-group" role="group" aria-label="Edit group">
                                <a class="btn btn-white" href="#">
                                    <i class="tio-edit"></i> Edit
                                </a>
                               <a class="btn btn-white" href="#">
                                    <i class="tio-delete-outlined"></i>
                                </a>
                            </div>
                        </th>
                    </tr>
                `;
            })
        })
        $('#addVariantsContainer').empty().append(html);
    });


    $('#btn-add-new-product').on('click', () => {
        let ten = $('#product-name-modal').val();
        let url = $('#url-post-data').val();
        if (ten.length === 0) {
            Toast('error', 'Vui lòng nhập tên !');
        } else {
            $.ajax({
                type: "POST",
                url: "/api/them-" + url,
                data: {
                    ten: ten
                },
                success: (data, status, xhr) => {
                    let id = $('#id-element-data').val();
                    let html = `<option value="${data.id}" selected>${data.ten}</option>`;
                    if (id == 'kichCo') {
                        html = `<option value="${data.ten}" selected>${data.ten}</option>`;
                    }
                    $('#' + id).append(html);
                    Toast('success', 'Thêm Thành Công !');
                    $('#product-name-modal').val('');
                },
                error: (xhr) => {
                    let mess = xhr.getResponseHeader('status');
                    if (mess == "existsByTen") {

                    }
                    switch (mess) {
                        case "existsByTen":
                            ToastError('Tên Đã Tồn Tại.');
                            break;
                        case "nameNull":
                            ToastError("Tên không được để trống.");
                            break;
                        case "nameSize":
                            ToastError("Tên kích cỡ phải từ 30 đến 50.");
                            break;
                        default:
                            ToastError('Lỗi hệ thống ! Vui lòng thử lại sau.')
                    }
                }
            })
        }
    })

    // thêm màu sắc
    $('#btn-add-new-color').on('click', () => {
        let coloCode = $('#colorChoice').val();
        let coloName = $('#color-name').val();
        if (coloName.length === 0) {
            Toast('error', 'Vui lòng nhập tên !');
        } else {
            $.ajax({
                type: "POST",
                url: "/api/them-mau-sac",
                data: {
                    ten_mau: coloName,
                    ma_mau: coloCode.toUpperCase()
                },
                success: (data, status, xhr) => {
                        let html = `<option value="${data.maMauSac}" data-name="${data.ten}">${data.maMauSac}</option>`;
                        $('#mauSac').append(html);
                        Toast('success', 'Thêm Thành Công !');
                        if (confirm('Bạn có muốn tạo thêm !')) {
                            $('#color-name').val('');
                        } else {
                            $('#form-add-color').modal('hide');
                            $('#color-name').val('');
                        }
                },
                error: (xhr) => {
                    let mes = xhr.getResponseHeader('status');
                    switch (mes){
                        case "existsByMa":ToastError('Mã màu đã tồn tại.')
                            break;
                        case "existsByTen":ToastError('Tên màu đã tồn tại.')
                            break;
                        default:ToastError('Lỗi hệ thống ! Vui lòng thử lại sau.')
                    }
                }
            })
        }
    })


})

function checkData() {

}

showColorCode('#000000')

function showColorCode(color) {
    let colorCode = document.getElementById("colorCode");
    colorCode.value = color.toUpperCase();
}


function isEmpty(str) {
    return (!str || str.length === 0);
}

function convertToNumber(text) {
    text = text.replace(/[.,]/g, "");
    text = text.replace(/[a-zA-Z]/g, "");
    var number = Number(text);
    return number;
}
