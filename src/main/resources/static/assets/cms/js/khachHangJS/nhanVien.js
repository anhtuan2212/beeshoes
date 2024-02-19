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

function validate() {
    var result = true;
    var hoTen = document.getElementById("hoTen").value;
    var sdt = document.getElementById("sdt").value;
    var cccd = document.getElementById("cccd").value;
    var ngaySinh = document.getElementById("ngaySinh").value;
    var soNha = document.getElementById("soNha").value;
    var phuongXa = document.getElementById("phuongXa").value;
    var quanHuyen = document.getElementById("quanHuyen").value;
    var tinhTP = document.getElementById("tinhTP").value;
    if (hoTen.length == 0) {
        document.getElementById("hoTen_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("hoTen_emty").style.display = "none";
    }
    if (sdt.length == 0) {
        document.getElementById("sdt_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("sdt_emty").style.display = "none";
    }
    if (cccd.length == 0) {
        document.getElementById("cccd_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("cccd_emty").style.display = "none";
    }
    if (ngaySinh.length == "") {
        document.getElementById("ngaySinh_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("ngaySinh_emty").style.display = "none";
    }
    if (soNha.length == 0) {
        document.getElementById("soNha_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("soNha_emty").style.display = "none";
    }
    if (District.length == "") {
        document.getElementById("quanHuyen_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("quanHuyen_emty").style.display = "none";
    }
    if (Ward.length == "") {
        document.getElementById("phuongXa_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("phuongXa_emty").style.display = "none";
    }
    if (Province.length == "") {
        document.getElementById("tinhTP_emty").style.display = "block";
        result = false;
    } else {
        document.getElementById("tinhTP_emty").style.display = "none";
    }
    return result;
}

//================================
//quét QR
$('#form-qr-code').on('shown.bs.modal', function () {
    startQRCodeScanner();
});

function startQRCodeScanner() {
    let video = document.getElementById("video_show_camera");
    let canvas = document.getElementById("canvas_context");
    let context = canvas.getContext("2d");
    let beepAudio = document.getElementById("sound_beep");

    if (navigator.mediaDevices.getUserMedia) {
        navigator.mediaDevices
            .getUserMedia({video: true})
            .then(function (stream) {
                video.srcObject = stream;
                setTimeout(() => {
                    requestAnimationFrame(tick);
                }, 500);
            })
            .catch(function (error) {
                console.log("Something went wrong");
            });
    }

    function playBeepSound() {
        // Phát âm thanh beep
        beepAudio.play();
    }

    $('#form-qr-code').on('hidden.bs.modal', function () {
        closeCamera();
    });

    function closeCamera() {
        // Dừng camera
        var tracks = video.srcObject.getTracks();
        tracks.forEach(track => track.stop());
    }

    function tick() {
        if (video.readyState === video.HAVE_ENOUGH_DATA) {
            canvas.hidden = false;
            context.drawImage(video, 0, 0, canvas.width, canvas.height);
            var imageData = context.getImageData(
                0,
                0,
                canvas.width,
                canvas.height
            );
            var code = jsQR(imageData.data, imageData.width, imageData.height, {
                inversionAttempts: "dontInvert",
            });
            if (code) {
                var data = code.data;
                result = data.split("|");
                console.log(result);
                playBeepSound();
                closeCamera();
                $('#hoTen').val(result[2])
                $('#cccd').val(result[0]);
                //nếu là nam thì Set the value of the radio button with id="maleRadio" to "male"
                if (result[4] == 'Nam') {
                    $('#gioi_tinh_nam').prop('checked', true)
                } else {
                    $('#gioi_tinh_nu').prop('checked', true)
                }
                let str = result[3];
                let formattedDate = str.substring(4) + '-' + str.substring(2, 4) + '-' + str.substring(0, 2);
                $('#ngaySinh').val(formattedDate);
                $('#form-qr-code').modal('hide');
            }
        }
        requestAnimationFrame(tick);
    }
}

//==================
//api địa chỉ
const provice_url = "https://api.npoint.io/ac646cb54b295b9555be";
const district_url = "https://api.npoint.io/34608ea16bebc5cffd42";
const ward_url = "https://api.npoint.io/dd278dc276e65c68cdf5";

var province_list = [];
var district_list = [];
var ward_list = [];


//===========================
$(document).on('ready', function () {
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
        handleData(data, $('#Province'), 'Id', 'Name');
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
    $('#Province').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let dis = $('#District');
        $('#Ward').html('')
        handleData(district_list.filter(district => district.ProvinceId === parseInt(id)), dis, 'Id', 'Name');
    });

    // Xử lý sự kiện khi thay đổi quận/huyện
    $('#District').on('change', function () {
        let id = $(this).find('option:selected').data('id');
        let war = $('#Ward');
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