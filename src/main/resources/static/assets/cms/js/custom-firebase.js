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
fileInput.addEventListener("change", function (e) {
    const files = e.target.files;
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const tempUrl = URL.createObjectURL(file);
        var div = document.createElement('div');
        var itemRef = null;
        div.className = "col-sm-2 mb-3";
        div.innerHTML = `
            <div class="card card-sm box-shadow-img-file">
                <img class="card-img-top img-product-files" src="${tempUrl}" alt="Image Description">
                <button type="button" class="btn btn-icon btn-sm btn-ghost-secondary position-absolute right-0 delete-btn">
                    <i class="tio-clear tio-lg" aria-hidden="true"></i>
                </button>
                <div class="card-body text-center">
                    <a class="js-fancybox-item text-body" href="javascript:;" data-toggle="tooltip" data-placement="top" title="View"
                       data-src="${tempUrl}"
                       data-caption="Image">
                        <i class="tio-visible-outlined mr-1"></i>Xem
                    </a>
                </div>
            </div>`;
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

function generateCard(url , lst) {
    var html =`
    <div class="card card-sm box-shadow-img-file">
        <img class="card-img-top img-product-files" src="${url}" alt="Image Description">
        <button type="button" class="btn btn-icon btn-sm btn-ghost-secondary position-absolute right-0 delete-btn">
            <i class="tio-clear tio-lg" aria-hidden="true"></i>
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
function backToSelected(url) {
    console.log(url)
}
var ImgSelected=[];
getAllImgFromFirebase(listRef);
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
                        var slt =`<a class="js-fancybox-item text-body dis-selected-img" onclick="backToSelected('${url}')" href="javascript:;">Bỏ</a>`;
                        ImgSelected.push(url);
                        console.log(ImgSelected)
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