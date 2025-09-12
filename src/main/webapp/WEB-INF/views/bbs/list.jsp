<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .form-container { max-width: 800px; margin: 0 auto; padding: 20px; }
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
    .form-control { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; }
    textarea.form-control { min-height: 200px; resize: vertical; }
    .upload-section { border: 1px solid #ddd; border-radius: 8px; padding: 20px; margin: 20px 0; }
    .upload-area { border: 2px dashed #ccc; border-radius: 8px; padding: 40px; text-align: center; cursor: pointer; transition: all 0.3s; }
    .upload-area:hover, .upload-area.dragover { border-color: #007bff; background-color: #f8f9fa; }
    .file-item { display: flex; align-items: center; justify-content: space-between; padding: 10px; border: 1px solid #eee; border-radius: 4px; margin: 5px 0; }
    .file-info { display: flex; align-items: center; flex-grow: 1; }
    .file-info i { margin-right: 10px; font-size: 18px; color: #6c757d; }
    .btn { padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; margin: 5px; }
    .btn-primary { background-color: #007bff; color: white; }
    .btn-success { background-color: #28a745; color: white; }
    .btn-danger { background-color: #dc3545; color: white; }
    .btn-secondary { background-color: #6c757d; color: white; }
    .progress-bar { width: 100%; height: 6px; background-color: #f0f0f0; border-radius: 3px; overflow: hidden; margin: 10px 0; display: none; }
    .progress-fill { height: 100%; background-color: #007bff; transition: width 0.3s; }
    .alert { padding: 15px; margin: 10px 0; border-radius: 4px; }
    .alert-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
    .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
</style>

<main>

    <div class="form-container">
        <h2>게시글 작성</h2>

        <form id="bbsForm" action="/m/bbs/save" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="title">제목 <span style="color: red;">*</span></label>
                <input type="text" id="title" name="title" class="form-control" required>
            </div>

            <div class="form-group">
                <label for="content">내용 <span style="color: red;">*</span></label>
                <textarea id="content" name="content" class="form-control" required></textarea>
            </div>

            <!-- 파일 업로드 섹션 -->
            <div class="upload-section">
                <h4>첨부파일</h4>
                <div class="upload-area" id="uploadArea">
                    <i class="fas fa-cloud-upload-alt" style="font-size: 48px; color: #ccc; margin-bottom: 10px;"></i>
                    <p>파일을 여기로 드래그하거나 클릭하여 선택하세요</p>
                    <input type="file" id="fileInput" name="files" multiple style="display: none;">
                    <button type="button" class="btn btn-primary" onclick="$('#fileInput').click()">파일 선택</button>
                </div>

                <div class="progress-bar" id="progressBar">
                    <div class="progress-fill" id="progressFill"></div>
                </div>

                <div id="messageArea"></div>

                <!-- 선택된 파일 목록 -->
                <div id="selectedFilesList"></div>
            </div>

            <div class="form-group" style="text-align: center;">
                <button type="submit" class="btn btn-success">
                    <i class="fas fa-save"></i> 저장
                </button>
                <button type="button" class="btn btn-secondary" onclick="history.back()">
                    <i class="fas fa-times"></i> 취소
                </button>
            </div>
        </form>
    </div>
</main>
<script type="text/javascript">
    let selectedFiles = [];
    const maxFileSize = 50 * 1024 * 1024; // 50MB
    const allowedExtensions = ['jpg', 'jpeg', 'png', 'gif', 'pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'txt', 'hwp', 'zip'];

    $(document).ready(function() {
        setupDragDrop();
        setupFileInput();
    });

    setupDragDrop = () => {
        const uploadArea = $('#uploadArea')[0];

        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            uploadArea.addEventListener(eventName, preventDefaults, false);
        });

        ['dragenter', 'dragover'].forEach(eventName => {
            uploadArea.addEventListener(eventName, () => {
                $('#uploadArea').addClass('dragover');
            }, false);
        });

        ['dragleave', 'drop'].forEach(eventName => {
            uploadArea.addEventListener(eventName, () => {
                $('#uploadArea').removeClass('dragover');
            }, false);
        });

        uploadArea.addEventListener('drop', handleDrop, false);
    };

    function setupFileInput() {
        $('#fileInput').on('change', function(e) {
            handleFiles(Array.from(e.target.files));
        });
    }

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    function handleDrop(e) {
        const files = Array.from(e.dataTransfer.files);
        handleFiles(files);
    }

    function handleFiles(files) {
        files.forEach(file => {
            if (validateFile(file)) {
                selectedFiles.push(file);
            }
        });

        updateFileInput();
        renderSelectedFiles();
    }

    function validateFile(file) {
        // 파일 크기 검증
        if (file.size > maxFileSize) {
            showMessage(`\${file.name}: 파일 크기가 제한을 초과했습니다. (최대: \${formatFileSize(maxFileSize)})`, 'error');
            return false;
        }

        // 확장자 검증
        const extension = file.name.split('.').pop().toLowerCase();
        if (!allowedExtensions.includes(extension)) {
            showMessage(`\${file.name}: 허용되지 않은 파일 형식입니다. (\${extension})`, 'error');
            return false;
        }

        return true;
    }

    function updateFileInput() {
        const dataTransfer = new DataTransfer();
        selectedFiles.forEach(file => dataTransfer.items.add(file));
        $('#fileInput')[0].files = dataTransfer.files;
    }

    function renderSelectedFiles() {
        const container = $('#selectedFilesList');
        container.empty();

        if (selectedFiles.length === 0) {
            return;
        }

        container.append('<h5>선택된 파일 (' + selectedFiles.length + '개)</h5>');

        selectedFiles.forEach((file, index) => {
            const fileItem = $(`
                    <div class="file-item">
                        <div class="file-info">
                            <i class="\${getFileIcon(file.name)}"></i>
                            <div>
                                <div><strong>\${file.name}</strong></div>
                                <small>\${formatFileSize(file.size)}</small>
                            </div>
                        </div>
                        <div>
                            <button type="button" class="btn btn-danger btn-sm" onclick="removeFile(\${index})">
                                <i class="fas fa-trash"></i> 제거
                            </button>
                        </div>
                    </div>
                `);
            container.append(fileItem);
        });
    }

    function removeFile(index) {
        selectedFiles.splice(index, 1);
        updateFileInput();
        renderSelectedFiles();
    }

    function getFileIcon(filename) {
        const extension = filename.split('.').pop().toLowerCase();

        const iconMap = {
            'jpg': 'fas fa-file-image',
            'jpeg': 'fas fa-file-image',
            'png': 'fas fa-file-image',
            'gif': 'fas fa-file-image',
            'pdf': 'fas fa-file-pdf',
            'doc': 'fas fa-file-word',
            'docx': 'fas fa-file-word',
            'xls': 'fas fa-file-excel',
            'xlsx': 'fas fa-file-excel',
            'ppt': 'fas fa-file-powerpoint',
            'pptx': 'fas fa-file-powerpoint',
            'txt': 'fas fa-file-alt',
            'zip': 'fas fa-file-archive',
            'hwp': 'fas fa-file-word'
        };

        return iconMap[extension] || 'fas fa-file';
    }

    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    function showMessage(message, type) {
        const messageArea = $('#messageArea');
        const alertClass = type === 'error' ? 'alert-danger' : 'alert-success';

        messageArea.html(`<div class="alert \${alertClass}">\${message}</div>`);

        setTimeout(() => {
            messageArea.empty();
        }, 5000);
    }
</script>