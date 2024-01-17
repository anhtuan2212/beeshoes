import { initializeApp } from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import {
    getStorage,
    ref,
    uploadBytes,
    getDownloadURL,
    listAll,
    deleteObject,
} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-storage.js";
const firebaseConfig = {
    apiKey: "AIzaSyAfK7SCIGsZsPcW3Gd3yZsITl2sZw1jBxY",
    authDomain: "datn-lightbe.firebaseapp.com",
    databaseURL:
        "https://datn-lightbe-default-rtdb.asia-southeast1.firebasedatabase.app",
    projectId: "datn-lightbe",
    storageBucket: "datn-lightbe.appspot.com",
    messagingSenderId: "398434574666",
    appId: "1:398434574666:web:40ab20f1a11cfbad5521b3",
    measurementId: "G-B520DWQ9L0",
};
const app = initializeApp(firebaseConfig);
const storage = getStorage(app);
const fileInput = document.getElementById("fileAttachmentBtn");
const listRef = ref(storage, "images");



function generateCard(url , lst) {
    var html =`
    <div class="card card-sm box-shadow-img-file" data-img-src="${url}">
        <img class="card-img-top img-product-files" src="${url}" alt="Image Description">
        <button type="button" class="btn btn-icon btn-sm btn-ghost-secondary position-absolute right-0 delete-btn">
            <i class="tio-delete-outlined"></i>
        </button>
        <div class="card-body text-center row">
            <div class="col-sm-6 p-0">
                <a class="js-fancybox-item text-body" href="javascript:;" data-toggle="tooltip" data-placement="top" title="View"
                    data-src="${url}" data-caption="Image">
                    <i class="tio-visible-outlined mr-1"></i>
                </a>
            </div>
            <div class="col-sm-6 p-0">${lst}</div>
       </div>
    </div>`;
    return html;
}
function randomString(n) {
    var chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var result = "";
    for (var i = 0; i < n; i++) {
        result += chars[Math.floor(Math.random() * chars.length)];
    }
    return result;
}
function renameFile(file) {
    var name = file.name; // lấy tên file
    var dotIndex = name.lastIndexOf("."); // lấy vị trí dấu chấm cuối cùng
    var ext = name.substring(dotIndex); // lấy đuôi file
    var newName = randomString(10) + ext; // tạo tên file mới bằng cách thêm chuỗi ngẫu nhiên vào trước đuôi file
    return new File([file], newName, {type: file.type}); // trả về file mới có tên mới
}
fileInput.addEventListener("change", function (e) {
    const files = e.target.files;
    for (let i = 0; i < files.length; i++) {
        const file = renameFile(files[i]);
        const tempUrl = URL.createObjectURL(file);
        var div = document.createElement('div');
        var itemRef = null;
        var slt =`<a class="js-fancybox-item text-body selected-img" href="javascript:;">Chọn</a>`;
        div.className = "col-sm-2 mb-3";
        div.innerHTML = generateCard(tempUrl,slt);
        document.getElementById("anh_co_san").appendChild(div);
        const storageRef = ref(storage, "images/" + file.name);
        uploadBytes(storageRef, file).then((snapshot) => {
            itemRef = snapshot.ref;
            getDownloadURL(snapshot.ref).then((url) => {
                // Cập nhật src của ảnh và data-src của link xem với URL đã upload
                var img = div.querySelector('.img-product-files');
                img.src = url;
                var viewLink = div.querySelector('.js-fancybox-item');
                viewLink.dataset.src = url;
            });
        });
        var deleteBtn = div.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', function () {
            if (confirm("Bạn có chắc chắn muốn xóa?")) {
                deleteObject(itemRef)
                    .then(() => {
                        console.log("Deleted", itemRef.name);
                        div.remove(); // Xóa div chứa hình ảnh
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            }
        });
    }
});


var ImgSelected =[];
getAllImgFromFirebase(listRef);
document.getElementById('btn-reset-img').addEventListener('click',function () {
    document.getElementById("anh_co_san").innerHTML='';
    getAllImgFromFirebase(listRef);
})
function getAllImgFromFirebase(listRef) {
    listAll(listRef)
        .then((res) => {
            res.items.forEach((itemRef) => {
                getDownloadURL(itemRef).then((url) => {
                    var div = document.createElement('div');
                    var slt =`<a class="js-fancybox-item text-body selected-img" href="javascript:;">Chọn</a>`;
                    div.className = "col-sm-2 mb-3 ";
                    div.innerHTML = generateCard(url,slt);
                    document.getElementById("anh_co_san").appendChild(div);
                    var deleteBtn = div.querySelector('.delete-btn');
                    deleteBtn.addEventListener('click', function () {
                        if (confirm("Bạn có chắc chắn muốn xóa?")) {
                            deleteObject(itemRef)
                                .then(() => {
                                    console.log("Deleted", itemRef.name);
                                    div.remove(); // Xóa div chứa hình ảnh
                                })
                                .catch((error) => {
                                    console.error(error);
                                });
                        }
                    });
                    var selectedImg = div.querySelector('.selected-img');
                    selectedImg.addEventListener('click',function () {
                        var slt =`<a class="js-fancybox-item text-body dis-selected-img" onclick="backToSelected('${url}',this)" href="javascript:;">Bỏ</a>`;
                        ImgSelected.push(url);
                        div.innerHTML = generateCard(url,slt);
                        document.getElementById("anh_duoc_chon").appendChild(div);
                    });
                });
            });
        })
        .catch((error) => {
            console.error(error);
        });
}
$(document).on('ready', function () {
    $('#btn-accept-select-img').on('click',function () {
        ImgSelected.forEach((url)=>{
            var div = document.createElement('div');
            div.className = "col-6 col-sm-4 col-md-3 mb-3 mb-lg-5";
            div.innerHTML = `
        <div class="card card-sm">
            <img class="card-img-top img-product-files-extra" src="${url}"
                 alt="Image Description">
            <div class="card-body">
                <div class="row text-center">
                    <div class="col">
                        <a class="js-fancybox-item text-body" href="javascript:;"
                           data-toggle="tooltip" data-placement="top" title="View"
                           data-src="${url}"
                           data-caption="Image #01">
                            <i class="tio-visible-outlined"></i>
                        </a>
                    </div>
                    <div class="col column-divider">
                        <a class="text-danger btn-delete-img" href="javascript:;" data-toggle="tooltip"
                           data-placement="top" title="Delete">
                            <i class="tio-delete-outlined"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>`;
            document.getElementById("img-selected").appendChild(div);
            var Img = div.querySelector('.btn-delete-img');
            var src = div.querySelector('.img-product-files-extra').getAttribute('src');
            Img.addEventListener('click',function () {
                ImgSelected.splice(1,ImgSelected.indexOf(src))
                console.log(ImgSelected)
                div.remove()
            })
        })
    })
})