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
        var sanPham = $('#sanPham').val();
        var theLoai = $('#theLoai').val();
        var thuongHieu = $('#thuongHieu').val();
        var chatLieu = $('#chatLieu').val();
        var deGiay = $('#deGiay').val();
        var coGiay = $('#coGiay').val();
        var muiGiay = $('#muiGiay').val();
        var giaNhap = $('#giaNhap').val();
        var giaGoc = $('#giaGoc').val();
        var sales = $('#sales').val();
        var trangThai = $('#status').val();

        var  imgSelected = function () {
            var img = [];
            $('.product-img-selected').each(function () {
                var src = $(this).attr('src');
                img.push(src);
            })
            return img;
        };
        var product_details = [];
        $('.row-data-detail').each(function (){
            var inputs = $(this).find('.form-control');
            var my_obj = {};
            inputs.each(function(){
                var name = $(this).attr('name');
                var value = $(this).val();
                my_obj[name] = value;
            });
            product_details.push(my_obj);
        });

        // post data lên thôi
        $.ajax({
            type:"POST",
            url:"/api/chi-tiet-san-pham",
            data:{
                imgSelected:JSON.stringify(imgSelected()),
                sanPham:sanPham,
                theLoai:theLoai,
                thuongHieu:thuongHieu,
                chatLieu:chatLieu,
                deGiay:deGiay,
                coGiay:coGiay,
                muiGiay:muiGiay,
                giaNhap:giaNhap,
                giaGoc:giaGoc,
                sales:sales,
                trangThai:trangThai,
                product_details:JSON.stringify(product_details)
            },success:()=>{
                alert("Thành Công !");
            },error:()=>{
                alert("Lỗi!")
            }
        })

    })
    //generate version product
    $('#mauSac, #kichCo').on('change', function () {
        var mausac = $('#mauSac').val();
        var giaban = $('#giaBan').val();
        var kichco = $('#kichCo').val();
        var html = '';
        mausac.forEach((mau) => {
            html += `<tr><th>${mau}</th></tr>`
            kichco.forEach((co) => {
                html += `
                <tr class="row-data-detail">
                    <td class="table-column-pr-0">
                        <div class="custom-control custom-checkbox">
                                <input type="checkbox" class="custom-control-input"
                                       id="productVariationsCheck4">
                                <label class="custom-control-label" for="productVariationsCheck4"></label>
                            </div>
                        </td>
                        <th>
                            <img class="avatar" src="/assets/cms/img/400x400/img2.jpg" alt="Image Description">
                        </th>
                        <th class="table-column-pl-0">
                            <input type="text" class="form-control" name="kichCo" value="${co}">
                        </th>
                        <th class="table-column-pl-0">
                            <input type="text" class="form-control" name="mauSac" value="${mau}" style="background-color: ${mau}">
                        </th>
                        <th class="table-column-pl-0">
                             <input type="text" class="form-control" name="giaBan" value="${giaban}">
                        </th>
                        <th class="table-column-pl-0">
                            <!-- Quantity Counter -->
                            <div class="js-quantity-counter input-group-quantity-counter">
                                <input type="number" name="soLuong"
                                       class="js-result form-control input-group-quantity-counter-control"
                                       value="1">

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
        var ten = $('#product-name-modal').val();
        var url = $('#url-post-data').val();
        if (ten.length === 0) {
            Toast('error','Vui lòng nhập tên !');
        } else {
            $.ajax({
                type: "POST",
                url: "/api/them-" + url,
                data: {
                    ten: ten
                },
                success: (data, status, xhr) => {
                    var id = $('#id-element-data').val();
                    var html = `<option value="${data.id}" selected>${data.ten === undefined ? data.tenTheLoai : data.ten}</option>`;
                    $('#' + id).append(html);
                    Toast('success','Thêm Thành Công !');
                    // $('#add_new_product_modal').modal('hide');
                    $('#product-name-modal').val('');
                },
                error: () => {
                    alert("lỗi !")
                }
            })
        }
    })

    // thêm màu sắc
    $('#btn-add-new-color').on('click', () => {
        var coloCode = $('#colorChoice').val();
        var coloName = $('#color-name').val();
        if (coloName.length === 0) {
            alert('Vui lòng nhập tên màu !')
        } else {
            $.ajax({
                type: "POST",
                url: "/api/them-mau-sac",
                data: {
                    ten_mau: coloName,
                    ma_mau: coloCode.toUpperCase()
                },
                success: (data, status, xhr) => {
                    if (data == "") {
                        alert("Mã màu đã tồn tại.")
                    } else {
                        var html = `<option value="${data.maMauSac}" data-name="${data.ten}" selected>${data.maMauSac}</option>`;
                        $('#mauSac').append(html);
                        alert('Thêm Thành Công !');
                        if (confirm('Bạn có muốn tạo thêm !')) {
                            $('#color-name').val('');
                        } else {
                            $('#form-add-color').modal('hide');
                            $('#color-name').val('');
                        }
                    }
                },
                error: () => {
                    alert("lỗi !")
                }
            })
        }
    })


})
showColorCode('#000000')

function showColorCode(color) {
    var colorCode = document.getElementById("colorCode");
    colorCode.value = color.toUpperCase();
}
function Toast(status,message) {
    $('#systoast').toast({delay: 5000,autohide:true,animation:true,progress:true,multiple:true});
    $('#systoast').toast('show');
    $('#system-toast-mesage').text(message);
    if (status=='success'){
        $('#toast-status').text("Thành Công !");
    }else if (status=='error'){
        $('#toast-status').text("Thất Bại !");
    }else{
    $('#toast-status').text("Sai Giá trị status !");
    }
}
