package com.blockchain.robot.entity;

/**
 * 钉钉 通知
 */
public class DingMessage {

    private String msgtype;
    private TextBean text;

    public static DingMessage newInstance(String content) {
        DingMessage message = new DingMessage();
        message.setMsgtype("text");
        TextBean bean = new TextBean();
        bean.setContent(content);
        message.setText(bean);
        return message;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public static class TextBean {

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
