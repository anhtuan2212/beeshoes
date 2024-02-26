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

function addCommasToNumber(number) {
    let numberStr = number.toString().replace(/[^\d]/g, '');
    let parts = [];
    for (let i = numberStr.length, j = 0; i >= 0; i--, j++) {
        parts.unshift(numberStr[i]);
        if (j > 0 && j % 3 === 0 && i > 0) {
            parts.unshift('.');
        }
    }
    return parts.join('');
}

function printAllData() {
    let arr = [];
    $('.product__item').each((index, element) => {
        arr.push($(element).data('product-id'));
    })
    dataShop.forEach((product) => {
        let bool = true;
        for (let i = 0; i < arr.length; i++) {
            if (product.id == arr[i]) {
                bool = false;
            }
        }
        let min_price = 0;
        let max_price = 0;
        for (let i = 0; i < product.chiTietSanPham.length; i++) {
            if (i === 0) {
                min_price = product.chiTietSanPham[i].giaBan;
                max_price = product.chiTietSanPham[i].giaGoc;
            } else {
                if (min_price > product.chiTietSanPham[i].giaBan) {
                    min_price = product.chiTietSanPham[i].giaBan;
                }
                if (max_price < product.chiTietSanPham[i].giaGoc) {
                    max_price = product.chiTietSanPham[i].giaGoc;
                }
            }
        }
        product.max_price = max_price;
        product.min_price = min_price;
        if (bool) {
            let url = ''
            for (let i = 0; i < product.anh.length; i++) {
                if (product.anh[i].main) {
                    url = product.anh[i].url;
                    break;
                }
            }
            min_price = addCommasToNumber(min_price);
            max_price = addCommasToNumber(max_price);
            let color = '';
            for (let i = 0; i < product.maMauSac.length; i++) {
                color += `<label style="background-color:${product.maMauSac[i]}">
                             <input type="radio" id="" value="1">
                        </label>`;
            }
            let size = '';
            for (let i = 0; i < product.kichCo.length; i++) {
                size += `<label class="label_select_size mr-3p" for="size_id_${product.kichCo[i].id + '_' + product.id}">
                             ${product.kichCo[i].ten}
                           <input class="size_selected" name="kichthuoc1" type="radio" id="size_id_${product.kichCo[i].id + '_' + product.id}" value="${product.id}" data-id-size="${product.id}">
                        </label>`
            }
            let sales = '';
            if (product.sale) {
                sales = '<span class="label">SALE</span>'
            }
            let html = `<div class="col-lg-4 col-md-6 col-sm-6 product_wraper">
                        <div class="product__item sale" data-product-id="${product.id}">
                                <div class="product__item__pic set-bg susor-pointer" data-setbg="${url}" data-href="/shop-details?product=${product.id}" style="background-image: url('${url}');">
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
                                        <h6 class="giaGoc">${max_price}đ</h6>
                                    </div>
                                    <h5>${min_price}đ</h5>
                                <div class="product__option__size">
                                        ${size}
                                </div>
                                <div class="product__color__select">
                                ${color}
                                </div>
                            </div>
                        </div>
                    </div>`;
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
        fillterdata(dataShop)
    })
    $(document).on('click', '.select_color', function () {
        if ($(this).closest('.label_select_color').hasClass('activeSelected')) {
            $(this).closest('.label_select_color').removeClass('activeSelected');
            selected_color = '';
        } else {
            $('.select_color').closest('.label_select_color').removeClass('activeSelected');
            $(this).closest('.label_select_color').addClass('activeSelected');
            selected_color = $(this).closest('.label_select_color').data('id')
        }
        fillterdata(dataShop)
    });

    $('.selected_tags').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            selected_tags = '';
        } else {
            $('.selected_tags').removeClass('active');
            $(this).addClass('active');
            selected_tags = $(this).data('id')
        }
        fillterdata(dataShop)
    });
    $('.selected_price').on('click', function () {
        if ($(this).hasClass('active')) {
            selected_price = '';
            $(this).removeClass('active');
        } else {
            $('.selected_price').removeClass('active');
            $(this).addClass('active');
            selected_price = $(this).data('id')
        }
        fillterdata(dataShop)
    });

    $('.selected_size').on('click', function () {
        $('.selected_size').removeClass('active')
        $(this).addClass('active')
        selected_size = $(this).data('id')
        fillterdata(dataShop)
    });

    $('.selected_brand').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            selected_brand = '';
        } else {
            $('.selected_brand').removeClass('active');
            $(this).addClass('active');
            selected_brand = $(this).data('id')
        }
        fillterdata(dataShop)
    });
    $('.selected_cate').on('click', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            selected_cate = '';
        } else {
            $('.selected_cate').removeClass('active');
            $(this).addClass('active');
            selected_cate = $(this).data('id')
        }
        fillterdata(dataShop)
    });
    let arrEle =  $('#list_product_items').children();
    function fillterdata(originalData) {
        let data = originalData.slice(); // Sao chép dữ liệu gốc để thực hiện lọc

        let isSorted = false; // Biến kiểm tra xem đã sắp xếp dữ liệu hay chưa
        if (fillter_price === 'cao-den-thap') {
            data.sort((a, b) => b.min_price - a.min_price);
            isSorted = true; // Đã sắp xếp dữ liệu
        } else if (fillter_price === 'thap-den-cao') {
            data.sort((a, b) => a.min_price - b.min_price);
            isSorted = true; // Đã sắp xếp dữ liệu
        }
        // let arrays = arrEle.splice();
        // $('#list_product_items').empty();
        // if (isSorted) {
        //     data.forEach(item => {
        //         const $productWrapper = $(`.product__item[data-product-id="${item.id}"]`).closest('.product_wraper');
        //         $('#list_product_items').append($productWrapper);
        //     });
        // } else {
        //     // Nếu không sắp xếp, giữ nguyên thứ tự của các phần tử HTML
        //     $('.product_wraper').each(function() {
        //         $('#list_product_items').append($(this));
        //     });
        // }


        // Lặp qua từng sản phẩm để kiểm tra xem có phù hợp với tất cả các điều kiện lọc không
        data.forEach(item => {
            let shouldDisplay = true; // Giả sử sản phẩm phù hợp với tất cả các điều kiện lọc ban đầu

            // Kiểm tra các điều kiện lọc và cập nhật biến shouldDisplay nếu cần
            if (selected_color && !item.maMauSac.includes(selected_color)) {
                shouldDisplay = false;
            }
            if (selected_size && !item.kichCo.find(size => size.ten === `${selected_size}`)) {
                shouldDisplay = false;
            }
            if (selected_cate && item.theLoai !== selected_cate) {
                shouldDisplay = false;
            }
            if (selected_price) {
                let [from, to] = selected_price.split('->').map(parseFloat);
                if (item.min_price < from || item.min_price > to) {
                    shouldDisplay = false;
                }
            }
            if (selected_brand && item.thuongHieu !== selected_brand) {
                shouldDisplay = false;
            }
            if (selected_tags && !item.tags.includes(selected_tags)) {
                shouldDisplay = false;
            }

            // Hiển thị hoặc ẩn sản phẩm tùy thuộc vào biến shouldDisplay
            const $productWrapper = $(`.product__item[data-product-id="${item.id}"]`).closest('.product_wraper');
            if (shouldDisplay) {
                $productWrapper.removeClass('d-none');
            } else {
                $productWrapper.addClass('d-none');
            }
        });
        console.log('=======================')
        console.log(selected_color)
        console.log(selected_price)
        console.log(selected_cate)
        console.log(selected_size)
        console.log(selected_brand)
        console.log(selected_tags)
        console.log(fillter_price)
    }

    $('#btn-load-more-product').on('click', function () {
        showLoader()
        setTimeout(() => {
            closeLoader();
        }, 1000)
    })
// Hàm này cần được gọi lần đầu tiên để lưu trữ dữ liệu gốc
})
