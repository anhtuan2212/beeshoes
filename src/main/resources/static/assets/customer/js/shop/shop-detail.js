
$(document).ready(function () {
    $('.select_color').on('change', function () {
        $('.select_color').closest('.label_select_color').removeClass('activeSelected')
        $(this).closest('.label_select_color').addClass('activeSelected');
    })
})

function addItemToCart(product, quantity, totalMoney) {
    console.log(product)
    let element = $('#list_product_items_cart').find(`div.cart_product_item[data-id-product="${product.id}"]`);
    if (element.length === 1) {
        let num = element.find(`#quantyti_${product.id}`).text().replace('SL:', '')
        let allNum = Number(num)+Number(quantity);
        console.log(allNum)
        element.find(`#quantyti_${product.id}`).text('SL:'+allNum)
    } else {
        let html = `
    <div class="cart_product_item row" data-id-product="${product.id}">
        <div class="thumb col-3">
            <img src="${product.product_img}" alt="product img">
        </div>
        <div class="cart_content col-9 ">
            <h5 class="title col-12 w_100">${product.name}</h5>
            <div class="product_quantity col-12 w_100 mt-1 " >
                <label class="mb-0" id="quantyti_${product.id}" >SL:${quantity}</label>
            </div>
            <div class="col-12 height-mid">
                <label class="small mb-0">MS:${product.color_name}</label>
                <label class="small mb-0">KT:${product.size}</label>
            </div>
            <h6 class="product_price col-sm-12 w_100">${product.gia_ban}đ</h6>
            <a class="cart_trash" data-id-delete="${product.id}"><i class="fa fa-trash"></i></a>
        </div>
    </div>
    `;
        $('#list_product_items_cart').append(html);
    }
    $('#total-money-cart').text(totalMoney);
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

function extractNumberFromString(str) {
    let numberStr = str.replace(/\D/g, '');
    let number = parseInt(numberStr);
    return number;
}

function addCommasToNumber(number) {
    let numberStr = number.toString().replace(/[^\d]/g, '');
    let parts = [];
    for (let i = numberStr.length, j = 0; i >= 0; i--, j++) {
        parts.unshift(numberStr[i]);
        if (j > 0 && j % 3 === 0 && i > 0) {
            console.log(j)
            parts.unshift('.');
        }
    }
    return parts.join('');
}

$(document).ready(function () {
    let data = [];
    let datas = JSON.parse(localStorage.getItem('shopping_carts'));
    let data_cart = [];
    if (datas !== null){
        data_cart = datas;
    }

    function addDataCart(data) {
        if (data_cart.length > 0) {
            let found = false;
            for (let i = 0; i < data_cart.length; i++) {
                if (data_cart[i].pro.id === data.pro.id) {
                    data_cart[i].quantity = Number(data_cart[i].quantity) + Number(data.quantity);
                    found = true;
                    break;
                }
            }

            if (!found) {
                data_cart.push(data);
            }
        } else {
            data_cart.push(data);
        }
    }


    $('#table-data tbody tr').each(function () {
        var obj = {};
        $(this).find('td').each(function () {
            let propertyName = $(this).data('name');
            let propertyValue = $(this).text();
            obj[propertyName] = propertyValue;
        });
        data.push(obj);
    });
    console.log(data)
    $('input[name="kichthuoc"], input[name="color"]').on('change', function () {
        var kichThuoc = $('input[name="kichthuoc"]:checked').val();
        var color = $('input[name="color"]:checked').val();
        console.log("kích thước :" + kichThuoc)
        console.log("color :" + color)
        if (color === undefined || kichThuoc === undefined) {
            return;
        }
        for (let i = 0; i < data.length; i++) {
            let product = data[i];
            if (product.size == kichThuoc && product.color_code == color) {
                console.log(product);
                $('#show-price').html(`${product.gia_ban}đ <span>${product.gia_goc}đ</span>`)
                $('#product-sku').html(`<span>SKU:</span> ${product.detail_code}`);
                $('#total-num-product').html(`Số lượng mẫu : ${product.so_luong_ton}`);
            }

        }
    });
    $('#add-to-cart').on('click', function () {
        let quantity = $('#quantity-selected').val();
        if (quantity < 1) {
            ToastError('Vui lòng nhập số lượng !');
            $('#quantity-selected').focus();
            return;
        }
        let kichThuoc = $('input[name="kichthuoc"]:checked').val();
        let color = $('input[name="color"]:checked').val();
        if (kichThuoc === undefined || color === undefined) {
            ToastError('Vui lòng chọn Màu Sắc và Kích Cỡ.');
            return;
        }
        for (let i = 0; i < data.length; i++) {
            let product = data[i];
            if (product.size == kichThuoc && product.color_code == color){
                addDataCart({pro: product, quantity: quantity})
                let total = 0;
                data_cart.forEach((data) => {
                    total += extractNumberFromString(data.pro.gia_ban) * data.quantity;
                    console.log(total)
                })
                total = addCommasToNumber(total);
                console.log(product)
                console.log('--------------------------')
                console.log(data_cart)
                ToastSuccess('Thêm thành công.')
                addItemToCart(product, quantity, total + 'đ')
                localStorage.setItem('shopping_carts',JSON.stringify(data_cart));
                $('#quantity__item_carts').text(data_cart.length);
                return;
            }
        }
    })

});