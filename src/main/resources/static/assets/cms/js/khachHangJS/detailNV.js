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

function isEmpty(str) {
    return (!str || str.length === 0);
}

function isValidPhoneNumber(phoneNumber) {
    let phone = /^(0\d{9,10})$/;
    // Kiểm tra xem số điện thoại có bắt đầu bằng số 0 và có đúng 10 số không
    return phone.test(phoneNumber);
}

function isValidEmail(email) {
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}

function formValidate() {
    var hoTen = $('#hoTen').val();
    var email = $('#email').val();
    var sdt = $('#sdt').val();
    var cccd = $('#cccd').val();
    var ngaySinh = $('#ngaySinh').val();
    var soNha = $('#soNha').val();
    var phuongXa = $('#phuongXa').val();
    var quanHuyen = $('#quanHuyen').val();
    var tinhTP = $('#tinhTP').val();

    if (isEmpty(hoTen)) {
        ToastError("Họ tên không được để trống.");
        return false;
    }

    if (isEmpty(email)) {
        ToastError("Email không được để trống.");
        return false;
    } else if (!isValidEmail(email)) {
        ToastError("Email không đúng định dạng.");
        return false;
    }

    if (isEmpty(sdt)) {
        ToastError("Số điện thoại không được để trống.");
        return false;
    } else if (!isValidPhoneNumber(sdt)) {
        ToastError("Sdt không đúng định dạng(0359xxxxxx)");
        return false;
    }

    if (isEmpty(cccd)) {
        ToastError("CCCD không được để trống.");
        return false;
    }

    // Kiểm tra ngày sinh phải trước ngày hiện tại
    var ngaySinhDate = new Date(ngaySinh);
    var ngayHienTai = new Date();
    if (ngaySinhDate >= ngayHienTai) {
        ToastError("Ngày sinh phải trước ngày hiện tại.");
        return false;
    }
    else if (isEmpty(ngaySinh)) {
        ToastError("Ngày sinh không được để trống.");
        return false;
    }

    if (isEmpty(soNha)) {
        ToastError("Số nhà không được để trống.");
        return false;
    }

    if (isEmpty(tinhTP)) {
        ToastError('Vui lòng chọn tỉnh.')
        return false;
    }
    if (isEmpty(quanHuyen)) {
        ToastError('Vui lòng chọn quận huyện.')
        return false;
    }
    if (isEmpty(phuongXa)) {
        ToastError('Vui lòng chọn phường xã.')
        return false;
    }

    // if (isEmpty(quanHuyen)) {
    //     ToastError("Quận/huyện không được để trống.");
    //     return false;
    // }
    //
    // if (isEmpty(phuongXa)) {
    //     ToastError("Phường/xã không được để trống.");
    //     return false;
    // }
    //
    // if (isEmpty(tinhTP)) {
    //     ToastError("Tỉnh/thành phố không được để trống.");
    //     return false;
    // }
    return true;
}

