<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .chat-container {
        max-width: 800px;
        margin: 20px auto;
        border: 1px solid #ddd;
        border-radius: 10px;
        overflow: hidden;
    }

    .chat-header {
        background: #007bff;
        color: white;
        padding: 15px;
        text-align: center;
    }

    .chat-messages {
        height: 400px;
        overflow-y: auto;
        padding: 20px;
        background: #f8f9fa;
    }

    .message {
        margin-bottom: 15px;
        padding: 10px;
        border-radius: 10px;
        max-width: 80%;
    }

    .user-message {
        background: #007bff;
        color: white;
        margin-left: auto;
    }

    .ai-message {
        background: white;
        border: 1px solid #ddd;
    }

    .chat-input {
        padding: 20px;
        background: white;
        border-top: 1px solid #ddd;
    }

    .loading {
        text-align: center;
        color: #6c757d;
        font-style: italic;
    }

    #promptInput {
        border-radius: 25px;
    }

    #sendButton {
        border-radius: 25px;
        min-width: 100px;
    }
</style>
<main>

    <div class="container-fluid">
        <h2 class="mt-3">AI Chat Service</h2>

        <!-- AI 서비스 상태 표시 -->
        <div id="statusAlert" class="alert alert-info" role="alert">
            AI 서비스 상태 확인 중...
        </div>

        <!-- 채팅 컨테이너 -->
        <div class="chat-container">
            <div class="chat-header">
                <h4>AI Assistant</h4>
                <small>Powered by Ollama</small>
            </div>

            <!-- 메시지 영역 -->
            <div id="chatMessages" class="chat-messages">
                <div class="message ai-message">
                    안녕하세요! AI Assistant입니다. 궁금한 것을 물어보세요.
                </div>
            </div>

            <!-- 입력 영역 -->
            <div class="chat-input">
                <div class="row g-2">
                    <div class="col">
                        <textarea id="promptInput"
                                  class="form-control"
                                  placeholder="질문을 입력하세요..."
                                  rows="2"
                                  maxlength="2000"></textarea>
                    </div>
                    <div class="col-auto">
                        <button id="sendButton" class="btn btn-primary h-100" type="button">
                            전송
                        </button>
                    </div>
                </div>
                <small class="text-muted mt-1 d-block">
                    <span id="charCount">0</span>/2000 글자
                </small>
            </div>
        </div>
    </div>
<!--
    <div id="checkStatusAi">Ai Ollama:</div><br>

    <button id="goCheckStatus" type="button" class="btn btn-primary" onclick="goCheckStatus();">AI 서비스 상태 확인</button><br>
    <br>
    <br>
    <br>
    <input type="text" id="promptValue" />
    <button id="goAskAI" type="button" class="btn btn-primary" onclick="goAskAI();">AI 질문 처리 (AJAX)</button><br>
-->
</main>
<script type="text/javascript">
    const MENU = "ai/ollama";
    const API_URL = "/api/" + MENU;
    $(document).ready(function() {
        if(!auth.accessTokenCheck()) return false;

        // AI 서비스 상태 확인
        checkAIStatus();

        // 글자 수 카운터
        $('#promptInput').on('input', function() {
            var length = $(this).val().length;
            $('#charCount').text(length);

            if (length > 1900) {
                $('#charCount').addClass('text-danger');
            } else {
                $('#charCount').removeClass('text-danger');
            }
        });

        // 전송 버튼 클릭
        $('#sendButton').click(function() {
            sendMessage();
        });

        // 엔터키로 전송 (Shift+Enter는 줄바꿈)
        $('#promptInput').keydown(function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                sendMessage();
            }
        });
    });

    // AI 서비스 상태 확인
    function checkAIStatus() {
        $.ajax({
            url: API_URL + "/status",
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            success: function(data) {
                if (data.status === 'online') {
                    $('#statusAlert')
                        .removeClass('alert-info alert-danger')
                        .addClass('alert-success')
                        .text('✓ ' + data.message);
                } else {
                    $('#statusAlert')
                        .removeClass('alert-info alert-success')
                        .addClass('alert-danger')
                        .text('✗ ' + data.message);
                }
            },
            error: function() {
                $('#statusAlert')
                    .removeClass('alert-info alert-success')
                    .addClass('alert-danger')
                    .text('✗ AI 서비스 상태를 확인할 수 없습니다.');
            }
        });
    }


    // 메시지 전송
    function sendMessage() {
        var prompt = $('#promptInput').val().trim();

        if (!prompt) {
            alert('질문을 입력해주세요.');
            return;
        }

        // 사용자 메시지 표시
        addMessage(prompt, 'user');

        // 로딩 표시
        addLoadingMessage();

        // 입력창 초기화 및 비활성화
        $('#promptInput').val('').prop('disabled', true);
        $('#sendButton').prop('disabled', true);
        $('#charCount').text('0');

        // AI에게 질문 전송
        $.ajax({
            url: API_URL + "/ask",
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            contentType: 'application/json',
            data: JSON.stringify({
                prompt: prompt
            }),
            success: function(data) {
                removeLoadingMessage();
console.log(data);
                if (data.success) {
                    addMessage(data.answer, 'ai');
                } else {
                    addMessage('오류: ' + data.message, 'ai');
                }
            },
            error: function(xhr) {
                removeLoadingMessage();
                var errorMsg = '서버 오류가 발생했습니다.';

                try {
                    var errorData = JSON.parse(xhr.responseText);
                    errorMsg = errorData.message || errorMsg;
                } catch (e) {}

                addMessage('오류: ' + errorMsg, 'ai');
            },
            complete: function() {
                // 입력창 재활성화
                $('#promptInput').prop('disabled', false).focus();
                $('#sendButton').prop('disabled', false);
            }
        });
    }

    // 메시지 추가
    function addMessage(content, type) {
        var messageClass = type === 'user' ? 'user-message' : 'ai-message';
        var messageHtml = '<div class="message ' + messageClass + '">' +
            escapeHtml(content) + '</div>';

        $('#chatMessages').append(messageHtml);
        scrollToBottom();
    }

    // 로딩 메시지 추가
    function addLoadingMessage() {
        var loadingHtml = '<div id="loadingMessage" class="loading">' +
            'AI가 답변을 생성하고 있습니다...</div>';
        $('#chatMessages').append(loadingHtml);
        scrollToBottom();
    }

    // 로딩 메시지 제거
    function removeLoadingMessage() {
        $('#loadingMessage').remove();
    }

    // 스크롤을 맨 아래로
    function scrollToBottom() {
        var chatMessages = $('#chatMessages');
        chatMessages.scrollTop(chatMessages[0].scrollHeight);
    }

    // HTML 이스케이프
    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;")
            .replace(/\n/g, "<br>");
    }








    const goAskAI_ = async() => {
        const promptValue = $("#promptValue").val();

        $('#loading').show();

        await fetch(API_URL + "/ask", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({ prompt: promptValue })
        })
            .then(res => res.json())
            .then(result => {

                console.log(result);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
    };
</script>