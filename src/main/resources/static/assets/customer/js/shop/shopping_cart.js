setTabsHeader('shop');
let ShopingCart = [];
let ListVoucher = []
let SelectedVoucher = null;
localStorage.removeItem('checkout_data');
window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        location.reload()
    }
});
window.addEventListener('popstate', function(event) {
    location.reload()
});
$(document).ready(async function () {
    let NumDataLength = 0;
    if (username === undefined) {
        deleteCheckoutDatalocalStorage();
        printAllProductInLocal();
        NumDataLength = getProductInLocalStorage() !== null ? getProductInLocalStorage().length : 0;
    } else {
        await getAllShoppingCartServer()
            .then(function (data) {
                printAllProductInServer(data);
                NumDataLength = data.length;
                if (NumDataLength > 0 && Array.isArray(data)) {
                    data.forEach((product) => {
                        let oj = {
                            quantity: product.soLuong,
                            id_cart: product.id,
                            pro: {
                                chat_lieu: '',
                                co_giay: '',
                                color_code: '',
                                color_name: product.chitietSanPham.mauSac,
                                de_giay: '',
                                detail_code: product.chitietSanPham.maSanPham,
                                gia_ban: addCommasToNumber(product.chitietSanPham.giaBan),
                                gia_goc: addCommasToNumber(product.chitietSanPham.giaGoc),
                                id: product.chitietSanPham.id,
                                name: product.chitietSanPham.ten,
                                product_img: product.chitietSanPham.anh,
                                size: product.chitietSanPham.kichCo,
                                so_luong_ton: product.chitietSanPham.soLuongTon,
                            }
                        }
                        ShopingCart.push(oj);
                    })
                }
                console.log(ShopingCart);
            })
            .catch(function (error) {
                console.log('Có lỗi xảy ra khi lấy dữ liệu giỏ hàng từ máy chủ.');
                console.log(error);
            });
    }
    let datatable = $('#datatable').DataTable({
        searching: false,
        lengthChange: false,
        info: false,
        pageLength: 6,
        paging: NumDataLength > 6,
        ordering: false,
        pagingType: 'full_numbers',
        language: {
            paginate: {
                first: '<i class="fa fa-angle-double-left"></i>',
                previous: '<i class="fa fa-angle-left"></i>',
                next: '<i class="fa fa-angle-right"></i>',
                last: '<i class="fa fa-angle-double-right"></i>'
            },
            "sEmptyTable": '<div class="text-center p-4"><img class="mb-3" src="/assets/cms/svg/illustrations/sorry.svg" alt="Image Description" style="width: 7rem;"><p class="mb-0">Bạn chưa thêm sản phẩm vào giỏ hàng !</p></div>'
        }
    });
    $('#btn-add-code-voucher').on('click', function () {
        let data = ListVoucher;
        let products = getCheckoutDataLocalStorage();
        if (products === null) {
            ToastError('Vui lòng chọn sản phẩm.')
            return;
        }
        let value = $('#input_voucher').val();
        if (value.length === 0) {
            ToastError('Vui lòng nhập mã.')
            return;
        }
        if (data !== null && Array.isArray(data)) {
            let voucher = data.find(item => item.ma === value.toUpperCase());
            console.log(voucher)
            if (voucher === undefined) {
                ToastError('Mã không tồn tại.');
            } else {
                let totalMoney = 0;
                products.forEach((data) => {
                    totalMoney += extractNumberFromString(data.pro.gia_ban) * Number(data.quantity);
                });
                if (totalMoney >= voucher.giaTriToiThieu) {
                    SelectedVoucher = voucher;
                    $('.wraper_voucher').removeClass('active');
                    $(`#voucher_${voucher.id}`).addClass('active');
                    updateTotalMoney();
                    ToastSuccess('Áp dụng thành công.')
                } else {
                    ToastError('Mã không đủ điều kiện áp dụng.');
                }
            }
        }
    })

    $(document).on('click', '.dec, .inc', function () {
        let id = $(this).parent().data('id');
        // Lấy giá trị hiện tại của input
        var value = parseInt($(this).siblings('input').val());
        // Xác định hành động dựa trên class của nút được nhấn
        if ($(this).hasClass('dec')) {
            // Giảm giá trị đi 1 nếu giá trị hiện tại lớn hơn 1
            if (value > 1) {
                $(this).siblings('input').val(value - 1);
                addQuantityProduct(id, -1)
            }
            if (value === 1) {
                let element = $(this).closest('tr');
                deleteProductInCheckoutAndLocal(id, element)
            }
        } else if ($(this).hasClass('inc')) {
            // Tăng giá trị lên 1
            $(this).siblings('input').val(value + 1);
            addQuantityProduct(id, +1)
        }
        updateTotalMoney();
    });
    $(document).on('click', '.cart__close i', function () {
        let id = $(this).parent().data('id');
        deleteProductInCheckoutAndLocal(id, $(this.closest('tr')))
    })
    $(document).on('change', '#selected_all_product', function () {
        let data = [];
        if (username === undefined) {
            data = getProductInLocalStorage();
        } else {
            data = ShopingCart;
        }
        let checked = $(this).is(':checked');
        if (checked) {
            if (Array.isArray(data)) {
                $('.selected_product').each((index, element) => {
                    let id = $(element).data('id');
                    if ($(element).is(':checked') !== checked) {
                        addDataSelectedCheckout(id);
                        $(element).prop('checked', checked);
                    }
                })
            }
        } else {
            $('.selected_product').each((index, element) => {
                let id = $(element).data('id');
                if ($(element).is(':checked') !== checked) {
                    deleteByIdSelectedCheckout(id)
                    $(element).prop('checked', false);
                }
            })
        }
    })

    function check_checkAll() {
        $('.selected_product').prop('checked', false);
        $('#input_voucher').val('');
        SelectedVoucher = null;
        $('#selected_all_product').prop('checked', false);
    }


    $(document).on('change', '.selected_product', function () {
        let id = $(this).data('id');
        if ($(this).is(':checked')) {
            addDataSelectedCheckout(id)
        } else {
            deleteByIdSelectedCheckout(id);
        }
    });

    $(document).on('click', '.wraper_voucher', function () {
        if ($(this).hasClass('active')) {
            $(this).removeClass('active');
            $('#input_voucher').val('');
            SelectedVoucher = null;
            updateTotalMoney();
        } else {
            $('.wraper_voucher').removeClass('active');
            $(this).addClass('active');
            $('#input_voucher').val($(this).find('.voucher_code').text())
            let id = $(this).data('id');
            setDiscount(id, updateTotalMoney);
        }
    });

    $(document).on('click', '#btn-checkout', function () {
        let data = getCheckoutDataLocalStorage();
        if (data !== null && Array.isArray(data) && data.length > 0) {
            let datasubmit = [];
            data.forEach((product) => {
                datasubmit.push({
                    quantity: product.quantity,
                    id_product_detail: product.pro.id
                });
            });
            let jsonData = JSON.stringify(datasubmit);
            let form = $('<form>', {
                action: '/checkout',
                method: 'POST',
                style: 'display: none;'
            });
            $('<input>').attr({
                type: 'hidden',
                name: 'listData',
                value: jsonData
            }).appendTo(form);
            $('<input>').attr({
                type: 'hidden',
                name: 'maGiamGia',
                value: SelectedVoucher === null ? '' : SelectedVoucher.ma
            }).appendTo(form);
            form.appendTo('body');
            form.submit();
            check_checkAll()
        } else {
            ToastError('Vui lòng chọn sản phẩm.')
        }
    });


    function deleteProductInCheckoutAndLocal(id, element) {
        Swal.fire({
            title: "Xóa sản phẩm?",
            text: "Xóa sản phẩm khỏi giỏ hàng?",
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
            didRender: () => {
                $('.swal2-select').remove();
            }
        }).then((result) => {
            if (result.isConfirmed) {
                deleteByIdSelectedCheckout(id);
                if (username === undefined) {
                    deleteByIdLocal(id);
                    printProductwithStartup()
                } else {
                    deleteGHCT($(`#product_${id}`).data('cart-id'));
                }
                datatable.row(datatable.row(element).index()).remove().draw();
                ToastSuccess('Xóa thành công.')
            }
        });
    }

    function setDiscount(id, callback) {
        let data = ListVoucher;
        if (data !== null && Array.isArray(data)) {
            let voucher = data.find(item => item.id === Number(id));
            if (voucher !== null) {
                SelectedVoucher = voucher;
            } else {
                SelectedVoucher = null;
            }
        } else {
            SelectedVoucher = null;
        }
        callback();
    }

    function formatDate(dateTimeString) {
        let dateTime = new Date(dateTimeString);

        // Lấy các thành phần của ngày và giờ
        let day = dateTime.getDate();
        let month = dateTime.getMonth() + 1;
        let year = dateTime.getFullYear();
        let hours = dateTime.getHours();
        let minutes = dateTime.getMinutes();
        month = month < 10 ? '0' + month : month;
        day = day < 10 ? '0' + day : day;
        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        let formattedDateTime = hours + 'H' + minutes + ' Ngày ' + day + '/' + month + '/' + year;

        return formattedDateTime;
    }

    function updateTotalMoney() {
        let data = getCheckoutDataLocalStorage();
        if (Array.isArray(data)) {
            let totalMoney = 0;
            data.forEach((data) => {
                totalMoney += Number(extractNumberFromString(data.pro.gia_ban)) * Number(data.quantity);
            });
            if (SelectedVoucher !== null) {
                if (totalMoney < Number(SelectedVoucher.giaTriToiThieu)) {
                    SelectedVoucher = null;
                }
            }
            if (ListVoucher !== null && Array.isArray(ListVoucher)) {
                let html = '';
                ListVoucher.forEach((voucher) => {
                    let ele = $(`#voucher_${voucher.id}`)
                    if (totalMoney > Number(voucher.giaTriToiThieu)) {
                        if (ele.length === 0) {
                            let showPr = '';
                            if (voucher.loaiVoucher === '$') {
                                showPr = `<h3 class="col-6 tienmat">${formatNumberMoney(voucher.giaTriToiDa)}</h3>`;
                            } else {
                                showPr = `<h3 class="col-6 phantram">${voucher.giaTriPhanTram}%</h3>`
                            }
                            html += `
                        <li class="wraper_li scaleIn">
                            <div data-id="${voucher.id}" id="voucher_${voucher.id}" class="wraper_voucher row m-0">
                                <div class="col-9 contents p-2">
                                    <h5 class="text-center voucher_code">${voucher.ma}</h5>
                                    <label class="express_date">Hạn Đến: ${formatDate(voucher.endDate1)}</label>
                                    <label class="express_date">Điều Kiện: Áp dụng cho đơn hàng từ ${addCommasToNumber(voucher.giaTriToiThieu) + 'đ'}</label>
                                    <label class="express_date">Tối Đa:${addCommasToNumber(voucher.giaTriToiDa) + 'đ'}/Khách Hàng</label>
                                    <label class="express_date w-100">Số Lượng : ${voucher.soLuong}</label>
                                </div>
                                <div class="col-3 p-2 row m-0 card__discount position-relative">
                                    <label class="col-4">Mã Giảm Giá</label>
                                    ${showPr}
                                </div>
                            </div>
                        </li>`;
                        }
                    } else {
                        if (ele.length !== 0) {
                            ele.parent().addClass('scaleOut');
                            ele.parent().on('animationend', function () {
                                ele.parent().remove();
                            });
                        }
                    }
                })
                let voucher = $('#list-voucher');
                voucher.append(html);
            }
            let discountAmount;
            if (SelectedVoucher !== null) {
                $('#discount_element').removeClass('d-none');
                $('#discount_money').text(addCommasToNumber(SelectedVoucher.giaTriToiDa) + 'đ')
                if (SelectedVoucher.loaiVoucher === '%') {
                    if (Number(SelectedVoucher.giaTriToiDa) < Number(totalMoney) * (Number(SelectedVoucher.giaTriPhanTram) / 100)) {
                        discountAmount = Number(SelectedVoucher.giaTriToiDa);
                    } else {
                        discountAmount = Number(totalMoney) * (Number(SelectedVoucher.giaTriPhanTram) / 100);
                    }
                } else {
                    discountAmount = SelectedVoucher.giaTriTienMat;
                }
            } else {
                discountAmount = 0;
                $('#discount_element').addClass('d-none');
            }
            let totalPayment = Number(totalMoney) - Number(discountAmount);
            totalPayment = Math.ceil(totalPayment / 1000) * 1000;
            $('#sub-total').text(addCommasToNumber(totalMoney) + 'đ');
            $('#total-money').text(addCommasToNumber(totalPayment) + 'đ');
        }
    }

    function updateProductQuantity(id, num, data) {
        if (Array.isArray(data)) {
            for (let i = 0; i < data.length; i++) {
                if (data[i].pro.id == id) {
                    data[i].quantity = Math.max(Number(data[i].quantity) + Number(num), 1);
                    let newPrice = extractNumberFromString(data[i].pro.gia_ban) * Number(data[i].quantity);
                    newPrice = addCommasToNumber(newPrice);
                    let indexRow = datatable.row($(`#product_${id}`).closest('tr')).index();
                    datatable.cell({row: indexRow, column: 3}).data(newPrice + 'đ').draw();
                    break;
                }
            }
        }
    }

    function addQuantityProduct(id, num) {
        if (username === undefined) {
            let dataLocal = getProductInLocalStorage();
            let dataCheckout = getCheckoutDataLocalStorage();

            updateProductQuantity(id, num, dataLocal);
            updateProductQuantity(id, num, dataCheckout);

            saveCheckoutDatalocalStorage(dataCheckout);
            saveProductTolocalStorage(dataLocal);
        } else {
            let dataCheckout = getCheckoutDataLocalStorage();
            updateProductQuantity(id, num, dataCheckout);
            saveCheckoutDatalocalStorage(dataCheckout);
            let id_cart = $(`#product_${id}`).data('cart-id');
            if (id_cart !== undefined) {
                $.ajax({
                    url: '/api/update-shopping-cart-quantity',
                    type: 'POST',
                    data: {
                        id: id_cart,
                        calcul: Number(num) > 0 ? 'plus' : 'minus',
                    },
                    success: function () {
                        updateProductQuantity(id, num, ShopingCart);
                        ToastSuccess("Lưu thành công.")
                    }, error: function (e) {
                        ToastError("Lỗi.");
                        console.log(e)
                    }
                })
            }
        }
    }


    function addDataSelectedCheckout(id) {
        let data = [];
        if (username === undefined) {
            data = getProductInLocalStorage();
        } else {
            data = ShopingCart;
        }
        if (Array.isArray(data)) {
            let product = data.find(item => item.pro.id == id);
            let checkoutData = getCheckoutDataLocalStorage();
            if (checkoutData == null) {
                checkoutData = [];
            }
            checkoutData.push(product)
            saveCheckoutDatalocalStorage(checkoutData)
            updateTotalMoney();
        }
    }

    function deleteByIdSelectedCheckout(id) {
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
            updateTotalMoney();
        }
    }

    function deleteByIdLocal(id) {
        let data = getProductInLocalStorage();
        if (Array.isArray(data)) {
            let ind = null;
            for (let i = 0; i < data.length; i++) {
                if (data[i].pro.id == id) {
                    ind = i;
                    break;
                }
            }
            console.log('vào')
            data.splice(ind, 1);
            saveProductTolocalStorage(data);
            updateTotalMoney();
        }
    }

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
        return new Promise(function (resolve, reject) {
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
                                     <input class="selected_product" type="checkbox" data-cart-id="${product.id}" id="product_${product.chitietSanPham.id}" data-id="${product.chitietSanPham.id}"/>
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
                                <div class="product__cart__item__pic mr-1">
                                    <img width="90" height="90" src="${product.chitietSanPham.anh}" alt="product_img">
                                </div>
                                <div class="product__cart__item__text pt-0">
                                    <h6 class="mb-0">${product.chitietSanPham.ten}</h6>
                                     <label class="small mb-0">MS:${product.chitietSanPham.mauSac}, KT:${product.chitietSanPham.kichCo}</label>
                                    <h5>${addCommasToNumber(product.chitietSanPham.giaBan)}đ</h5>
                                </div>
                            </td>
                            <td class="quantity__item">
                                <div class="quantity">
                                    <div class="pro-qty-2" data-id="${product.chitietSanPham.id}">
                                    <span class="fa fa-angle-left dec qtybtn"></span>
                                        <input type="text" value="${product.soLuong}" readonly>
                                    <span class="fa fa-angle-right inc qtybtn"></span>
                                    </div>
                                </div>
                            </td>
                            <td class="cart__price">${addCommasToNumber(product.chitietSanPham.giaBan * Number(product.soLuong))}đ</td>
                            <td class="cart__close" data-id="${product.chitietSanPham.id}"><i class="fa fa-close"></i></td>
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
                        <div class="product__cart__item__pic mr-1">
                            <img width="90" height="90" src="${product.pro.product_img}" alt="product_img">
                        </div>
                        <div class="product__cart__item__text pt-0">
                            <h6 class="mb-0">${product.pro.name}</h6>
                            <label class="small mb-0">MS:${product.pro.color_name}, KT:${product.pro.size}</label>
                            <h5>${product.pro.gia_ban}đ</h5>
                        </div>
                    </td>
                    <td class="quantity__item">
                        <div class="quantity">
                            <div class="pro-qty-2" data-id="${product.pro.id}"><span class="fa fa-angle-left dec qtybtn"></span>
                                <input type="text" value="${product.quantity}" readonly>
                            <span class="fa fa-angle-right inc qtybtn"></span></div>
                        </div>
                    </td>
                    <td class="cart__price">${addCommasToNumber(extractNumberFromString(product.pro.gia_ban) * Number(product.quantity))}đ</td>
                    <td class="cart__close" data-id="${product.pro.id}"><i class="fa fa-close"></i></td>
                </tr>
                `;
            })
            $('#sub-total').text('0đ');
            $('#total-money').text('0đ');
            $('#show-data').empty().html(html);
        }
    }

    try {
        let data = await getAllVoucher();
        ListVoucher = data;
        console.log(data);
    } catch (error) {
        console.log(error);
    }

    function getAllVoucher() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/api/get-all-voucher',
                type: 'GET',
                success: function (data) {
                    resolve(data);
                },
                error: function (xhr) {
                    reject(xhr);
                }
            });
        });
    }

})
