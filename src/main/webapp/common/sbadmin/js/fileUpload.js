/**
 * 파일 업로드 유틸리티 라이브러리
 */
class FileUploadManager {
    constructor(options = {}) {
        this.options = {
            uploadUrl: '/api/files/upload',
            multipleUploadUrl: '/api/files/upload/multiple',
            listUrl: '/api/files/list',
            downloadUrl: '/api/files/download',
            previewUrl: '/api/files/preview',
            deleteUrl: '/api/files',
            maxFileSize: 50 * 1024 * 1024, // 50MB
            allowedExtensions: ['jpg', 'jpeg', 'png', 'gif', 'pdf', 'doc', 'docx', 'xls', 'xlsx', 'txt'],
            ...options
        };

        this.jwtToken = localStorage.getItem('jwtToken');
    }

    /**
     * 단일 파일 업로드
     */
    async uploadFile(file, referenceType, referenceId) {
        this.validateFile(file);

        const formData = new FormData();
        formData.append('file', file);
        formData.append('referenceType', referenceType);
        formData.append('referenceId', referenceId);

        return await this.makeRequest(this.options.uploadUrl, {
            method: 'POST',
            body: formData
        });
    }

    /**
     * 다중 파일 업로드
     */
    async uploadFiles(files, referenceType, referenceId, progressCallback) {
        files.forEach(file => this.validateFile(file));

        const formData = new FormData();
        files.forEach(file => formData.append('files', file));
        formData.append('referenceType', referenceType);
        formData.append('referenceId', referenceId);

        return await this.makeRequest(this.options.multipleUploadUrl, {
            method: 'POST',
            body: formData
        }, progressCallback);
    }

    /**
     * 파일 목록 조회
     */
    async getFileList(referenceType, referenceId) {
        const url = `${this.options.listUrl}?referenceType=${referenceType}&referenceId=${referenceId}`;
        return await this.makeRequest(url, { method: 'GET' });
    }

    /**
     * 파일 삭제
     */
    async deleteFile(fileId) {
        const url = `${this.options.deleteUrl}/${fileId}`;
        return await this.makeRequest(url, { method: 'DELETE' });
    }

    /**
     * 파일 다운로드 URL 생성
     */
    getDownloadUrl(fileId) {
        return `${this.options.downloadUrl}/${fileId}`;
    }

    /**
     * 파일 미리보기 URL 생성
     */
    getPreviewUrl(fileId) {
        return `${this.options.previewUrl}/${fileId}`;
    }

    /**
     * 파일 유효성 검증
     */
    validateFile(file) {
        if (!file) {
            throw new Error('파일이 선택되지 않았습니다.');
        }

        if (file.size > this.options.maxFileSize) {
            throw new Error(`파일 크기가 제한을 초과했습니다. (최대: ${this.formatFileSize(this.options.maxFileSize)})`);
        }

        const extension = this.getFileExtension(file.name);
        if (!this.options.allowedExtensions.includes(extension)) {
            throw new Error(`허용되지 않은 파일 형식입니다: ${extension}`);
        }
    }

    /**
     * HTTP 요청 수행
     */
    async makeRequest(url, options, progressCallback) {
        const headers = {
            'Authorization': `Bearer ${this.jwtToken}`,
            ...options.headers
        };

        // FormData인 경우 Content-Type 헤더 제거 (브라우저가 자동 설정)
        if (!(options.body instanceof FormData)) {
            headers['Content-Type'] = 'application/json';
        }

        const requestOptions = {
            ...options,
            headers
        };

        // 진행률 콜백이 있는 경우 XMLHttpRequest 사용
        if (progressCallback && options.method === 'POST') {
            return this.makeRequestWithProgress(url, requestOptions, progressCallback);
        }

        const response = await fetch(url, requestOptions);

        if (!response.ok) {
            const errorData = await response.json().catch(() => ({}));
            throw new Error(errorData.message || '요청 처리 중 오류가 발생했습니다.');
        }

        return await response.json();
    }

