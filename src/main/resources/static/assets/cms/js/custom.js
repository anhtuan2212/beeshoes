$(document).on('ready', function () {
    $('#btn-add-new-product').on('click',()=>{
        var ten = $('#product-name-modal').val();
        if (ten.length===0){
            alert("Vui lòng nhập tên !");
        }else{
            $.ajax({
                type:"POST",
                url:"/api/add-product",
                data:{
                    ten:ten
                },
                success: (data,status,xhr)=>{
                    var html =`<option value="${data.id}" selected>${data.ten}</option>`;
                    $('#sanPham').append(html);
                    alert('Thêm Thành Công !');
                    if (confirm('Bạn có muốn tạo thêm !')){
                        $('#product-name-modal').val('');
                    }else{
                    $('#add_new_product_modal').modal('hide');
                    $('#product-name-modal').val('');
                    }
                },
                error:()=>{
                    alert("lỗi !")
                }
            })
        }
    })


    //ADD NEW CATEGORY
    $('#btn-add-new-category').on('click',()=>{
        var ten = $('#category-name-modal').val();
        if (ten.length=== 0 ){
            alert("Vui lòng nhập tên !");
        }else{
            $.ajax({
                type:"POST",
                url:"/api/add-category",
                data:{
                    ten:ten
                },
                success: (data,status,xhr)=>{
                    var html =`<option value="${data.id}" selected>${data.tenTheLoai}</option>`;
                    $('#theLoai').append(html);
                    alert('Thêm Thành Công !');
                    if (confirm('Bạn có muốn tạo thêm !')){
                        $('#category-name-modal').val('');
                    }else{
                        $('#add_new_category').modal('hide');
                        $('#category-name-modal').val('');
                    }
                },
                error:()=>{
                    alert("lỗi !")
                }
            })
        }
    })
})