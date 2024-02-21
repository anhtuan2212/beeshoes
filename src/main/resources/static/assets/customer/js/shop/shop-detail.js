$(document).ready(function () {
    $('.select_color').on('change',function () {
        $('.select_color').closest('.label_select_color').removeClass('activeSelected')
        $(this).closest('.label_select_color').addClass('activeSelected');
    })
})