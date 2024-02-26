import {initializeApp} from "https://www.gstatic.com/firebasejs/10.7.1/firebase-app.js";
import {
    getStorage,
    ref,
    uploadBytes,
    getDownloadURL
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

function randomString(n) {
    let chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let result = "";
    for (var i = 0; i < n; i++) {
        result += chars[Math.floor(Math.random() * chars.length)];
    }
    return result;
}

function renameFile(file) {
    let name = file.name;
    let dotIndex = name.lastIndexOf(".");
    let ext = name.substring(dotIndex);
    let newName = randomString(10) + ext;
    return new File([file], newName, {type: file.type});
}

$(document).ready(function () {
    $('#submitBtn').on('click', async function () { // Sử dụng async function để sử dụng await
        let check = true;
        if (validate()) {
            check = await checkDuplicate();
        }else{
            return;
        }
        if (check) {
            console.log(check)
            return;
        }
        let urlImg = $('#url-avatar-user').val();
        let form = $('#btn-submit-nhan-vien');
        if (urlImg === undefined) {
            let input = document.getElementById('avatarUploader');
            let img = $('#avatarImg');
            const file = renameFile(input.files[0]);
            console.log(file)
            const storageRef = ref(storage, `avatars/${file.name}`);
            try {
                const snapshot = await uploadBytes(storageRef, file);
                console.log(snapshot)
                const url = await getDownloadURL(snapshot.ref);
                img.attr('src', url);
                form.append(`<input id="url-avatar-user" type="hidden" name="avatar" value="${url}">`);
                form.submit();
            } catch (error) {
                console.error(error);
            }
        } else {
            form.submit();
        }
    })
})

