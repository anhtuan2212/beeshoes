function PrintBillOder() {
    let elementToPrint = document.getElementById("oder-print");
    printElement(elementToPrint);
}

resetData();

function initSelect2(element) {
    $.HSCore.components.HSSelect2.init(element);
}

let canRunDetection = true;
let dataShop = [];
let dataProductDetails = [];
let num_oder_add = null;
window.onload = function () {
    // ToastSuccess('Tải hoàn tất !')
    $.ajax({
        url: '/api/get-all-san-pham',
        type: 'GET',
        success: function (data) {
            dataShop = [...data];
            dataProductDetails = data.flatMap(obj => obj.chiTietSanPham);
            console.log(dataShop)
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("Lỗi khi gọi API: " + textStatus, errorThrown);
        }
    })
};

// hàm thêm dấu chấm vào số
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

function PrintHtmlOder(product, numOder) {
    let table = $(`#datatable_hd_${numOder}`);
    let totalMoney = addCommasToNumber(Number(product.quantity) * Number(product.giaBan));
    if (table.length > 0) {
        let tbody = table.find('tbody');
        if (table.find('.tr-show-no-data').length > 0) {
            tbody.empty();
        }
        let tr = table.find(`#row_${numOder + '_' + product.id}`);
        if (tr.length > 0) {
            let curentQuantity = tr.find('.input-group-quantity-counter-control').val();
            if (Number(curentQuantity) !== Number(product.quantity)) {
                tr.find('.input-group-quantity-counter-control').val(product.quantity);
                let total = Number(product.quantity) * Number(product.giaBan);
                tr.find('.form-show-total-tr').val(addCommasToNumber(total));

            }
        } else {
            let html = `
                <tr id="row_${numOder + '_' + product.id}" data-id-product="${product.id}" data-id-hd="${numOder}">
                    <td>
                        <a class="media align-items-center" href="/cms/view-product?id=16">
                            <img class="avatar avatar-lg mr-3" src="${product.anh}"
                                 alt="Image Product">
                            <div class="media-body">
                                <h5 class="text-hover-primary mb-0">${product.ten}
                                    (${product.tenMau + '-' + product.kichCo})</h5>
                            </div>
                        </a>
                    </td>
                    <td class="table-column-pl-0">
                        <div class="js-quantity-counter input-group-quantity-counter">
                            <input type="number"
                                   class="js-result form-quantity form-control input-group-quantity-counter-control"
                                   value="${product.quantity}">
                            <div class="input-group-quantity-counter-toggle">
                                <a class="js-minus input-group-quantity-counter-btn"
                                   href="javascript:;">
                                    <i class="tio-remove"></i>
                                </a>
                                <a class="js-plus input-group-quantity-counter-btn"
                                   href="javascript:;">
                                    <i class="tio-add"></i>
                                </a>
                            </div>
                        </div>
                    </td>
                    <td class="table-column-pl-0">
                        <div class="input-group input-group-merge">
                            <input type="text" class="form-control form-show-total-tr" value="${totalMoney}" readonly>
                            <div class="input-group-prepend money-dv">
                                <div class="input-group-text">VND</div>
                            </div>
                        </div>
                    </td>
                    <td class="pl-1 pr-1">
                        <a class="btn btn-white btn-delete-tr-oder" href="javascript:;">
                            <i class="tio-delete-outlined"></i>
                        </a>
                    </td>
                </tr>`;
            tbody.append(html);
        }
    } else {
        ToastError('Lỗi.')
    }
}

function findProductByCode(code) {
    if (!Array.isArray(dataProductDetails)) {
        console.error("dataProductDetails không phải là một mảng.");
        return null;
    }
    let data = [...dataProductDetails];
    let product = data.find(pro => pro.maSanPham.toString().toLowerCase() === code.toString().toLowerCase());
    if (product === undefined) {
        ToastError('Mã sản phẩm không tồn tại.')
        return null;
    }
    return product;
}

