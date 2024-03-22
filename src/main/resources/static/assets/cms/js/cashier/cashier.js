function PrintOder() {
    let elementToPrint = document.getElementById("oder-print");
    printElement(elementToPrint);
}

function initSelect2(element) {
    $.HSCore.components.HSSelect2.init(element);
}

let dataShop = [];
let dataProductDetails = [];
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

$(document).on('ready', function () {
    let modalBarcode = $('#form-modal-barcode');
    $(document).on('click', '.btn-scan-barcode', function () {
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
        let canRunDetection = true;

        Quagga.onDetected(function (result) {
            if (canRunDetection) {
                ToastSuccess(result.codeResult.code);
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
        formShow.addClass('d-none')
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