    /**
     * 진행률 콜백이 있는 HTTP 요청
     */
    makeRequestWithProgress(url, options, progressCallback) {
        return new Promise((resolve, reject) => {
            const xhr = new XMLHttpRequest();

            xhr.upload.addEventListener('progress', (e) => {
                if (e.lengthComputable && progressCallback) {
                    const percentComplete = (e.loaded / e.total) * 100;
                    progressCallback(percentComplete);
                }
            });

            xhr.addEventListener('load', () => {
                if (xhr.status >= 200 && xhr.status < 300) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        resolve(response);
                    } catch (e) {
                        reject(new Error('응답 파싱 오류'));
                    }
                } else {
                    try {
                        const errorResponse = JSON.parse(xhr.responseText);
                        reject(new Error(errorResponse.message || '요청 실패'));
                    } catch (e) {
                        reject(new Error('요청 실패'));
                    }
                }
            });

            xhr.addEventListener('error', () => {
                reject(new Error('네트워크 오류'));
            });

            xhr.open(options.method, url);

            // 헤더 설정
            Object.entries(options.headers || {}).forEach(([key, value]) => {
                xhr.setRequestHeader(key, value);
            });

            xhr.send(options.body);
        });
    }

    /**
     * 파일 확장자 추출
     */
    getFileExtension(filename) {
        return filename.split('.').pop().toLowerCase();
    }

    /**
     * 파일 크기 포맷팅
     */
    formatFileSize(bytes) {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    /**
     * 날짜 포맷팅
     */
    formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR');
    }

    /**
     * 파일 타입 아이콘 클래스 반환
     */
    getFileIconClass(filename, mimeType) {
        const extension = this.getFileExtension(filename);

        if (mimeType && mimeType.startsWith('image/')) return 'fa-file-image';
        if (mimeType && mimeType.startsWith('video/')) return 'fa-file-video';
        if (mimeType && mimeType.startsWith('audio/')) return 'fa-file-audio';

        switch (extension) {
            case 'pdf': return 'fa-file-pdf';
            case 'doc':
            case 'docx': return 'fa-file-word';
            case 'xls':
            case 'xlsx': return 'fa-file-excel';
            case 'ppt':
            case 'pptx': return 'fa-file-powerpoint';
            case 'zip':
            case 'rar':
            case '7z': return 'fa-file-archive';
            case 'txt': return 'fa-file-alt';
            default: return 'fa-file';
        }
    }
}

/**
 * 드래그 앤 드롭 파일 업로드 컴포넌트
 */
class DragDropUploader {
    constructor(containerId, options = {}) {
        this.container = document.getElementById(containerId);
        this.fileManager = new FileUploadManager(options);
        this.selectedFiles = [];

        this.init();
    }

    init() {
        this.setupDragDrop();
        this.setupFileInput();
        this.createUI();
    }

    setupDragDrop() {
        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {
            this.container.addEventListener(eventName, this.preventDefaults);
        });

        ['dragenter', 'dragover'].forEach(eventName => {
            this.container.addEventListener(eventName, () => {
                this.container.classList.add('dragover');
            });
        });

        ['dragleave', 'drop'].forEach(eventName => {
            this.container.addEventListener(eventName, () => {
                this.container.classList.remove('dragover');
            });
        });

        this.container.addEventListener('drop', (e) => {
            const files = Array.from(e.dataTransfer.files);
            this.handleFiles(files);
        });
    }

    setupFileInput() {
        const fileInput = this.container.querySelector('input[type="file"]');
        if (fileInput) {
            fileInput.addEventListener('change', (e) => {
                const files = Array.from(e.target.files);
                this.handleFiles(files);
            });
        }
    }

    createUI() {
        if (!this.container.innerHTML.trim()) {
            this.container.innerHTML = `
                <div class="upload-area">
                    <p>파일을 여기로 드래그하거나 클릭하여 선택하세요</p>
                    <input type="file" multiple style="display: none;">
                    <button type="button" class="btn btn-primary">파일 선택</button>
                </div>
                <div class="file-list"></div>
            `;

            const button = this.container.querySelector('button');
            const fileInput = this.container.querySelector('input[type="file"]');

            button.addEventListener('click', () => fileInput.click());
        }
    }

    handleFiles(files) {
        files.forEach(file => {
            try {
                this.fileManager.validateFile(file);
                this.selectedFiles.push(file);
            } catch (error) {
                this.showError(`${file.name}: ${error.message}`);
            }
        });

        this.renderFileList();
    }

    renderFileList() {
        const fileList = this.container.querySelector('.file-list');
        fileList.innerHTML = '';

        this.selectedFiles.forEach((file, index) => {
            const fileItem = document.createElement('div');
            fileItem.className = 'file-item';
            fileItem.innerHTML = `
                <div class="file-info">
                    <i class="${this.fileManager.getFileIconClass(file.name, file.type)}"></i>
                    <span class="filename">${file.name}</span>
                    <span class="filesize">(${this.fileManager.formatFileSize(file.size)})</span>
                </div>
                <div class="file-actions">
                    <button class="btn btn-danger btn-sm" onclick="this.removeFile(${index})">제거</button>
                </div>
            `;

            fileList.appendChild(fileItem);
        });
    }

    removeFile(index) {
        this.selectedFiles.splice(index, 1);
        this.renderFileList();
    }

    async uploadFiles(referenceType, referenceId, progressCallback) {
        if (this.selectedFiles.length === 0) {
            throw new Error('업로드할 파일이 없습니다.');
        }

        const result = await this.fileManager.uploadFiles(
            this.selectedFiles,
            referenceType,
            referenceId,
            progressCallback
        );

        // 업로드 성공 시 선택된 파일 목록 초기화
        this.selectedFiles = [];
        this.renderFileList();

        return result;
    }

    preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    showError(message) {
        // 에러 메시지 표시 (실제 구현에 맞게 수정)
        console.error(message);
    }
}

// 전역 객체로 사용할 수 있도록 export
window.FileUploadManager = FileUploadManager;
window.DragDropUploader = DragDropUploader;