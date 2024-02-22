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

function showLoader() {
    $('#preloder').css('display', 'block').find('.loader').css('display', 'block');
}
function closeLoader() {
    $('#preloder').css('display', 'none').find('.loader').css('display', 'none');
}
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
});
$(document).ready(function () {
    $('.select_color').on('change',function () {
        $('.select_color').closest('.label_select_color').removeClass('activeSelected')
        $(this).closest('.label_select_color').addClass('activeSelected');
    })
    $('#btn-load-more-product').on('click',function () {
        showLoader()
        setTimeout(()=>{
            closeLoader();
        },1000)
    })
})
window.onload = function() {
    ToastSuccess('Tải hoàn tất !')
};
