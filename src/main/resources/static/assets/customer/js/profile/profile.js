$(document).ready(function() {
    $('#input_avatar').change(function() {
        if (this.files && this.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#avatar_preview').attr('src', e.target.result);
            }
            reader.readAsDataURL(this.files[0]);
        }
        console.log(this.files)
    });
});