//=============api diaCHi
// let province_list = [];
// let district_list = [];
// let ward_list = [];
// let quan_huyen = $('.quanHuyen').closest("div");
// let tinh_TP = $('.tinhTP').closest("div");
// let phuong_xa = $('.phuong_xa').closest("div");
//
// function handleData(data, dropdown, idKey, valueKey) {
//     dropdown.empty().append(`<option value="">Vui lòng chọn</option>`);
//     data.forEach(item => {
//         dropdown.append(`<option data-id="${item[idKey]}" value="${item[valueKey]}">${item[valueKey]}</option>`);
//     });
// }
//
// function fillAllTinh() {
//     return new Promise((resolve, reject) => {
//         tinh_TP.each((index, element) => {
//             let select = $(element).find('select');
//             let tinh = $(element).find('input[type="text"]').val();
//             select.append(`<option value="" >Chọn Tỉnh</option>`);
//             province_list.forEach(data => {
//                 select.append(`<option value="${data.Id}" ${tinh === data.Name ? 'selected' : ''}>${data.Name}</option>`);
//             });
//         });
//         resolve();
//     });
// }
//
// function fillAllHuyen() {
//     return new Promise((resolve, reject) => {
//         quan_huyen.each((index, element) => {
//             let select = $(element).find('select');
//             select.html('');
//             let name = $(element).find('input[type="text"]').val();
//             let idTinh = $(element).closest('.form-address').find('.tinhTP').val();
//             select.append(`<option value="" >Chọn huyện</option>`);
//             district_list.forEach(data => {
//                 if (data.ProvinceId == idTinh) {
//                     select.append(`<option value="${data.Id}" ${name === data.Name ? 'selected' : ''}>${data.Name}</option>`);
//                 }
//             });
//         });
//         resolve();
//     });
// }
//
// function fillAllXa() {
//     return new Promise((resolve, reject) => {
//         phuong_xa.each((index, element) => {
//             let select = $(element).find('select');
//             select.html('');
//             let name = $(element).find('input[type="text"]').val();
//             let idHuyen = $(element).closest('.form-address').find('select.quanHuyen').val();
//             select.append(`<option value="" >Chọn xã</option>`);
//             ward_list.forEach(data => {
//                 if (data.DistrictId == idHuyen) {
//                     let selected = data.Name == name ? 'selected' : '';
//                     select.append(`<option value="${data.Id}" ${selected}>${data.Name}</option>`);
//                 }
//             });
//         });
//         resolve();
//     });
// }
//
// fetch('/assets/cms/json/TinhTP.json')
//     .then(response => response.json())
//     .then(data => {
//         province_list = data;
//         handleData(province_list, $('.tinhTP'), 'Id', 'Name');
//         return fillAllTinh();
//     })
//     .then(() => {
//         return fetch('/assets/cms/json/QuanHuyen.json')
//             .then(response => response.json())
//             .then(data => {
//                 district_list = data;
//                 return fillAllHuyen();
//             });
//     })
//     .then(() => {
//         return fetch('/assets/cms/json/PhuongXa.json')
//             .then(response => response.json())
//             .then(data => {
//                 ward_list = data;
//                 return fillAllXa();
//             });
//     })
//     .catch(error => console.error('Error:', error));

//==============
$(document).on('ready', function () {
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
    // $('.tinhTP').on('change', function () {
    //     $(this).closest('.form-address').find('select.phuong_xa').html('')
    //     return fillAllHuyen()
    // })
    // $('.quanHuyen').on('change', function () {
    //     return fillAllXa();
    // })

    // BUILDER TOGGLE INVOKER
    // =======================================================
    $('.js-navbar-vertical-aside-toggle-invoker').click(function () {
        $('.js-navbar-vertical-aside-toggle-invoker i').tooltip('hide');
    });

    $('.js-masked-input').each(function () {
        var mask = $.HSCore.components.HSMask.init($(this));
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


    // INITIALIZATION OF FILE ATTACH
    // =======================================================
    $('.js-file-attach').each(function () {
        var customFile = new HSFileAttach($(this)).init();
    });


    // INITIALIZATION OF STEP FORM
    // =======================================================
    $('.js-step-form').each(function () {
        var stepForm = new HSStepForm($(this), {
            finish: function () {
                $("#addUserStepFormProgress").hide();
                $("#addUserStepFormContent").hide();
                $("#successMessageContent").show();
            }
        }).init();
    });


    // INITIALIZATION OF MASKED INPUT
    // =======================================================
    $('.js-masked-input').each(function () {
        var mask = $.HSCore.components.HSMask.init($(this));
    });


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
        var select2 = $.HSCore.components.HSSelect2.init($(this));
    });


    // INITIALIZATION OF ADD INPUT FILED
    // =======================================================
    $('.js-add-field').each(function () {
        new HSAddField($(this), {
            addedField: function () {
                $('.js-add-field .js-select2-custom-dynamic').each(function () {
                    var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
                });
            }
        }).init();
    });
});