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
    return (!str || str.length === 0 );
}
function isValidEmail(email) {
    let emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailPattern.test(email);
}
function isValidPhoneNumber(phoneNumber) {
    let phone = /^(0\d{9,10})$/;
    // Kiểm tra xem số điện thoại có bắt đầu bằng số 0 và có đúng 10 số không
    return phone.test(phoneNumber);
}

function formvalidate() {
    var hoTen = $('#hoTen').val();
    var email = $('#email').val();
    var sdt = $('#sdt').val();
    var ngaySinh = $('#ngaySinh').val();

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
    }else if (!isValidPhoneNumber(sdt)) {
        ToastError("Sdt không đúng định dạng(0359xxxxxx)");
        return  false;
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
    return true;
    ToastSuccess("Cập nhật thành công!")
}

//check trùng sdt,email
function checkDuplicateKH1() {
    return new Promise(function (resolve, reject) {
        let email = $('#email').val();
        let phone = $('#sdt').val();
        let id = $('#idKhachHang').val();
        $.ajax({
            url: '/cms/khach-hang/check-duplicate',
            type: 'POST',
            data: {email: email, phoneNumber: phone, id: id},
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

$(document).on('change', 'input.addressDefaultSwitch[name="addressDefault"]', function () {
    let idDiaChi = $(this).val()
    let idKhachHang = $('#idKhachHang').val();
    $.ajax({
        url: '/cms/khach-hang/set-default-address',
        type: 'POST',
        data: {
            idDiaChi: idDiaChi,
            idKhachHang: idKhachHang
        },
        success: function () {
            ToastSuccess("Thành công")
        },
        error: function (xhr) {
            ToastError("Thất bại")
        }
    })
})
let province_list = [];
let district_list = [];
let ward_list = [];
let quan_huyen = $('.quanHuyen').closest("div");
let tinh_TP = $('.tinhTP').closest("div");
let phuong_xa = $('.phuong_xa').closest("div");

function handleData(data, dropdown, idKey, valueKey) {
    dropdown.empty().append(`<option value="">Vui lòng chọn</option>`);
    data.forEach(item => {
        dropdown.append(`<option data-id="${item[idKey]}" value="${item[valueKey]}">${item[valueKey]}</option>`);
    });
}

function fillAllTinh() {
    return new Promise((resolve, reject) => {
        tinh_TP.each((index, element) => {
            let select = $(element).find('select');
            let tinh = $(element).find('input[type="text"]').val();
            console.log(tinh)
            select.append(`<option value="" >Chọn Tỉnh</option>`);
            province_list.forEach(data => {
                select.append(`<option value="${data.ProvinceID}" ${tinh === data.ProvinceName ? 'selected' : ''}>${data.ProvinceName}</option>`);
            });
        });
        resolve();
    });
}

function fillAllHuyen() {
    return new Promise((resolve, reject) => {
        quan_huyen.each((index, element) => {
            let select = $(element).find('select');
            select.html('');
            let name = $(element).find('input[type="text"]').val();
            let idTinh = $(element).closest('.form-address').find('.tinhTP').val();
            select.append(`<option value="" >Chọn huyện</option>`);
            district_list.forEach(data => {
                if (data.ProvinceID == idTinh) {
                    select.append(`<option value="${data.DistrictID}" ${name === data.DistrictName ? 'selected' : ''}>${data.DistrictName}</option>`);
                }
            });
        });
        resolve();
    });
}

function fillAllXa() {
    return new Promise((resolve, reject) => {
        phuong_xa.each((index, element) => {
            let select = $(element).find('select');
            select.html('');
            let name = $(element).find('input[type="text"]').val();
            let idHuyen = $(element).closest('.form-address').find('select.quanHuyen').val();
            select.append(`<option value="" >Chọn xã</option>`);
            ward_list.forEach(data => {
                if (data.DistrictID == idHuyen) {
                    let selected = data.Name == name ? 'selected' : '';
                    select.append(`<option value="${data.DistrictID}" ${selected}>${data.Name}</option>`);
                }
            });
        });
        resolve();
    });
}

fetch('/assets/address-json/province.json')
    .then(response => response.json())
    .then(data => {
        province_list = data;
        console.log(data)
        handleData(province_list, $('#tinhTPMoi'), 'ProvinceID', 'ProvinceName');
        return fillAllTinh();
    })
    .then(() => {
        return fetch('/assets/address-json/district.json')
            .then(response => response.json())
            .then(data => {
                district_list = data;
                return fillAllHuyen();
            });
    })
    .then(() => {
        return fetch('/assets/address-json/ward.json')
            .then(response => response.json())
            .then(data => {
                ward_list = data;
                return fillAllXa();
            });
    })
    .catch(error => console.error('Error:', error));

$(document).on('ready', function () {
    $('#btn-submit').on('click', async function () { // Sử dụng async function để sử dụng await
        let check = true;
        if (formvalidate()) {
            check = await checkDuplicateKH1();
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
        let form = $('#form-data-cus');
        form.submit();

    })

    $('.btn_save_address').on('click', function () {
        let id = $(this).data('id-address');
        let soNha = $(this).closest('.form-address').find('.soNha').val();
        let phuong = $(this).closest('.form-address').find('select.phuong_xa');
        let huyen = $(this).closest('.form-address').find('select.quanHuyen');
        let tinh = $(this).closest('.form-address').find('select.tinhTP');
        if (soNha.length === 0) {
            return ToastError('Vui lòng nhập địa chỉ.')
        }
        if (tinh.val() == null) {
            return ToastError('Vui lòng chọn tỉnh.')
        }
        if (huyen.val() == null) {
            return ToastError('Vui lòng chọn quận huyện.')
        }
        if (phuong.val() == null) {
            return ToastError('Vui lòng chọn phường xã.')
        }
        let selectedPhuong = phuong.find(":selected").text();
        let selectedHuyen = huyen.find(":selected").text();
        let selectedTinh = tinh.find(":selected").text();
        $.ajax({
            url: '/cms/khach-hang/update/update-diachi',
            type: 'POST',
            data: {
                soNhaDto: soNha,
                phuongXaDto: selectedPhuong,
                quanHuyenDto: selectedHuyen,
                tinhThanhPhoDto: selectedTinh,
                id: id
            },
            success: function () {
                ToastSuccess('Lưu thành công.')
            },
            error: function () {
                ToastError('Lưu thất bại.')
            }
        })
    })
    // ONLY DEV
    // =======================================================
    $('.tinhTP').on('change', function () {
        $(this).closest('.form-address').find('select.phuong_xa').html('')
        return fillAllHuyen()
    })
    $('.quanHuyen').on('change', function () {
        return fillAllXa();
    })


    $('.form-address').on('change', function () {
        $(this).find('.btn_save_address').removeClass('d-none');
    })

    // Xử lý sự kiện khi thay đổi tỉnh/thành phố
    $('#tinhTPMoi').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let dis = $('#quanHuyenMoi');
        $('#phuongXaMoi').html('')
        handleData(district_list.filter(district => district.ProvinceID === parseInt(id)), dis, 'DistrictID', 'DistrictName');
    });

    // Xử lý sự kiện khi thay đổi quận/huyện
    $('#quanHuyenMoi').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let war = $('#phuongXaMoi');
        handleData(ward_list.filter(ward => ward.DistrictID === parseInt(id)), war, 'Code', 'Name');
    });


    //====================
    $('#addNewAddress').on('click', function () {
        let soNha = $('#soNhaMoi').val();
        let phuongXa = $('#phuongXaMoi').val();
        let quanHuyen = $('#quanHuyenMoi').val();
        let tinhTP = $('#tinhTPMoi').val();
        let idKhachHang = $('#idKhachHang').val();
        $.ajax({
            url: '/cms/khach-hang/update/add-diachi',
            type: 'POST',
            data: {
                idKH: idKhachHang,
                soNhaDto: soNha,
                phuongXaDto: phuongXa,
                quanHuyenDto: quanHuyen,
                tinhThanhPhoDto: tinhTP
            },
            success: function (data, status, xhr) {
                let html = `<div class="card-body row">
                                                        <div class="col-sm-6">
                                                            <div class="row form-group">
                                                                <label for="soNha" class="col-sm-5 col-form-label input-label">Số nhà</label>
                                                                <div class="col-sm-7">
                                                                    <input class="form-control" name="addressLine1" value="${data.soNha}" placeholder="Your address" aria-label="Your address" value="32A">
                                                                </div>
                                                                <i id="soNha_emty" style="color: red;display: none">Vui lòng nhập vào số nhà</i>
                                                            </div>
                                                            <div class="row form-group">
                                <label for="phuongXa" class="col-sm-5 col-form-label input-label">Phường/Xã</label>
                                <div class="col-sm-7">
                                    <select name="phuong_xa" class="js-select2-custom custom-select phuong_xa"
                                        data-hs-select2-options='{"placeholder": "Chọn Phường Xã...",
                                        "searchInputPlaceholder": "Tìm Phường Xã..."}'>
                                        <option value="${data.phuongXa}">${data.phuongXa}</option>
                                    </select>
                                    <input type="text" id="phuongXa${data.id}" value="${data.phuongXa}" hidden>
                                </div>
                                <i id="phuongXa_emty" style="color: red;display: none">Vui lòng nhập vào phường xã</i>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="row form-group">
                                <label for="quanHuyen" class="col-sm-5 col-form-label input-label">Quận/Huyện</label>
                                <div class="col-sm-7">
                                    <select name="quan_huyen" class="js-select2-custom custom-select quan_huyen"
                                        data-hs-select2-options='{"placeholder": "Chọn Quận Huyện...",
                                        "searchInputPlaceholder": "Tìm Quận Huyện..."}'>
                                        <option value="${data.quanHuyen}">${data.quanHuyen}</option>
                                    </select>
                                    <input type="text" id="quanHuyen${data.id}" value="${data.quanHuyen}" hidden>
                                </div>
                                <i id="quanHuyen_emty" style="color: red;display: none">Vui lòng nhập vào quận huyện</i>
                            </div>
                            <div class="row form-group">
                                <label for="tinhTP" class="col-sm-5 col-form-label input-label">Tỉnh/Thành phố</label>
                                <div class="col-sm-7">
                                    <select name="tinh_tp" class="js-select2-custom custom-select tinh_tp"
                                        data-hs-select2-options='{"placeholder": "Chọn Tỉnh/Thành phố...",
                                        "searchInputPlaceholder": "Tìm Tỉnh/Thành phố..."}'>
                                        <option value="${data.tinhThanhPho}">${data.tinhThanhPho}</option>
                                    </select>
                                    <input type="text" id="tinhTP${data.id}" value="${data.tinhThanhPho}" hidden>
                                </div>
                                <i id="tinhTP_emty" style="color: red;display: none">Vui lòng nhập vào tỉnh thành phố</i>
                            </div>
                        </div>
                         <div class="col-sm-6">
                            <div class="row form-group">
                                <div class="d-flex justify-content-between">
                                    <label class="toggle-switch toggle-switch col-sm-10" for="preferencesSwitch${data.id}">Địa chỉ mặc định
                                        <span class="col-sm-2">
                                                <input type="radio" name="addressDefault" 
                                                class="toggle-switch-input addressDefaultSwitch" 
                                                id="preferencesSwitch${data.id}" value="${data.id}">
                                                <span class="toggle-switch-label ml-auto">
                                                    <span class="toggle-switch-indicator"></span>
                                                </span>
                                            </span>
                                    </label>
                                </div>
                            </div>
                        </div>
                      </div>`;
                $('#listAddress').append(html);
                $('#soNhaMoi').val('');
                $('#tinhTPMoi').empty().append('<option value="">Chọn Tỉnh/Thành phố</option>');
                $('#quanHuyenMoi').empty().append('<option value="">Chọn Quận/Huyện</option>');
                $('#phuongXaMoi').empty().append('<option value="">Chọn Phường/Xã</option>');
                switch (xhr.getResponseHeader('status')) {
                    case "oke":
                        ToastSuccess("Lưu thành công")
                        break;
                    default:
                        ToastError("Thất bại")
                }
            }, error: function (xhr) {
                switch (xhr.getResponseHeader('status')) {
                    case "soNhaNull":
                        ToastError("Số nhà không được Null")
                        break;
                    case "tinhTPNull":
                        ToastError("Tỉnh thành phố không được Null")
                        break;
                    case "quanHuyenNull":
                        ToastError("Quận huyện không được Null")
                        break;
                    case "phuongXaNull":
                        ToastError("Phường xã không được Null")
                        break;
                    case "maxAddress":
                        ToastError("Địa chỉ tối đã là 4")
                        break;
                    case "oke":
                        ToastSuccess("Lưu thành công")
                        break;
                    default:
                        ToastError("Thất bại")
                }
            }
        })
    })

    //===============================


    //================================
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