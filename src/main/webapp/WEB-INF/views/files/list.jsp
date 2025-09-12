<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>파일 업로드</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .upload-area {
            border: 2px dashed #ccc;
            border-radius: 10px;
            padding: 20px;
            text-align: center;
            margin: 20px 0;
            cursor: pointer;
            transition: border-color 0.3s;
        }
        .upload-area:hover {
            border-color: #007bff;
        }
        .upload-area.dragover {
            border-color: #007bff;
            background-color: #f8f9fa;
        }
        .file-list {
            margin-top: 20px;
        }
        .file-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            margin: 5px 0;
        }
        .file-item .file-info {
            flex-grow: 1;
        }
        .file-item .file-actions {
            margin-left: 10px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin: 0 2px;
        }
        .btn-primary { background-color: #007bff; color: white; }
        .btn-danger { background-color: #dc3545; color: white; }
        .btn-success { background-color: #28a745; color: white; }
        .progress-bar {
            width: 100%;
            height: 20px;
            background-color: #f0f0f0;
            border-radius: 10px;
            overflow: hidden;
            margin: 10px 0;
        }
        .progress-fill {
            height: 100%;
            background-color: #007bff;
            transition: width 0.3s;
        }
        .error-message {
            color: #dc3545;
            margin: 10px 0;
        }
        .success-message {
            color: #28a745;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>파일 업로드</h2>

    <!-- 파일 업로드 영역 -->
    <div class="upload-area" id="uploadArea">
        <p>파일을 여기로 드래그하거나 클릭하여 선택하세요</p>
        <input type="file" id="fileInput" multiple style="display: none;">
    </div>

    <!-- 업로드 설정 -->
    <div class="upload-settings">
        <label>참조 타입:</label>
        <select id="referenceType">
            <option value="user">User</option>
            <option value="company">Company</option>
            <option value="bbs">BBS</option>
        </select>

        <label>참조 ID:</label>
        <input type="number" id="referenceId" placeholder="참조 ID 입력" required>

        <button class="btn btn-primary" onclick="uploadFiles()">업로드</button>
    </div>

    <!-- 프로그레스 바 -->
    <div class="progress-bar" id="progressBar" style="display: none;">
        <div class="progress-fill" id="progressFill"></div>
    </div>

    <!-- 메시지 표시 -->
    <div id="messageArea"></div>

    <!-- 선택된 파일 목록 -->
    <div class="file-list" id="selectedFiles"></div>

    <!-- 업로드된 파일 목록 -->
    <div class="file-list" id="uploadedFiles">
        <h3>업로드된 파일</h3>
        <div id="fileListContainer"></div>
    </div>
</div>

<script>
    let selectedFiles = [];
    let jwtToken = localStorage.getItem('jwtToken'); // JWT 토큰 가져오기

    $(document).ready(function() {
        // 드래그 앤 드롭 이벤트
        $('#uploadArea').on('dragover', function(e) {
            e.preventDefault();
            $(this).addClass('dragover');
        });

        $('#uploadArea').on('dragleave', function(e) {
            e.preventDefault();
            $(this).removeClass('dragover');
        });

        $('#uploadArea').on('drop', function(e) {
            e.preventDefault();
            $(this).removeClass('dragover');

            const files = e.originalEvent.dataTransfer.files;
            handleFileSelect(files);
        });

        // 클릭하여 파일 선택
        $('#uploadArea').click(function() {
            $('#fileInput').click();
        });

        $('#fileInput').change(function() {
            handleFileSelect(this.files);
        });

        // 페이지 로드 시 파일 목록 불러오기
        loadFileList();
    });

    function handleFileSelect(files) {
        selectedFiles = Array.from(files);
        displaySelectedFiles();
    }

    function displaySelectedFiles() {
        const container = $('#selectedFiles');
        container.empty();

        if (selectedFiles.length === 0) {
            container.hide();
            return;
        }

        container.show();
        container.append('<h3>선택된 파일</h3>');

        selectedFiles.forEach((file, index) => {
            const fileItem = $(`
                    <div class="file-item">
                        <div class="file-info">
                            <strong>${file.name}</strong>
                            <span>(${formatFileSize(file.size)})</span>
                        </div>
                        <div class="file-actions">
                            <button class="btn btn-danger" onclick="removeFile(${index})">제거</button>
                        </div>
                    </div>
                `);
            container.append(fileItem);
        });
    }

    function removeFile(index) {
        selectedFiles.splice(index, 1);
        displaySelectedFiles();
    }

    function uploadFiles() {
        const referenceType = $('#referenceType').val();
        const referenceId = $('#referenceId').val();

        if (!referenceId) {
            showMessage('참조 ID를 입력해주세요.', 'error');
            return;
        }

        if (selectedFiles.length === 0) {
            showMessage('업로드할 파일을 선택해주세요.', 'error');
            return;
        }

        const formData = new FormData();
        selectedFiles.forEach(file => {
            formData.append('files', file);
        });
        formData.append('referenceType', referenceType);
        formData.append('referenceId', referenceId);

        // 프로그레스 바 표시
        $('#progressBar').show();
        $('#progressFill').css('width', '0%');

        $.ajax({
            url: '/api/files/upload/multiple',
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            xhr: function() {
                const xhr = new window.XMLHttpRequest();
                xhr.upload.addEventListener('progress', function(e) {
                    if (e.lengthComputable) {
                        const percentComplete = (e.loaded / e.total) * 100;
                        $('#progressFill').css('width', percentComplete + '%');
                    }
                });
                return xhr;
            },
            success: function(response) {
                $('#progressBar').hide();
                if (response.success) {
                    showMessage(response.message, 'success');
                    selectedFiles = [];
                    displaySelectedFiles();
                    $('#fileInput').val('');
                    loadFileList(); // 파일 목록 새로고침
                } else {
                    showMessage(response.message, 'error');
                }
            },
            error: function(xhr, status, error) {
                $('#progressBar').hide();
                let errorMessage = '업로드 중 오류가 발생했습니다.';
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMessage = xhr.responseJSON.message;
                }
                showMessage(errorMessage, 'error');
            }
        });
    }

    function loadFileList() {
        const referenceType = $('#referenceType').val();
        const referenceId = $('#referenceId').val();

        if (!referenceId) return;

        $.ajax({
            url: '/api/files/list',
            type: 'GET',
            data: {
                referenceType: referenceType,
                referenceId: referenceId
            },
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            success: function(files) {
                displayFileList(files);
            },
            error: function() {
                showMessage('파일 목록을 불러오는데 실패했습니다.', 'error');
            }
        });
    }

    function displayFileList(files) {
        const container = $('#fileListContainer');
        container.empty();

        if (files.length === 0) {
            container.append('<p>업로드된 파일이 없습니다.</p>');
            return;
        }

        files.forEach(file => {
            const fileItem = $(`
                    <div class="file-item">
                        <div class="file-info">
                            <strong>${file.originalFilename}</strong>
                            <span>(${formatFileSize(file.fileSize)})</span>
                            <br>
                            <small>업로드: ${formatDate(file.uploadDate)}</small>
                        </div>
                        <div class="file-actions">
                            <button class="btn btn-primary" onclick="downloadFile(${file.fileId})">다운로드</button>
                            ${file.mimeType && file.mimeType.startsWith('image/') ?
                                `<button class="btn btn-success" onclick="previewFile(${file.fileId})">미리보기</button>` : ''}
                            <button class="btn btn-danger" onclick="deleteFile(${file.fileId})">삭제</button>
                        </div>
                    </div>
                `);
            container.append(fileItem);
        });
    }

    function downloadFile(fileId) {
        window.open('/api/files/download/' + fileId, '_blank');
    }

    function previewFile(fileId) {
        window.open('/api/files/preview/' + fileId, '_blank');
    }

    function deleteFile(fileId) {
        if (!confirm('파일을 삭제하시겠습니까?')) {
            return;
        }

        $.ajax({
            url: '/api/files/' + fileId,
            type: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + jwtToken
            },
            success: function(response) {
                if (response.success) {
                    showMessage(response.message, 'success');
                    loadFileList(); // 파일 목록 새로고침
                } else {
                    showMessage(response.message, 'error');
                }
            },
            error: function() {
                showMessage('파일 삭제 중 오류가 발생했습니다.', 'error');
            }
        });
    }

    function showMessage(message, type) {
        const messageArea = $('#messageArea');
        messageArea.removeClass('error-message success-message');
        messageArea.addClass(type === 'error' ? 'error-message' : 'success-message');
        messageArea.text(message);

        setTimeout(() => {
            messageArea.empty().removeClass('error-message success-message');
        }, 5000);
    }

    function formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR');
    }

    // 참조 타입이나 ID가 변경되면 파일 목록 새로고침
    $('#referenceType, #referenceId').change(function() {
        loadFileList();
    });
</script>
</body>
</html>