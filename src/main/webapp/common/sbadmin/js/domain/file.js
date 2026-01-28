
// main.js
window.file = {
    init() {
    },
    uploadFile(referenceType, referenceId, fileEl) {

        //const form = document.getElementById(formNm);
        //const data = Object.fromEntries(new FormData(form).entries());


        const fileInput = document.getElementById(fileEl);
        const file = fileInput.files[0];

        if (!file) {
            alert("파일을 선택하세요.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("referenceType", referenceType);
        formData.append("referenceId", referenceId);

        fetch("/api/files/upload", {
            method: "POST",
            headers: {
                //"Authorization": "Bearer YOUR_JWT_TOKEN"
                // ⚠️ Content-Type 직접 지정하면 안 됨
            },
            body: formData
        })
            .then(res => res.json())
            .then(data => {
                console.log(data);
                //alert(data.message);
            })
            .catch(err => {
                console.error(err);
                //alert("업로드 실패");
            });
    }

};