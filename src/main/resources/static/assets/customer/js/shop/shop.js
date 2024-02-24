var dataShop = null;
window.onload = function () {
    // ToastSuccess('Tải hoàn tất !')
    $.ajax({
        url: '/api/get-all-san-pham',
        type: 'GET',
        success: function (data) {
            dataShop = data;
            console.log(dataShop)
            printAllData()
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("Lỗi khi gọi API: " + textStatus, errorThrown);
        }
    })
};

function printAllData() {
    let arr = [];
    $('.product__item').each((index, element) => {
        arr.push($(element).data('product-id'));
    })
    console.log(arr)
    dataShop.forEach((product) => {
        let bool = true;
        for (let i = 0; i < arr.length; i++) {
            if (product.id == arr[i]) {
                bool = false;
            }
        }
        if (bool) {
            let url = ''
            for (let i = 0; i < product.anh.length; i++) {
                if (product.anh[i].main) {
                    url = product.anh[i].url;
                    break;
                }
            }
            console.log(product)
            let min_price = 0;
            let max_price =0;
            for (let i = 0; i < product.chiTietSanPham.length; i++) {
                if (i===0){
                   min_price = product.chiTietSanPham[i].giaBan;
                   max_price = product.chiTietSanPham[i].giaGoc;
                }else{
                    if (min_price>product.chiTietSanPham[i].giaBan){
                        min_price = product.chiTietSanPham[i].giaBan;
                    }
                    if (max_price<product.chiTietSanPham[i].giaGoc){
                        max_price = product.chiTietSanPham[i].giaGoc;
                    }
                }
            }
            let color ='';
            for (let i = 0; i < product.maMauSac.length; i++) {
                color+=`<label style="background-color:${product.maMauSac[i]}">
                             <input type="radio" id="" value="1">
                        </label>`;
            }
            let size = '';
            for (let i = 0; i < product.kichCo.length; i++) {
                size +=`<label class="label_select_size" for="size_id_42_1">
                             ${product.kichCo[i].ten}
                           <input class="size_selected" name="kichthuoc1" type="radio" id="size_id_${product.kichCo[i].id+'_'+product.id}" value="33" data-id-size="4">
                        </label>`
            }
            let sales = '';
            if (product.sale){
                sales ='<span class="label">SALE</span>'
            }
            let html = `<div class="col-lg-4 col-md-6 col-sm-6 d-none">
                        <div class="product__item sale" data-product-id="1">
                                <div class="product__item__pic set-bg susor-pointer" data-setbg="" data-href="/shop-details?product=${product.id}" style="background-image: url("");">
                                    ${sales}
                                    <ul class="product__hover">
                                        <li><a href="javascript:;"><img src="/assets/customer/img/icon/heart.png" alt=""></a>
                                        </li>
                                        <li><a href="javascript:;"><img src="/assets/customer/img/icon/shopping-cart-add.svg" alt=""></a></li>
                                        <li><a href="/shop-detail?product=${product.id}"><img src="/assets/customer/img/icon/eye.svg" alt=""></a></li>
                                    </ul>
                                </div>
                            <div class="product__item__text">
                                <h6 class="product_name">${product.ten}</h6>
                                <a href="/shop-detail?product=${product.id}" class="add-cart">Xem Sản Phẩm</a>
                                    <div class="rating">
                                        <h6 class="giaGoc">${max_price}</h6>
                                    </div>
                                    <h5>${min_price}</h5>
                                <div class="product__option__size">
                                        ${size}
                                </div>
                                <div class="product__color__select">
                                ${color}
                                </div>
                            </div>
                        </div>
                    </div>`
            console.log(html)
            $('#list_product_items').append(html);
        }
    })
}

function Toast(status, message) {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        },
        customClass: {
            popup: 'custom-toast-class', // Thêm lớp CSS tùy chỉnh
        }
    });
    Toast.fire({
        icon: status,
        title: message
    });
}

function ToastSuccess(message) {
    Toast('success', message)
}

function ToastError(message) {
    Toast('error', message)
}

function showLoader() {
    $('#preloder').css('display', 'block').find('.loader').css('display', 'block');
}

function closeLoader() {
    $('#preloder').css('display', 'none').find('.loader').css('display', 'none');
}

let selected_color = '';
let selected_tags = '';
let selected_price = '';
let selected_brand = '';
let selected_size = '';
let selected_cate = '';
let fillter_price = '';
$(document).ready(function () {
    $('#selected_filter_price').on('change', function () {
        fillter_price = $(this).val()
        fillterdata()
    })
    $('.select_color').on('click', function () {
        if ($(this).hasClass('activeSelected')) {
            $(this).closest('.label_select_color').removeClass('activeSelected');
        } else {
            $('.select_color').closest('.label_select_color').removeClass('activeSelected');
            $(this).closest('.label_select_color').addClass('activeSelected');
        }
        selected_color = $(this).closest('.label_select_color').data('id')
        fillterdata()
    });

    $('.selected_tags').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $('.selected_tags').removeClass('active');
            $(this).addClass('active');
        }
        selected_tags = $(this).data('id')
        fillterdata()
    });
    $('.selected_price').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $('.selected_price').removeClass('active');
            $(this).addClass('active');
        }
        selected_price = $(this).data('id')
        fillterdata()
    });
    $('.selected_size').on('click', function () {
        selected_size = $(this).data('id')
        fillterdata()
    });
    $('.selected_brand').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $('.selected_brand').removeClass('active');
            $(this).addClass('active');
        }
        selected_brand = $(this).data('id')
        fillterdata()
    });
    $('.selected_cate').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
        } else {
            $('.selected_cate').removeClass('active');
            $(this).addClass('active');
        }
        selected_cate = $(this).data('id')
        fillterdata()
    });

    $('#btn-load-more-product').on('click', function () {
        showLoader()
        setTimeout(() => {
            closeLoader();
        }, 1000)
    })


    function fillterdata() {
        console.log('=======================')
        console.log(selected_color)
        console.log(selected_price)
        console.log(selected_cate)
        console.log(selected_size)
        console.log(selected_brand)
        console.log(selected_tags)
        console.log(fillter_price)

    }
})
