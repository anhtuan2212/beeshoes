self.onmessage = function (e) {
    let arrHoDon = [];
    $.ajax({
        url: '/api/get-all-san-pham',
        type: 'GET',
        success: function (data) {
            arrHoDon = data;
            self.postMessage(arrHoDon);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error("Lỗi khi gọi API: " + textStatus, errorThrown);
        }
    })
};
