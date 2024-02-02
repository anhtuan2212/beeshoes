import {initializeApp} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
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
const fileInStorages = [];
const ImgStorage = {};

function generateCard(url, lst) {
    var html = `
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

$(document).ready(function () {
    $('#addVariantsContainer').on('change', '.formAddImg', async function (e) {
        const files = e.target.files;
        let img = $(this).parent().find('.avatar')[0];
        let snip = $(this).parent().find('.spinner-border')[0];
        $(snip).removeClass('d-none');
        $(img).addClass('d-none')
        let del = $(this).parent().find('.btn-del-img')[0];
        const file = renameFile(files[0]);
        const tempUrl = URL.createObjectURL(file);
        img.src = tempUrl;
        const storageRef = ref(storage, `images/${file.name}`);
        try {
            const snapshot = await uploadBytes(storageRef, file);
            const url = await getDownloadURL(snapshot.ref);
            fileInStorages.push(storageRef);
            console.log(storageRef)
            sessionStorage.setItem('fileImg', JSON.stringify(fileInStorages));
            $(this).attr('img-src')
            img.src = url;
            $(snip).addClass('d-none')
            $(img).removeClass('d-none');
        } catch (error) {
            console.error(error);
        }
        $(del).on('click', async function () {
            try {
                await deleteObject(storageRef);
                fileInStorages.splice(0, fileInStorages.indexOf(storageRef));
                sessionStorage.setItem('fileImg', JSON.stringify(fileInStorages));
                console.log("Deleted", storageRef.name);
                img.src = '/assets/cms/img/400x400/img2.jpg';
            } catch (error) {
                console.error(error);
            }
        });
    });
})

async function ClaerImg(files) {
    let promises = files.map(file => {
        return deleteObject(file).then(() => {
            console.log('Deleted', file.name);
        }).catch((error) => {
            console.error(error);
        });
    });
    return Promise.all(promises);
}

window.addEventListener('beforeunload', async function (e) {
    let files = JSON.parse(sessionStorage.getItem('fileImg'));
    let completed = false;
    setTimeout(async () => {
        try {
            await ClaerImg(files);
            completed = true;
        } catch (error) {
            completed = false;
        }
        if (completed) {
            e.returnValue = 'Bạn có chắc chắn muốn rời khỏi trang?';
        } else {
            e.preventDefault();
        }
    }, 100);
});

