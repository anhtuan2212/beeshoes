function Toast(status,message) {
    $('#systoast').toast({delay: 5000,autohide:true,animation:true,progress:true,hideAfter: 5000});
    $('#systoast').toast('show');
    $('#system-toast-mesage').text(message);
    if (status=='success'){
        $('#img-toast').attr('src','/assets/cms/img/icon/success.svg')
        $('#toast-status').text("Thành Công !");
    }else if (status=='error'){
        $('#img-toast').attr('src','/assets/cms/img/icon/error.svg')
        $('#toast-status').text("Thất Bại !");
    }else{
        $('#toast-status').text("Sai Giá trị status !");
    }
}
function ToastSuccess(message) {
    Toast('success',message)
}
function ToastError(message) {
    Toast('error',message)
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
        let  imgSelected = function () {
            let img = [];
            $('.product-img-selected').each(function () {
                let src = $(this).attr('src');
                img.push(src);
            })
            return img;
        };
        let product_details = [];
        $('.row-data-detail').each(function (){
            let inputs = $(this).find('.form-control');
            let my_obj = {};
            inputs.each(function(){
                let name = $(this).attr('name');
                let value = $(this).val();
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
                moTa:mota,
                muiGiay:muiGiay,
                giaNhap:giaNhap,
                giaGoc:giaGoc,
                sales:sales,
                trangThai:trangThai,
                product_details:JSON.stringify(product_details)
            },success:(data,status,xhr)=>{
                let mess =JSON.parse(xhr.getResponseHeader('mess'));
                let mes ='';
                for (let i= 0 ; i < mess.length ; i++){
                    if(i+1==mess.length){
                        mes+=mess[i];
                    }else{
                        mes+=mess[i]+',';
                    }
                }
                ToastError('Vui lòng nhập :'+mes+' !');
            },error:(e)=>{
                console.log(e)
                ToastError('Gửi Lỗi !');
                // Toast('error','Thêm Lỗi !');
            }
        })

    })
    //generate version product
    $('#mauSac, #kichCo').on('change', function () {
        let mausac = $('#mauSac').val();
        let giaban = $('#giaBan').val();
        let kichco = $('#kichCo').val();
        let soLuong = $('#soLuong').val();
        let html = '';
        mausac.forEach((mau,index1) => {
            html += `<tr><th>${mau}</th></tr>`
            kichco.forEach((co,index2) => {
                html += `
                <tr class="row-data-detail">
                    <td class="table-column-pr-0">
                        <div class="custom-control custom-checkbox">
                                <input type="text" class="form-control" name="id" value="" hidden="">
                                <input type="checkbox" class="custom-control-input"
                                       id="productVariationsCheck${index1+''+index2}">
                                <label class="custom-control-label" for="productVariationsCheck${index1+''+index2}"></label>
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
            Toast('error','Vui lòng nhập tên !');
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
                    $('#' + id).append(html);
                    Toast('success','Thêm Thành Công !');
                    // $('#add_new_product_modal').modal('hide');
                    $('#product-name-modal').val('');
                },
                error: () => {
                    Toast('error','Thêm Lỗi !');
                }
            })
        }
    })

    // thêm màu sắc
    $('#btn-add-new-color').on('click', () => {
        let coloCode = $('#colorChoice').val();
        let coloName = $('#color-name').val();
        if (coloName.length === 0) {
            Toast('error','Vui lòng nhập tên !');
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
                        Toast('error','Mã màu đã tồn tại !');
                    } else {
                        let html = `<option value="${data.maMauSac}" data-name="${data.ten}" selected>${data.maMauSac}</option>`;
                        $('#mauSac').append(html);
                        Toast('success','Thêm Thành Công !');
                        if (confirm('Bạn có muốn tạo thêm !')) {
                            $('#color-name').val('');
                        } else {
                            $('#form-add-color').modal('hide');
                            $('#color-name').val('');
                        }
                    }
                },
                error: () => {
                    Toast('error','Thêm Lỗi !');
                }
            })
        }
    })


})
showColorCode('#000000')

function showColorCode(color) {
    let colorCode = document.getElementById("colorCode");
    colorCode.value = color.toUpperCase();
}

