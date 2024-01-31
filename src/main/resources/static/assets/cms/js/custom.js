
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
    //generate version product
    /*
    $('#mauSac, #kichCo').on('change', function () {
        let mausac = $('#mauSac').val();
        let giaban = $('#giaBan').val();
        let kichco = $('#kichCo').val();
        let soLuong = $('#soLuong').val();
        let html = '';
        mausac.forEach((mau, index1) => {
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
                        <th class="table-column-pl-0 width-100">
                            <input type="text" class="form-control" name="kichCo" value="${co}">
                        </th>
                        <th class="table-column-pl-0 width-100">
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
*/

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
    $('#mauSac').on('change', function () {
        var mausac = $('#mauSac').val();
        mausac.forEach((mau) => {
            var ele = $('li[title="' + mau + '"]');
            var tenmau = $('option[value="' + mau + '"]').attr('data-name');
            var span = $(ele[0]).find('span:not(.select2-selection__choice__remove)').eq(0);
            $(span[0]).text(tenmau);
            $(ele[0]).css('background-color', mau);
            $(ele[0]).css('color', '#FFFFFF');
        });
    });

    $(window).on('load', function () {
        var mausac = $('#mauSac').val();
        mausac.forEach((mau) => {
            var ele = $('li[title="' + mau + '"]');
            var tenmau = $('option[value="' + mau + '"]').attr('data-name');
            var span = $(ele[0]).find('span:not(.select2-selection__choice__remove)').eq(0);
            $(span[0]).text(tenmau);
            $(ele[0]).css('background-color', mau);
            $(ele[0]).css('color', '#FFFFFF');
        });
        if ($('#status-product').val() == 1) {
            ToastError('Sản Phẩm không tồn tại.')
        }
    });
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
