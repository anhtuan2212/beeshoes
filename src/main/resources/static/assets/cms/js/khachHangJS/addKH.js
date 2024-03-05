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

function formvalidate() {
    var hoTen = $('#hoTen').val();
    var email = $('#email').val();
    var sdt = $('#sdt').val();
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

    // Kiểm tra ngày sinh phải trước ngày hiện tại
    var ngaySinhDate = new Date(ngaySinh);
    var ngayHienTai = new Date();
    if (ngaySinhDate >= ngayHienTai) {
        ToastError("Ngày sinh phải trước ngày hiện tại.");
        return false;
    } else if (isEmpty(ngaySinh)) {
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
    return true;
    ToastSuccess("Lưu hành công")
}

//check trùng sdt,email
function checkDuplicateKH() {
    return new Promise(function (resolve, reject) {
        let email = $('#email').val();
        let phone = $('#sdt').val();
        $.ajax({
            url: '/cms/khach-hang/check-duplicate',
            type: 'POST',
            data: {email: email, phoneNumber: phone},
            success: function (response) {
                if (response.email) {
                    ToastError('Email đã tồn tại.');
                }
                if (response.phoneNumber) {
                    ToastError('Số điện thoại đã tồn tại.');
                }
                resolve(response);
            },
            error: function (xhr, status, error) {
                reject(error);
            }
        });
    });
}


//api địa chỉ
const provice_url = "https://api.npoint.io/ac646cb54b295b9555be";
const district_url = "https://api.npoint.io/34608ea16bebc5cffd42";
const ward_url = "https://api.npoint.io/dd278dc276e65c68cdf5";

var province_list = [];
var district_list = [];
var ward_list = [];
$(document).on('ready', function () {
    $('#submitBtn').on('click', async function () { // Sử dụng async function để sử dụng await
        let check = true;
        if (formvalidate()) {
            check = await checkDuplicateKH();
        } else {
            return;
        }
        if (check.email) {
            console.log(check)
            return;
        }
        if (check.sdt) {
            console.log(check)
            return;
        }
        let form = $('#btn-submit-khach-hang');
        form.submit();
    })


    // Hàm để gọi API và xử lý dữ liệu trả về
    function fetchData(url, successCallback) {
        $.ajax({
            url: url,
            type: "GET",
            success: successCallback
        });
    }

    // Hàm xử lý dữ liệu của các tỉnh/thành phố, quận/huyện, và phường/xã
    function handleData(data, dropdown, idKey, valueKey) {
        dropdown.html('');
        dropdown.append(`<option value="">Vui lòng chọn</option>`);
        data.forEach(function (item) {
            dropdown.append(`<option data-id="${item[idKey]}" value="${item[valueKey]}">${item[valueKey]}</option>`);
        });
    }

    // Gọi API và xử lý dữ liệu cho các tỉnh/thành phố
    fetchData(provice_url, function (data) {
        province_list = data;
        handleData(data, $('#tinhTP'), 'Id', 'Name');
    });

    // Gọi API và xử lý dữ liệu cho các quận/huyện
    fetchData(district_url, function (data) {
        district_list = data;
    });

    // Gọi API và xử lý dữ liệu cho các phường/xã
    fetchData(ward_url, function (data) {
        ward_list = data;
    });

    // Xử lý sự kiện khi thay đổi tỉnh/thành phố
    $('#tinhTP').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let dis = $('#quanHuyen');
        $('#phuongXa').html('')
        handleData(district_list.filter(district => district.ProvinceId === parseInt(id)), dis, 'Id', 'Name');
    });

    // Xử lý sự kiện khi thay đổi quận/huyện
    $('#quanHuyen').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let war = $('#phuongXa');
        handleData(ward_list.filter(ward => ward.DistrictId === parseInt(id)), war, 'Id', 'Name');
    });

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