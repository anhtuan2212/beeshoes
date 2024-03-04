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
                console.log(data);
            })
            .catch(function (error) {
                console.log('Có lỗi xảy ra khi lấy dữ liệu giỏ hàng từ máy chủ.');
                console.log(error);
            });
    }
    $(document).on('click', '.dec, .inc', function () {
        let id = $(this).parent().data('id');
        // Lấy giá trị hiện tại của input
        var value = parseInt($(this).siblings('input').val());
        // Xác định hành động dựa trên class của nút được nhấn
        if ($(this).hasClass('dec')) {
            // Giảm giá trị đi 1 nếu giá trị hiện tại lớn hơn 1
            if (value > 1) {
                $(this).siblings('input').val(value - 1);
            }
            addQuantityProduct(id, -1)
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
        let data = getProductInLocalStorage();
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
    $(document).on('change', '.selected_product', function () {
        let id = $(this).data('id');
        if ($(this).is(':checked')) {
            addDataSelectedCheckout(id)
        } else {
            deleteByIdSelectedCheckout(id);
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
                value: 'VLXX'
            }).appendTo(form);
            form.appendTo('body');
            form.submit();
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
                deleteByIdLocal(id);
                datatable.row(datatable.row(element).index()).remove().draw();
                ToastSuccess('Xóa thành công.')
            }
        });
    }

    function updateTotalMoney() {
        let datas = getCheckoutDataLocalStorage();
        if (Array.isArray(datas)) {
            let totalMoney = 0;
            datas.forEach((data) => {
                totalMoney += extractNumberFromString(data.pro.gia_ban) * Number(data.quantity);
            });
            $('#sub-total').text(addCommasToNumber(totalMoney) + 'đ');
            $('#total-money').text(addCommasToNumber(totalMoney) + 'đ');
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
        let dataLocal = getProductInLocalStorage();
        let dataCheckout = getCheckoutDataLocalStorage();

        updateProductQuantity(id, num, dataLocal);
        updateProductQuantity(id, num, dataCheckout);

        saveCheckoutDatalocalStorage(dataCheckout);
        saveProductTolocalStorage(dataLocal);
    }


    function addDataSelectedCheckout(id) {
        let data = getProductInLocalStorage();
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
            let totalMoney = 0;
            for (let i = 0; i < data.length; i++) {
                if (data[i].pro.id == id) {
                    ind = i;
                } else {
                    totalMoney += extractNumberFromString(data[i].pro.gia_ban) * Number(data[i].quantity);
                }
            }
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
                                    <div class="pro-qty-2" data-id="${product.id}">
                                    <span class="fa fa-angle-left dec qtybtn"></span>
                                        <input type="text" value="${product.soLuong}" readonly>
                                    <span class="fa fa-angle-right inc qtybtn"></span>
                                    </div>
                                </div>
                            </td>
                            <td class="cart__price">${addCommasToNumber(product.chitietSanPham.giaBan * Number(product.soLuong))}đ</td>
                            <td class="cart__close" data-id="${product.id}"><i class="fa fa-close"></i></td>
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
})

