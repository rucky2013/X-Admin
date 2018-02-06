package com.leaf.xadmin.service;

import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-02-06 18:04</p>
 * <p>version: 1.0</p>
 */
public interface IMailService {

    /**
     * 发送简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleMail(String to, String subject, String content);

    /**
     * 发送Html格式邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    void sendHtmlMail(String to, String subject, String content);

    /**
     * 发送附件邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath);

    /**
     * 发送静态资源邮件
     *
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     */
    void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId);

    /**
     * 发送模板邮件
     *
     * @param to
     * @param subject
     * @param templateName
     * @param templateParams
     */
    void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> templateParams);
}
