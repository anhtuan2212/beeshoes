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

function isValidEmail(email) {
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}

function formValidate(e) {
    if (confirm('Bạn có muốn cập nhật không?')) {
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
        }

        if (isEmpty(cccd)) {
            ToastError("CCCD không được để trống.");
            return false;
        }

        if (isEmpty(ngaySinh)) {
            ToastError("Ngày sinh không được để trống.");
            return false;
        }

        if (isEmpty(soNha)) {
            ToastError("Số nhà không được để trống.");
            return false;
        }

        if (isEmpty(quanHuyen)) {
            ToastError("Quận/huyện không được để trống.");
            return false;
        }

        if (isEmpty(phuongXa)) {
            ToastError("Phường/xã không được để trống.");
            return false;
        }

        if (isEmpty(tinhTP)) {
            ToastError("Tỉnh/thành phố không được để trống.");
            return false;
        }
        ToastSuccess("Thành công")
        if (result == false) {
            ToastError("Thất bại")
            e.preventDefault();
        }
        return result;
    }
    ToastError("Thất bại")
    e.preventDefault();
}

//==============
$(document).on('ready', function () {
    // ONLY DEV
    // =======================================================

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