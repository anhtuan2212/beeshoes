import {initializeApp} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import {
    getStorage,
    ref,
    uploadBytes,
    getDownloadURL,
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
let ImgStorage = [];

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

document.getElementById('chooseImg').addEventListener("change", function (e) {
    $('#btn-save-img').removeClass('d-none');
    let files = Array.from(e.target.files);
    console.log(files);
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const tempUrl = URL.createObjectURL(file);
        let div = document.createElement('div');
        div.className = "col-6 col-sm-4 col-md-3 mb-3 mb-lg-5";
        div.innerHTML = `
            <div class="card card-sm" style="width: 180px;height: 230px">
                <img class="card-img-top"  width="179" height="179"  src="${tempUrl}" alt="Image Description">
                <div class="card-body">
                    <div class="row text-center">
                        <div class="col-4">
                            <a class="js-fancybox-item text-body" href="javascript:;" data-toggle="tooltip"
                                data-placement="top" title="" data-src="${tempUrl}"
                                data-caption="${file.name}" data-original-title="View">
                                <i class="tio-visible-outlined"></i>
                            </a>
                        </div>
                        <div class="col-4 column-divider ">
                            <input type="radio" class="check-main-radio" name="main-img" data-toggle="tooltip"
                                data-placement="top" title="" data-original-title="Main">
                        </div>
                        <div class="col-4 column-divider">
                            <a class="text-danger btn-delete-img" href="javascript:;" data-toggle="tooltip"
                                data-placement="top" title="" data-original-title="Delete">
                                <i class="tio-delete-outlined"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>`;
        document.getElementById("fancyboxGallery").appendChild(div);
        const deleteButton = div.querySelector(".btn-delete-img");
        deleteButton.addEventListener("click", function () {
            const index = Array.from(div.parentElement.children).indexOf(div);
            if (files.length > 1) {
                files.splice(index, 1);
            } else {
                console.log("vào")
                document.getElementById('chooseImg').value = '';
            }
            div.remove();
            console.log(document.getElementById('chooseImg').files);
        });
    }
});

$(document).on('ready', '#btn-save-img', function () {

   let files = document.getElementById("chooseImg").files;
   let lstURL = [];
   if (files.length ===0){
       ToastError('File rỗng.')
       return;
   }
    for (let i = 0; i < files.length; i++) {
        const storageRef = ref(storage, "images/" + file.name);
        uploadBytes(storageRef, file).then((snapshot) => {
            getDownloadURL(snapshot.ref).then((url) => {
              lstURL.push(url);
            });
        });
    }
    $.ajax({
        url: "",
        type: "POST",
        data: {},
        success: function () {

        },
        error: function () {

        }
    })
})

// document.getElementById('chooseImg').addEventListener("change",function (e) {
//
//     const files = e.target.files;
//     for (let i = 0; i < files.length; i++) {
//         const file = renameFile(files[i]);
//         const tempUrl = URL.createObjectURL(file);
//         var itemRef = null;
//         var div = document.createElement('div');
//         div.className ="col-6 col-sm-4 col-md-3 mb-3 mb-lg-5"
//         div.innerHTML = `
//                     <div class="card card-sm" style="width: 180px;height: 230px">
//                         <img class="card-img-top"  width="179" height="179"  src="${tempUrl}" alt="Image Description">
//                         <div class="card-body">
//                             <div class="row text-center">
//                                 <div class="col-4">
//                                     <a class="js-fancybox-item text-body" href="javascript:;" data-toggle="tooltip"
//                                        data-placement="top" title="" data-src="${tempUrl}"
//                                        data-caption="${file.name}" data-original-title="View">
//                                         <i class="tio-visible-outlined"></i>
//                                     </a>
//                                 </div>
//                                  <div class="col-4 column-divider ">
//                                     <input type="radio" class="check-main-radio" name="main-img" data-toggle="tooltip"
//                                            data-placement="top" title="" data-original-title="Main">
//                                 </div>
//                                 <div class="col-4 column-divider">
//                                     <a class="text-danger btn-delete-img" href="javascript:;" data-toggle="tooltip"
//                                        data-placement="top" title="" data-original-title="Delete">
//                                         <i class="tio-delete-outlined"></i>
//                                     </a>
//                                 </div>
//                             </div>
//                         </div>
//                     </div>`;
//         document.getElementById("fancyboxGallery").appendChild(div);
//         const storageRef = ref(storage, "images/" + file.name);
//         uploadBytes(storageRef, file).then((snapshot) => {
//             itemRef = snapshot.ref;
//             getDownloadURL(snapshot.ref).then((url) => {
//                 // Cập nhật src của ảnh và data-src của link xem với URL đã upload
//                 var img = div.querySelector('.card-img-top');
//                 img.src = url;
//                 var viewLink = div.querySelector('.js-fancybox-item');
//                 viewLink.dataset.src = url;
//                 let oj = {url:url,ref:storageRef}
//                 ImgStorage.push(oj)
//                 console.log(ImgStorage)
//             });
//         });
//         var deleteBtn = div.querySelector('.btn-delete-img');
//         deleteBtn.addEventListener('click', function () {
//             if (confirm("Bạn có chắc chắn muốn xóa?")) {
//                 deleteObject(itemRef)
//                     .then(() => {
//                         console.log("Deleted", itemRef.name);
//                         div.remove(); // Xóa div chứa hình ảnh
//                     })
//                     .catch((error) => {
//                         console.error(error);
//                     });
//             }
//         });
//     }
// })
