/**
 * Created by ubuntu on 16-12-6.
 */
$(document).ready(function() {
    var um = UM.getEditor('myEditor');
    $('#nickname')[0].focus();
    // 新建WebSocket对象，最后的/websocket对应服务器端的@ServerEndpoint("/websocket")
    var socket = new WebSocket('ws://localhost:8080/chat/websocket');
    // 处理服务器端发送的数据
    socket.onmessage = function(event) {
        addMessage(event.data);
    };
    // 点击Send按钮时的操作
    $('#send').on('click', function() {
        var nickname = $('#nickname').val();

        console.log('fsdf' + nickname);
        if (!um.hasContents()) {    // 判断消息输入框是否为空
            // 消息输入框获取焦点
            um.focus();
            // 添加抖动效果
            $('.edui-container').addClass('am-animation-shake');
            setTimeout("$('.edui-container').removeClass('am-animation-shake')", 1000);
        } else if (nickname == '') {    // 判断昵称框是否为空
            //昵称框获取焦点
            $('#nickname')[0].focus();
            // 添加抖动效果
            $('#message-input-nickname').addClass('am-animation-shake');
            setTimeout("$('#message-input-nickname').removeClass('am-animation-shake')", 1000);
        } else {
            // 发送消息
            socket.send(JSON.stringify({
                content : um.getContent(),
                nickname : nickname
            }));
            // 清空消息输入框
            um.setContent('');
            // 消息输入框获取焦点
            um.focus();
        }
    });

    // 把消息添加到聊天内容中
    function addMessage(message) {
        message = JSON.parse(message);
        var messageItem = '<li class="am-comment '
            + (message.isSelf ? 'am-comment-flip' : 'am-comment')
            + '">'
            + '<a href="javascript:void(0)" ><img src="assets/images/'
            + (message.isSelf ? 'self.png' : 'others.jpg')
            + '" alt="" class="am-comment-avatar" width="48" height="48"/></a>'
            + '<div class="am-comment-main"><header class="am-comment-hd"><div class="am-comment-meta">'
            + '<a href="javascript:void(0)" class="am-comment-author">'
            + message.nickname + '</a> <time>' + message.date
            + '</time></div></header>'
            + '<div class="am-comment-bd">' + message.content
            + '</div></div></li>';
        $(messageItem).appendTo('#message-list');
        // 把滚动条滚动到底部
        $(".chat-content-container").scrollTop($(".chat-content-container")[0].scrollHeight);
    }

    // 初始化消息输入框
    var um = UM.getEditor('myEditor');
    // 使昵称框获取焦点
    $('#nickname')[0].focus();
});
