
$(document).ready(async function () {
    let NumDataLength = 0;
    if (username === undefined) {
        deleteCheckoutDatalocalStorage();
        printAllProductInLocal();
        NumDataLength = getProductInLocalStorage() !== null ? getProductInLocalStorage().length : 0;
    } else {
      await getAllShoppingCartServer()
            .then(function(data) {
                printAllProductInServer(data);
                NumDataLength = data.length;
                console.log(data);
            })
            .catch(function(error) {
                console.log('Có lỗi xảy ra khi lấy dữ liệu giỏ hàng từ máy chủ.');
                console.log(error);
            });
    }
    console.log(NumDataLength)
    let datatable = $('#datatable').DataTable({
        searching: false,
        lengthChange: false,
        info: false,
        pageLength: 6,
        paging: NumDataLength > 6,
        ordering: false,
        select: {
            style: 'multi', selector: '.selected_product',
            classMap: {
                checkAll: '#selected_all_product',
            }
        },
        pagingType: 'full_numbers',
        language: {
            paginate: {
                first: '<i class="fa fa-angle-double-left"></i>',
                previous: '<i class="fa fa-angle-left"></i>',
                next: '<i class="fa fa-angle-right"></i>',
                last: '<i class="fa fa-angle-double-right"></i>'
            },
            "sEmptyTable":'<div class=\"text-center p-4\"><img class=\"mb-3\" src=\"/assets/cms/svg/illustrations/sorry.svg\" alt=\"Image Description\" style=\"width: 7rem;\"><p class=\"mb-0\">Bạn chưa thêm sản phẩm vào giỏ hàng !</p></div>',
        }
    });
    $(document).on('change', '.selected_product', function () {
        let data = getProductInLocalStorage();
        if ($(this).is(':checked')) {
            if (Array.isArray(data)) {
                let id = $(this).data('id');
                let product = data.find(item => item.pro.id == id);
                let checkoutData = getCheckoutDataLocalStorage();
                if (checkoutData == null) {
                    checkoutData = [];
                }
                checkoutData.push(product)
                saveCheckoutDatalocalStorage(checkoutData)
                checkoutData = getCheckoutDataLocalStorage();
                let totalMoney = 0;
                checkoutData.forEach((datac) => {
                    totalMoney += extractNumberFromString(datac.pro.gia_ban) * Number(datac.quantity);
                })
                $('#sub-total').text(addCommasToNumber(totalMoney) + 'đ');
                $('#total-money').text(addCommasToNumber(totalMoney) + 'đ');
            }
        } else {
            let id = $(this).data('id');
            let data = getCheckoutDataLocalStorage();
            if (Array.isArray(data)) {
                let ind = null;
                let totalMoney = 0;
                for (let i = 0; i < data.length; i++) {
                    if (data[i].pro.id == id) {
                        ind = i;
                    } else {
                        totalMoney += extractNumberFromString(data[i].pro.gia_ban) * Number(data[i].quantity);
                    }
                }
                data.splice(ind, 1);
                saveCheckoutDatalocalStorage(data);
                $('#sub-total').text(addCommasToNumber(totalMoney) + 'đ');
                $('#total-money').text(addCommasToNumber(totalMoney) + 'đ');
            }
        }
    });
    function getCheckoutDataLocalStorage() {
        return JSON.parse(localStorage.getItem('checkout_data'));
    }

    function deleteCheckoutDatalocalStorage() {
        localStorage.removeItem('checkout_data');
    }

    function saveCheckoutDatalocalStorage(data) {
        localStorage.setItem('checkout_data', JSON.stringify(data));
    }

    function getAllShoppingCartServer() {
        return new Promise(function(resolve, reject) {
            $.ajax({
                url: '/api/get-product-in-cart',
                type: 'GET',
                success: function (data) {
                    resolve(data); // Trả về dữ liệu khi thành công
                },
                error: function (xhr) {
                    reject(xhr); // Trả về lỗi khi có lỗi xảy ra
                }
            });
        });
    }
    function printAllProductInServer(data) {
        if (Array.isArray(data)) {
            console.log(data)
            let html = '';
            let totalMoney = 0;
            data.forEach((product) => {
                html += `
                        <tr>
                            <td>
                                <div class="checkbox-wrapper-30 mr-2">
                                   <span class="checkbox">
                                     <input class="selected_product" type="checkbox" id="product_${product.id}" data-id="${product.id}"/>
                                     <svg>
                                       <use xlink:href="#checkbox-30" class="checkbox"></use>
                                     </svg>
                                   </span>
                                     <svg xmlns="http://www.w3.org/2000/svg" style="display:none">
                                         <symbol id="checkbox-30" viewBox="0 0 22 22">
                                             <path fill="none" stroke="currentColor"
                                                   d="M5.5,11.3L9,14.8L20.2,3.3l0,0c-0.5-1-1.5-1.8-2.7-1.8h-13c-1.7,0-3,1.3-3,3v13c0,1.7,1.3,3,3,3h13 c1.7,0,3-1.3,3-3v-13c0-0.4-0.1-0.8-0.3-1.2"/>
                                         </symbol>
                                     </svg>
                                </div>
                            </td>
                            <td class="product__cart__item">
                                <div class="product__cart__item__pic">
                                    <img width="90" height="90" src="${product.chitietSanPham.anh}" alt="product_img">
                                </div>
                                <div class="product__cart__item__text">
                                    <h6>${product.chitietSanPham.ten}</h6>
                                    <h5>${addCommasToNumber(product.chitietSanPham.giaBan)}đ</h5>
                                </div>
                            </td>
                            <td class="quantity__item">
                                <div class="quantity">
                                    <div class="pro-qty-2"><span class="fa fa-angle-left dec qtybtn"></span>
                                        <input type="text" value="${product.soLuong}">
                                    <span class="fa fa-angle-right inc qtybtn"></span></div>
                                </div>
                            </td>
                            <td class="cart__price">${addCommasToNumber(product.chitietSanPham.giaBan * Number(product.soLuong))}đ</td>
                            <td class="cart__close"><i class="fa fa-close"></i></td>
                        </tr>
                        `;
            })
            $('#sub-total').text('0đ');
            $('#total-money').text('0đ');
            $('#show-data').empty().html(html);
        }
    }


    function printAllProductInLocal() {
        let data = getProductInLocalStorage();
        if (Array.isArray(data)) {
            let html = '';
            let totalMoney = 0;
            data.forEach((product) => {
                html += `
                <tr>
                    <td>
                        <div class="checkbox-wrapper-30 mr-2">
                           <span class="checkbox">
                             <input class="selected_product" type="checkbox" id="product_${product.pro.id}" data-id="${product.pro.id}"/>
                             <svg>
                               <use xlink:href="#checkbox-30" class="checkbox"></use>
                             </svg>
                           </span>
                             <svg xmlns="http://www.w3.org/2000/svg" style="display:none">
                                 <symbol id="checkbox-30" viewBox="0 0 22 22">
                                     <path fill="none" stroke="currentColor"
                                           d="M5.5,11.3L9,14.8L20.2,3.3l0,0c-0.5-1-1.5-1.8-2.7-1.8h-13c-1.7,0-3,1.3-3,3v13c0,1.7,1.3,3,3,3h13 c1.7,0,3-1.3,3-3v-13c0-0.4-0.1-0.8-0.3-1.2"/>
                                 </symbol>
                             </svg>
                        </div>
                    </td>
                    <td class="product__cart__item">
                        <div class="product__cart__item__pic">
                            <img width="90" height="90" src="${product.pro.product_img}" alt="product_img">
                        </div>
                        <div class="product__cart__item__text">
                            <h6>${product.pro.name}</h6>
                            <h5>${product.pro.gia_ban}đ</h5>
                        </div>
                    </td>
                    <td class="quantity__item">
                        <div class="quantity">
                            <div class="pro-qty-2"><span class="fa fa-angle-left dec qtybtn"></span>
                                <input type="text" value="${product.quantity}">
                            <span class="fa fa-angle-right inc qtybtn"></span></div>
                        </div>
                    </td>
                    <td class="cart__price">${addCommasToNumber(extractNumberFromString(product.pro.gia_ban) * Number(product.quantity))}đ</td>
                    <td class="cart__close"><i class="fa fa-close"></i></td>
                </tr>
                `;
            })
            $('#sub-total').text('0đ');
            $('#total-money').text('0đ');
            $('#show-data').empty().html(html);
        }
    }
})

