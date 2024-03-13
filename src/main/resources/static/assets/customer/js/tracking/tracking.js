$(document).ready(function () {
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
    $('#select_product').on('show.bs.modal', function (e) {
        let dataS = dataShop;
        let element = $('#tenSanPham');
        let mauSac = $('#mauSac');
        let kichCo = $('#kichCo');
        element.empty();
        let arr = [];
        if (dataS !== null && Array.isArray(dataS)) {
            dataS.forEach((item, index) => {
                element.append(`<option value="${item.id}" ${index === 0 ? 'selected' : ''}>${item.ten}</option>`)
                if (index === 0) {
                    item.chiTietSanPham.forEach((chil,ind) => {
                        if (ind===0){
                            item.kichCo.forEach((kc,i)=>{
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
            $(element).niceSelect('update');
            $(mauSac).niceSelect('update');
            $(kichCo).niceSelect('update');
        }
        $('#soLuong').val(1);
        $('#preview-img').attr('src',dataS[0].chiTietSanPham[0].anh);
        // let ms = mauSac.val();
        // let kc = kichCo.find('option:selected').text();

    });
})