$(document).ready(function () {
    $('#avatar_selected').change(function () {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#show_avatar_slected').attr('src', e.target.result);
            }
            reader.readAsDataURL(this.files[0]);
        }
        console.log(this.files)
    });
});

function activeEdit(id) {
    let ele = $('#' + id);
    let btnSave = $('#btn-save')
    ele.removeClass('d-none')
    ele.closest('.form_data').find('label').addClass('d-none')
    if (btnSave.hasClass('d-none')) {
        btnSave.removeClass('d-none');
    }
}