function findProductById(id) {
    if (!Array.isArray(dataProductDetails)) {
        console.error("dataProductDetails không phải là một mảng.");
        return null;
    }
    let data = [...dataProductDetails];
    let product = data.find(pro => pro.id === id);
    return product;
}

function saveProductToOder(product, numOder, quantity) {
    product.quantity = quantity;
    let data = getListProductLocal(numOder);
    let index = null;
    let found = false;
    let pro = null;
    if (Number(product.soLuongTon) < 1) {
        ToastError('Sản phẩm tạm hết hàng.');
        return;
    }
    if (data !== null && Array.isArray(data)) {
        for (let i = 0; i < data.length; i++) {
            if (Number(data[i].id) === Number(product.id)) {
                data[i].quantity = Number(data[i].quantity) + Number(quantity);
                pro = data[i]
                found = true;
                index = i;
                break;
            }
        }
        if (!found) {
            pro = product;
            data.push(product);
        }
    } else {
        data = []
        pro = product;
        data.push(product);
    }
    if (index !== null && Number(data[index].quantity) > Number(product.soLuongTon)) {
        ToastError('Số lượng tồn kho đã tối đa.');
        return;
    }
    if (pro !== null) {
        PrintHtmlOder(pro, numOder)
    }
    saveToLocalStorage(data, numOder)
    updateTotalMoney(numOder)
}

function resetData() {
    localStorage.setItem('List_product_oder_1', JSON.stringify([]))
    localStorage.setItem('List_product_oder_2', JSON.stringify([]))
    localStorage.setItem('List_product_oder_3', JSON.stringify([]))
    localStorage.setItem('List_product_oder_4', JSON.stringify([]))
    localStorage.setItem('List_product_oder_5', JSON.stringify([]))

}

function saveToLocalStorage(listPro, oder) {
    localStorage.setItem(`List_product_oder_${oder}`, JSON.stringify(listPro))
}

function getListProductLocal(oder) {
    return JSON.parse(localStorage.getItem(`List_product_oder_${oder}`));
}

function updateTotalMoney(oder) {
    let data = getListProductLocal(oder);
    let total = 0;
    if (data !== null && Array.isArray(data)) {
        data.forEach((pro) => {
            total += Number(pro.quantity) * Number(pro.giaBan);
        })
    }
    let wrapper = $(`#oder_content_${oder}`);
    let type = wrapper.find(`input[name="deliveryOptionCheckbox_hd_${oder}"]:checked`).val();
    let shipingFee = 0;
    if (type === 'CP') {
        shipingFee = 30000;
        if (total > 2000000) {
            shipingFee = 0;
        }
    }
    let payment = total + shipingFee;
    wrapper.find('h4.total-money').text(addCommasToNumber(total) + 'đ')
    wrapper.find('h4.shipping-money').text(addCommasToNumber(shipingFee) + 'đ')
    wrapper.find('h3.payment-money').text(addCommasToNumber(payment) + 'đ')
}

