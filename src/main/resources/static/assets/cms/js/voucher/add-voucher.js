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

function formvalidate() {
    var result = true;
    var ten = document.getElementById("ten").value;
    var tienmat = document.getElementById("tienmat").value;
    var phantram = document.getElementById("phantram").value;
    var toithieu = document.getElementById("toithieu").value;
    var batdau = document.getElementById("batdau").value;
    var ketthuc = document.getElementById("ketthuc").value;
    var dieukien = document.getElementById("dieukien").value;
    var soluong = document.getElementById("soluong").value;
    var loai = document.getElementById("loai").value;
    if (ten.length == 0) {
        document.getElementById("ten_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("ten_emty").style.display = "none";
    }
    if (soluong.length != 0 && parseInt(soluong) > 0) {
        document.getElementById("soluong_emty").style.display = "none";

    } else {
        document.getElementById("soluong_emty").style.display = "block";
        result = false;
    }

    if (tienmat.length === 0 && parseFloat(tienmat) > 0) {
        document.getElementById("tienmat_emty").style.display = "none";
    } else {
        // Chuyển đổi chuỗi thành số kiểu double
        let tienmatValue = parseFloat(tienmat);

        // Kiểm tra nếu giá trị là số và lớn hơn 0
        if (!isNaN(tienmatValue) && tienmatValue >= 0) {
            document.getElementById("tienmat_emty").style.display = "none";
        } else {
            document.getElementById("tienmat_emty").style.display = "block";
            result = false;
        }
    }
    if (parseInt(phantram) >= 0 && parseInt(phantram) <= 100) {
        document.getElementById("phantram1_emty").style.display = "none"
    } else {
        document.getElementById("phantram1_emty").style.display = "block";
        result = false;
        ;
    }

    if (toithieu.length === 0 && parseFloat(toithieu) > 0) {
        document.getElementById("toithieu_emty").style.display = "none";
    } else {
        // Chuyển đổi chuỗi thành số kiểu double
        let toithieuValue = parseFloat(toithieu);

        // Kiểm tra nếu giá trị là số và lớn hơn 0
        if (!isNaN(toithieuValue) && toithieuValue > 0) {
            document.getElementById("toithieu_emty").style.display = "none";
        } else {
            document.getElementById("toithieu_emty").style.display = "block";
            result = false;
        }
    }
    if (batdau.length == 0) {
        document.getElementById("batdau_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("batdau_emty").style.display = "none";
    }
    if (ketthuc.length == 0) {
        document.getElementById("ketthuc_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("ketthuc_emty").style.display = "none";
    }
    if (ketthuc < batdau) {
        document.getElementById("ketthuc1_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("ketthuc1_emty").style.display = "none";
    }
    if (dieukien.length === 0 && parseFloat(dieukien) > 0) {
        document.getElementById("dieukien_emty").style.display = "none";
    } else {
        // Chuyển đổi chuỗi thành số kiểu double
        let dieukienValue = parseFloat(dieukien);

        // Kiểm tra nếu giá trị là số và lớn hơn 0
        if (!isNaN(dieukienValue) && dieukienValue > 0) {
            document.getElementById("dieukien_emty").style.display = "none";
        } else {
            document.getElementById("dieukien_emty").style.display = "block";
            result = false;
        }
    }
    if (loai.length == "") {
        document.getElementById("loai_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("loai_emty").style.display = "none";
    }
    if (result === false) {
        ToastError("Thất bại")
        return result;
    }
    return result;
}

document.getElementById("loai").addEventListener("change", function () {
    var selectedOption = this.value;

    if (selectedOption === "$") {
        // Ẩn phần trường nhập giá trị phần trăm và disabled nó
        document.getElementById("phantram").style.display = "none";

        document.getElementById("pt").style.display = "none";
        document.getElementById("phantram1_emty").style.display = "none";

        // Hiển thị phần trường nhập giá trị tiền mặt
        document.getElementById("tienmat").style.display = "block";
        document.getElementById("tm").style.display = "block";
        document.getElementById("tienmat_emty").style.display = "none";
        // Đặt giá trị của phần trăm là 0
        document.getElementById("phantram").value = 0;
    } else if (selectedOption === "%") {
        // Ẩn phần trường nhập giá trị tiền mặt và disabled nó
        document.getElementById("tienmat").style.display = "none";

        document.getElementById("tm").style.display = "none";
        document.getElementById("tienmat_emty").style.display = "none";
        // Hiển thị phần trường nhập giá trị phần trăm
        document.getElementById("phantram").style.display = "block";
        document.getElementById("pt").style.display = "block";
        document.getElementById("phantram1_emty").style.display = "none";
        // Đặt giá trị của tiền mặt là 0
        document.getElementById("tienmat").value = 0;
    }
});


$(document).on('ready', function () {
    // ONLY DEV
    // =======================================================

    $('#btn-submit-form').on('click',function () {
        Swal.fire({
            title: "Xác nhận lưu?",
            text: "Các dữ liệu sẽ được lưu lại?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            cancelButtonText: "không",
            confirmButtonText: "Xác Nhận"
        }).then((result) => {
            if (result.isConfirmed) {
                if (formvalidate()){
                    $('#form-data').submit();
                }
            }
        })

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


    // INITIALIZATION OF NAV SCROLLER
    // =======================================================
    $('.js-nav-scroller').each(function () {
        new HsNavScroller($(this)).init()
    });


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
        var select2 = $.HSCore.components.HSSelect2.init($(this));
    });


    // INITIALIZATION OF DATATABLES
    // =======================================================
});
