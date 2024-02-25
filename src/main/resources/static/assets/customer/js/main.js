/*  ---------------------------------------------------
    Template Name: Male Fashion
    Description: Male Fashion - ecommerce teplate
    Author: Colorib
    Author URI: https://www.colorib.com/
    Version: 1.0
    Created: Colorib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");

        /*------------------
            Gallery filter
        --------------------*/
        $('.filter__controls li').on('click', function () {
            $('.filter__controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.product__filter').length > 0) {
            var containerEl = document.querySelector('.product__filter');
            var mixer = mixitup(containerEl);
        }
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    //Search Switch
    $('.search-switch').on('click', function () {
        $('.search-model').fadeIn(400);
    });

    $('.search-close-switch').on('click', function () {
        $('.search-model').fadeOut(400, function () {
            $('#search-input').val('');
        });
    });

    /*------------------
		Navigation
	--------------------*/
    $(".mobile-menu").slicknav({
        prependTo: '#mobile-menu-wrap',
        allowParentLinks: true
    });

    /*------------------
        Accordin Active
    --------------------*/
    $('.collapse').on('shown.bs.collapse', function () {
        $(this).prev().addClass('active');
    });

    $('.collapse').on('hidden.bs.collapse', function () {
        $(this).prev().removeClass('active');
    });

    //Canvas Menu
    $(".canvas__open").on('click', function () {
        $(".offcanvas-menu-wrapper").addClass("active");
        $(".offcanvas-menu-overlay").addClass("active");
    });

    $(".offcanvas-menu-overlay").on('click', function () {
        $(".offcanvas-menu-wrapper").removeClass("active");
        $(".offcanvas-menu-overlay").removeClass("active");
    });

    /*-----------------------
        Hero Slider
    ------------------------*/
    $(".hero__slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 1,
        dots: false,
        nav: true,
        navText: ["<span class='arrow_left'><span/>", "<span class='arrow_right'><span/>"],
        animateOut: 'fadeOut',
        animateIn: 'fadeIn',
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: false
    });

    /*--------------------------
        Select
    ----------------------------*/
    $("select").niceSelect();

    /*-------------------
		Radio Btn
	--------------------- */
    $(document).on('click',".product__color__select label, .shop__sidebar__size label, .product__details__option__size label", function () {
        $(".product__color__select label, .shop__sidebar__size label, .product__details__option__size label").removeClass('active');
        $(this).addClass('active');
    });
    
    $(document).on('click',".label_select_size", function () {
        $(".label_select_size").removeClass('active');
        $(this).addClass('active');
    });

    /*-------------------
		Scroll
	--------------------- */
    $(".nice-scroll").niceScroll({
        cursorcolor: "#0d0d0d",
        cursorwidth: "5px",
        background: "#e5e5e5",
        cursorborder: "",
        autohidemode: true,
        horizrailenabled: false
    });

    /*------------------
        CountDown
    --------------------*/
    // For demo preview start
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    if(mm == 12) {
        mm = '01';
        yyyy = yyyy + 1;
    } else {
        mm = parseInt(mm) + 1;
        mm = String(mm).padStart(2, '0');
    }
    var timerdate = mm + '/' + dd + '/' + yyyy;
    // For demo preview end


    // Uncomment below and use your date //

    /* var timerdate = "2020/12/30" */

    $("#countdown").countdown(timerdate, function (event) {
        $(this).html(event.strftime("<div class='cd-item'><span>%D</span> <p>Days</p> </div>" + "<div class='cd-item'><span>%H</span> <p>Hours</p> </div>" + "<div class='cd-item'><span>%M</span> <p>Minutes</p> </div>" + "<div class='cd-item'><span>%S</span> <p>Seconds</p> </div>"));
    });

    /*------------------
		Magnific
	--------------------*/
    $('.video-popup').magnificPopup({
        type: 'iframe'
    });

    /*-------------------
		Quantity change
	--------------------- */
    var proQty = $('.pro-qty');
    proQty.prepend('<span class="fa fa-angle-up dec qtybtn"></span>');
    proQty.append('<span class="fa fa-angle-down inc qtybtn"></span>');
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) - 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) + 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });

    var proQty = $('.pro-qty-2');
    proQty.prepend('<span class="fa fa-angle-left dec qtybtn"></span>');
    proQty.append('<span class="fa fa-angle-right inc qtybtn"></span>');
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });

    /*------------------
        Achieve Counter
    --------------------*/
    $('.cn_num').each(function () {
        $(this).prop('Counter', 0).animate({
            Counter: $(this).text()
        }, {
            duration: 4000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

})(jQuery);

function setTabsHeader(tab) {
    $('.tabsheader').each(function() {
        var t = $(this).attr('data-web');
        $(this).removeClass("active");
        if (tab == t){
            $(this).addClass("active");
        }
    });
}
//kiểm tra form đăng ký
$(document).ready(function() {
    var delayTimer;
    function kiemTraMatKhau() {
        var matKhau = $('#password').val();
        var nhapLaiMatKhau = $('#repassword').val();
        if (matKhau.length === 0){
            $('#txt_error_mk').text('Vui lòng nhập mật khẩu.');
            return false;
        }else{
            $('#txt_error_mk2').remove();
            $('#txt_error_mk').text('');
        }
        if (matKhau === nhapLaiMatKhau && matKhau !== '') {
            // Báo trùng khớp
            $('#txt_nhạp_lai_mk').removeClass("text-danger").addClass("text-success").text('Mật khẩu trùng khớp.');
            return true;
        } else {
            // Báo không trùng khớp
            $('#txt_nhạp_lai_mk').removeClass("text-success").addClass("text-danger").text('Mật khẩu không trùng khớp.');
            return false;
        }
    }
    function kiemTraSDT() {
        var sdt = $('#sdt').val();
        var regexSDT =/^(84|0|\+84)\d{9}$/;
        if (sdt.length===0) {
            $('#txt_error_sdt').text('Vui lòng nhập số điện thoại.');
            return false;
        }else if (!regexSDT.test(sdt)) {
            $('#txt_error_sdt').text('Số điện thoại không đúng định dạng.');
            return false;
        }else{
            $('#txt_error_sdt').text('');
        }
        return true;
    }
    function kiemTraHo() {
        var ho = $('#ho').val();
        if (ho.length===0) {
            $('#txt_error_ho').text('Vui lòng nhập họ.');
            return false;
        }else{
            $('#txt_error_ho').text('');
        }
        return true;
    }
    function kiemTraTenDem() {
        var ho = $('#tenDem').val();
        if (ho.length===0) {
            $('#txt_error_tendem').text('Vui lòng nhập tên đệm.');
            return false;
        }else{
            $('#txt_error_tendem').text('');
        }
        return true;
    }
    function kiemTraTen() {
        var ho = $('#ten').val();
        if (ho.length===0) {
            $('#txt_error_ten').text('Vui lòng nhập tên.');
            return false;
        }else{
            $('#txt_error_ten').text('');
        }
        return true;
    }
    function kiemTraEmail() {
        var email = $('#email').val();
        var regexEmail = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
        if (email.length===0) {
            $('#txt_error_email').text('Vui lòng nhập email.');
            return false;
        }else if (!regexEmail.test(email)) {
            $('#txt_error_email').text('Email không đúng định dạng.');
            return false;
        }else{
            $('#txt_error_email').text('');
        }
        return true;
    }
    function kiemTraNgaySinh() {
        var ngaySinh = $('#ngaySinh').val();
        console.log(ngaySinh)
        var regexNgaySinh =/^(19|20)\d\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$/;
        if (ngaySinh.length === 0) {
            $('#txt_error_ngaysinh').text('Vui lòng chọn ngày sinh.');
            return false;
        } else if (!regexNgaySinh.test(ngaySinh)) {
            $('#txt_error_ngaysinh').text('Ngày sinh không đúng định dạng.');
            return false;
        } else {
            $('#txt_error_ngaysinh').text('');
            return true;
        }
    }

    function kiemTraGioiTinh() {
        if ($('input[name="gioiTinh"]:checked').length > 0) {
            $('#txt_error_gioitinh').text('');
            return true;
        } else {
            $('#txt_error_gioitinh').text('Vui lòng chọn giới tính.');
            return false;
        }
    }

// Thêm sự kiện kiểm tra khi người dùng thay đổi lựa chọn giới tính
    $('input[name="gioiTinh"]').on('change', function() {
        kiemTraGioiTinh();
    });

// Thêm sự kiện kiểm tra khi người dùng nhập vào trường ngày sinh
    $('#ngaySinh').on('change', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraNgaySinh, 300); // Độ trễ 300 mili giây
    });
    $('#ho').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraHo, 300); // Độ trễ 300 mili giây
    });
    $('#tenDem').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraTenDem, 300); // Độ trễ 300 mili giây
    });
    $('#ten').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraTen, 300); // Độ trễ 300 mili giây
    });
    $('#email').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraEmail, 300); // Độ trễ 300 mili giây
    });
    $('#sdt').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraSDT, 300); // Độ trễ 300 mili giây
    });
    $('#password, #repassword').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(kiemTraMatKhau, 300); // Độ trễ 300 mili giây
    });
    $('#form-register').on('submit', function(e) {
        if (!kiemTraHo()||!kiemTraTenDem()||!kiemTraTen()||!kiemTraNgaySinh() || !kiemTraGioiTinh()|| !kiemTraEmail() || !kiemTraSDT() || !kiemTraMatKhau() ) {
            e.preventDefault(); // Ngăn không cho form được submit
        }
    });
});




//kiểm tra email , password đăng nhập
$(document).ready(function() {
    var delayTimer;
    $('#email').on('keyup', function() {
        clearTimeout(delayTimer);
        delayTimer = setTimeout(function() {
            var email = $('#email').val();
            var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (emailRegex.test(email)) {
                // Nếu email đúng định dạng, không hiển thị gì
                $('#text_error_email').text('');
            } else {
                // Nếu email không đúng định dạng, hiển thị thông báo
                $('#text_error_email').text('Email không đúng định dạng.');
            }
        }, 300); // Độ trễ 300 mili giây
    });
});

$(document).ready(function() {
    $('#form-login').on('submit', function(e) {
        var email = $('#email').val();
        var password = $('#password').val();
        var errorMessages = true;
        if (email === '') {
            $('#text_error_email').text('Email không được để trống !')
            errorMessages = false;
        }
        if (password === '') {
            $('#txt_error_password').text('Mật khẩu không được để trống !')
            errorMessages = false;
        }
        if (errorMessages===false) {
            e.preventDefault(); // Dừng submit form
        }
    });
});