function printElement(elementToPrint) {
    let iframe = document.getElementById("myFrame");
    let iframeDocument = iframe.contentWindow.document;
    let html = `
               <html>
                   <head>
                       <title>LightBee</title>
                       <link rel="stylesheet" href="/assets/cms/css/vendor.min.css">
                       <link rel="stylesheet" href="/assets/cms/vendor/icon-set/style.css">
                       <link rel="stylesheet" href="/assets/cms/css/custom.css?v=1.0">
                       <link rel="stylesheet" href="/assets/cms/css/cashier.css?v=1.0">
                       <link rel="stylesheet" href="/assets/cms/css/theme.min.css?v=1.0">
                   </head>
                       <body>
                           ${elementToPrint.innerHTML}
                       </body>
                       <script>
                       window.print();
                       </script>
               </html>`
    iframeDocument.open();
    iframeDocument.write(html); // Truyền chuỗi HTML vào phương thức write()
    iframeDocument.close();
    // iframe.contentWindow.print();
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

function Confirm(title, message, txt_cancel, txt_confirm) {
    return new Promise((resolve, reject) => {
        Swal.fire({
            title: title,
            text: message,
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            cancelButtonText: txt_cancel,
            confirmButtonText: txt_confirm,
        }).then((result) => {
            resolve(result.isConfirmed);
        });
    })
}

function getOderNum(element) {
    return $(element).closest('.oder-wraper-content').data('id-num-hd');
}

function deleteById(id, oder) {
    let list = getListProductLocal(oder);
    if (list === null) {
        return;
    }
    list = list.filter(item => item.id !== id);
    saveToLocalStorage(list, oder);
    updateTotalMoney(oder);
    ToastSuccess('Xóa thành công.')
}

let Detection = true;

function addProductByBarcode(code) {
    if (Detection) {
        console.log(code)
        let product = findProductByCode(code);
        if (num_oder_add !== null && product !== null) {
            saveProductToOder(product, num_oder_add, 1);
        }
        Detection = false;
        setTimeout(() => {
            Detection = true;
        }, 2000);
    }
}

function updateQuantityProduct(id, oder, operator, element) {
    let data = getListProductLocal(oder);
    let index = data.findIndex(item => Number(item.id) === Number(id));
    if (index === -1) {
        console.log('K tìm thấy dữ liệu.')
        return;
    }
    if (data[index].quantity === 1 && operator === 'minus') {
        let tr = $(element).closest('tr')
        let tbody = tr.closest('tbody');
        Confirm('Xác Nhận Xóa !', 'Thao tác không thể hoàn tác.', 'Hủy', 'Xác Nhận').then((check) => {
            if (check) {
                deleteById(id, oder);
                if (tbody.find('tr').length === 1) {
                    let html =
                        `<tr class="tr-show-no-data">
                    <td class="table-column-pr-0" rowspan="4">
                        <h5 class="text-center">Chưa có dữ liệu.</h5>
                    </td>
                </tr>`;
                    tbody.append(html)
                }
                tr.remove();
            }
        })
    } else {
        if (operator === 'minus') {
            data[index].quantity = Number(data[index].quantity) - 1;
        } else {
            data[index].quantity = Number(data[index].quantity) + 1;
        }
        saveToLocalStorage(data, oder);
        updateTotalMoney(oder);
        PrintHtmlOder(data[index], oder);
    }
}

$(document).on('ready', function () {

    $(document).on('click', '.input-group-quantity-counter-btn', function () {
        let oder = getOderNum(this);
        let tr = $(this).closest('tr')
        let id = tr.data('id-product')
        if ($(this).hasClass('js-plus')) {
            updateQuantityProduct(id, oder, 'plus', this)
        } else {
            updateQuantityProduct(id, oder, 'minus', this)
        }
    })
    $(document).on('click', '.btn-delete-tr-oder', function () {
        let tr = $(this).closest('tr');
        let id = tr.data('id-product');
        let oder = tr.data('id-hd');
        let tbody = tr.closest('tbody');
        Confirm('Xác Nhận Xóa !', 'Thao tác không thể hoàn tác.', 'Hủy', 'Xác Nhận').then((check) => {
            if (check) {
                deleteById(id, oder);
                if (tbody.find('tr').length === 1) {
                    let html =
                        `<tr class="tr-show-no-data">
                    <td class="table-column-pr-0" rowspan="4">
                        <h5 class="text-center">Chưa có dữ liệu.</h5>
                    </td>
                </tr>`;
                    tbody.append(html)
                }
                tr.remove();
            }
        })
    })
    $(document).on('change', '.option-select-receipt', function () {
        let oder = getOderNum(this);
        updateTotalMoney(oder);
    })
    $(document).on('click', '.li-item-ctsp-search', function () {
        console.log('run')
        let id_pro = $(this).data('ctsp-id');
        let oder = getOderNum(this);
        let product = findProductById(id_pro);
        saveProductToOder(product, oder, 1);
    })

    let modalBarcode = $('#form-modal-barcode');
    $(document).on('click', '.btn-scan-barcode', function () {
        let oder = getOderNum(this);
        num_oder_add = oder;
        modalBarcode.modal('show');
    })
    modalBarcode.on('shown.bs.modal', function () {
        let video = document.getElementById("video_show_camera");
        let beepSound = document.getElementById("sound_beep");
        Quagga.init(
            {
                inputStream: {
                    name: "Live",
                    type: "LiveStream",
                    target: document.querySelector("#show_video_live"),
                    constraints: {
                        width: 640,
                        height: 480,
                        facingMode: "environment",
                    },
                },
                decoder: {
                    readers: ["code_128_reader"],
                },
            },
            function (err) {
                if (err) {
                    console.log(err);
                    return;
                }
                console.log("Initialization finished. Ready to start");
                Quagga.start();
            }
        );
        Quagga.onDetected(function (result) {
            if (canRunDetection) {
                addProductByBarcode(result.codeResult.code)
                beepSound.play();
                canRunDetection = false;
                setTimeout(() => {
                    canRunDetection = true;
                }, 2000);
            }
        });

    });
    modalBarcode.on('hidden.bs.modal', function () {
        Quagga.stop();
    });
    $(document).on('change', '.customer-selected', function () {
        // let id = $(this).closest('.oder-wraper-content').data('id-num-hd');
        let content = $(this).closest('.oder-wraper-content');
        if ($(this).val() !== '#') {
            let option = $(this).find('option:selected');
            let arrName = option.text().split('-');
            let name = arrName[0];
            let cusCode = arrName[1];
            let phone = option.data('phone');
            let email = option.data('email');
            content.find('strong.fullName-customer').text(name)
            content.find('strong.email-customer').text(email)
            content.find('strong.phone-customer').text(phone)
            content.find('strong.code-customer').text(cusCode)
        } else {
            content.find('strong.fullName-customer').text('')
            content.find('strong.email-customer').text('')
            content.find('strong.phone-customer').text('')
            content.find('strong.code-customer').text('')
        }
    })
    $(document).on('click', '.btn-delete-oder', function () {
        Confirm('Xác Nhận Xóa !', 'Sau khi xóa sẽ không thể hoàn tác', 'Đóng', 'Xác Nhận').then(check => {
            if (check) {
                let li = $(this).closest('li.nav-item');
                let a = li.find('a.nav-link');
                if (a.hasClass('active')) {
                    let prevLi = li.prev();
                    if (prevLi.length !== 0) {
                        let id = prevLi.find('a.nav-link').attr('href');
                        prevLi.find('a.nav-link').addClass('active');
                        $(id).addClass('show active')
                    } else {
                        let nextLi = li.next();
                        if (nextLi.length !== 0) {
                            let id = nextLi.find('a.nav-link').attr('href');
                            nextLi.find('a.nav-link').addClass('active');
                            $(id).addClass('show active')
                        }
                    }
                }
                let id_ct = a.attr('href');
                $(id_ct).remove();
                li.remove();
                ToastSuccess('Xóa Thành Công.')
            }
        })
    })
    $('#btn-add-new-bill').on('click', function () {
        let listItem = $('#list-item-oder');
        let tabContent = $('#tab-content-show');
        let quantity_oder = listItem.find('li');
        if (quantity_oder.length > 4) {
            return;
        }
        let numId = quantity_oder.length + 1
        if ($(`#oder_content_${numId}`).length > 0) {
            for (let i = 1; i < 9; i++) {
                if ($(`#oder_content_${i}`).length === 0) {
                    numId = i;
                    break;
                }
            }
        }
        let navTtem =
            ` <li class="nav-item position-relative">
                <a class="nav-link nav-show-oder" id="nav-link-tab-${numId}" data-toggle="pill" href="#nav-content-tab-${numId}" role="tab"
                   aria-controls="nav-content-tab-${numId}" aria-selected="false">Hóa Đơn ${numId}</a>
                <i class="tio-clear btn-delete-oder"></i>
            </li>`;
        listItem.append(navTtem)
        let contentMau = $('#oder_content_mau');
        let div = contentMau.clone();
        div.find('h4.card-header-title').text('Hóa Đơn ' + numId);
        div.find('select.customer-selected').addClass('js-select2-custom')
        div.find('[data-id-hd]').each((index, ele) => {
            let curentId = $(ele).attr('id');
            let id = $(ele).data('id-hd') + numId;
            let type = $(ele).attr('type');
            if (type === 'checkbox') {
                div.find(`label[for=${curentId}]`).attr('for', id);
            }
            if (type === 'radio') {
                div.find(`label[for='${curentId}']`).attr('for', id + '_' + index);
                $(ele).attr('id', id + '_' + index)
                $(ele).attr('name', id)
            } else {
                $(ele).attr('id', id)
            }
            // console.log(ele)
        })
        let navContent =
            `<div class="tab-pane fade" id="nav-content-tab-${numId}" role="tabpanel" aria-labelledby="nav-link-tab-${numId}">
             <div id="oder_content_${numId}" class="row oder-wraper-content" data-id-num-hd="${numId}">
              ${div.html()}
              </div>
             </div>`;
        tabContent.append(navContent);
        $('.js-select2-custom').each(function () {
            initSelect2($(this));
        });
    })


    if (window.localStorage.getItem('hs-builder-popover') === null) {
        $('#builderPopover').popover('show')
            .on('shown.bs.popover', function () {
                $('.popover').last().addClass('popover-dark')
            });
        $(document).on('click', '#closeBuilderPopover', function () {
            window.localStorage.setItem('hs-builder-popover', true);
            $('#builderPopover').popover('dispose');
        });
    } else {
        $('#builderPopover').on('show.bs.popover', function () {
            return false
        });
    }

    // END ONLY DEV
    // =======================================================

    // INITIALIZATION OF QUANTITY COUNTER
    // =======================================================
    $('.js-quantity-counter').each(function () {
        var quantityCounter = new HSQuantityCounter($(this)).init();
    });
    $(document).on('input', '.form-input-search', function () {
        let val = $(this).val();
        let formShow = $(this).closest('.wrapper-form-search').find('.div-form-search');
        formShow.removeClass('d-none')
        let list = [...dataProductDetails];
        let li = formShow.find('li');
        li.removeClass('d-none');
        if (val.length > 0) {
            list.forEach((ctsp) => {
                let propertiesToCheck = ['ten', 'tenMau', 'maSanPham', 'kichCo'];
                let isHave = propertiesToCheck.some(property => ctsp[property].toString().toLowerCase().includes(val.toLowerCase()));
                if (!isHave) {
                    formShow.find(`li.li-item-ctsp-search[data-ctsp-id="${ctsp.id}"]`).addClass('d-none');
                }
            });
        }
    });
    $(document).on('focus', '.form-input-search', function () {
        console.log('focus')
        let formShow = $(this).closest('.wrapper-form-search').find('.div-form-search');
        formShow.removeClass('d-none')
    });
    $(document).on('blur', '.form-input-search', function () {
        console.log('un focus')
        let formShow = $(this).closest('.wrapper-form-search').find('.div-form-search');

        // Kiểm tra xem chuột có nằm trong phạm vi của .div-form-search hay không
        if (!formShow.is(':hover')) {
            formShow.addClass('d-none');
        }
    });

    $(document).on('mouseleave', '.div-form-search', function () {
        $(this).addClass('d-none');
    });

    // INITIALIZATION OF ADD INPUT FILED
    // =======================================================
    $('.js-add-field').each(function () {
        new HSAddField($(this), {
            addedField: function () {
                $('.js-add-field .js-select2-custom-dynamic').each(function () {
                    var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
                });

                $('[data-toggle="tooltip"]').tooltip();


                // INITIALIZATION OF QUANTITY COUNTER
                // =======================================================
                $('.js-quantity-counter').each(function () {
                    var quantityCounter = new HSQuantityCounter($(this)).init();
                });
            },
            deletedField: function () {
                $('.tooltip').hide();
            }
        }).init();
    });


    // BUILDER TOGGLE INVOKER
    // =======================================================
    $('.js-navbar-vertical-aside-toggle-invoker').click(function () {
        $('.js-navbar-vertical-aside-toggle-invoker i').tooltip('hide');
    });


    // INITIALIZATION OF MEGA MENU
    // =======================================================
    var megaMenu = new HSMegaMenu($('.js-mega-menu'), {
        desktop: {
            position: 'left'
        }
    }).init();


    // INITIALIZATION OF NAVBAR VERTICAL NAVIGATION
    // =======================================================
    var sidebar = $('.js-navbar-vertical-aside').hsSideNav();


    // INITIALIZATION OF TOOLTIP IN NAVBAR VERTICAL MENU
    // =======================================================
    $('.js-nav-tooltip-link').tooltip({boundary: 'window'})

    $(".js-nav-tooltip-link").on("show.bs.tooltip", function (e) {
        if (!$("body").hasClass("navbar-vertical-aside-mini-mode")) {
            return false;
        }
    });


    // INITIALIZATION OF UNFOLD
    // =======================================================
    $('.js-hs-unfold-invoker').each(function () {
        var unfold = new HSUnfold($(this)).init();
    });


    // INITIALIZATION OF FORM SEARCH
    // =======================================================
    $('.js-form-search').each(function () {
        new HSFormSearch($(this)).init()
    });


    // INITIALIZATION OF SELECT2
    // =======================================================
    // Định nghĩa hàm để khởi tạo Select2 cho một phần tử


    $('.js-select2-custom').each(function () {
        initSelect2($(this));
    });

    // $(document).on('DOMNodeInserted', function (e) {
    //     if ($(e.target).hasClass('js-select2-custom')) {
    //         initSelect2($(e.target));
    //     }
    // });

    // INITIALIZATION OF QUILLJS EDITOR
    // =======================================================
    var quill = $.HSCore.components.HSQuill.init('.js-quill');


    // INITIALIZATION OF ADD INPUT FILED
    // =======================================================
    // $('.js-add-field').each(function () {
    //     new HSAddField($(this), {
    //         addedField: function () {
    //             $('.js-add-field .js-select2-custom-dynamic').each(function () {
    //                 var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
    //             });
    //         }
    //     }).init();
    // });


    // INITIALIZATION OF TAGIFY
    // =======================================================
    $('.js-tagify').each(function () {
        var tagify = $.HSCore.components.HSTagify.init($(this));
    });


    // INITIALIZATION OF DROPZONE FILE ATTACH MODULE
    // =======================================================
    $('.js-dropzone').each(function () {
        var dropzone = $.HSCore.components.HSDropzone.init('#' + $(this).attr('id'));
    });


    // INITIALIZATION OF STEP FORM
    // =======================================================
    $('.js-step-form').each(function () {
        var stepForm = new HSStepForm($(this), {
            finish: function () {
                $("#checkoutStepFormProgress").hide();
                $("#checkoutStepFormContent").hide();
                $("#checkoutStepOrderSummary").hide();
                $("#checkoutStepSuccessMessage").show();
            }
        }).init();
    });


    // INITIALIZATION OF MASKED INPUT
    // =======================================================
    $('.js-masked-input').each(function () {
        var mask = $.HSCore.components.HSMask.init($(this));
    });
});


<!-- IE Support -->

if (/MSIE \d|Trident.*rv:/.test(navigator.userAgent)) document.write('<script src="/assets/vendor/babel-polyfill/polyfill.min.js"><\/script>');
