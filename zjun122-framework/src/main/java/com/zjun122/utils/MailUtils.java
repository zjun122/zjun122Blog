package com.zjun122.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Random;

/**
 * 发邮件工具类
 */
public final class MailUtils {
    private static final String USER = "123456@qq.com"; // 发件人称号，同邮箱地址
    private static final String PASSWORD = "***"; // 如果是qq邮箱可以使户端授权码，或者登录密码

    /**
     *
     * @param to 收件人邮箱
     *
     */
    /* 发送验证信息的邮件 */
    public static String sendMail(String to) {

        Transport ts = null;
        String code = null;
        try {
            Properties pro = new Properties();
            pro.setProperty("mail.host", "smtp.qq.com"); //设置qq邮件服务器
            pro.setProperty("mail.transport.protocol", "smtp"); //邮件发送协议
            pro.setProperty("mail.smtp.auth", "true"); //需要验证用户名密码

            //QQ邮箱需要设置SSL加密，加上下列代码；（其他邮箱不需要）
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            pro.put("mail.smtp.ssl.enable", "true");
            pro.put("mail.smtp.ssl.socketFactory", sf);
            pro.put("mail.smtp.ssl.protocols", "TLSv1.2");


            //使用JavaMail发送邮箱的5个步骤

            //1、创建定义整个应用程序所需的环境信息的session对象
            //qq邮箱才需要使用，其他的不用
            Session session = Session.getDefaultInstance(pro, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    //发件人的邮箱 用户名，授权码
                    return new PasswordAuthentication(USER, PASSWORD);
                }
            });

            //开启session的debug模式，可以看到发送email时的运行状态
//            session.setDebug(true);

            //2、通过session得到transport对象
            ts = session.getTransport();

            //3、使用邮箱的用户名和授权码连接上邮箱服务器
            ts.connect("smtp.qq.com", USER, PASSWORD);

            //4、创建邮件：写邮件
            //注意：需要传递session
            MimeMessage message = new MimeMessage(session);

            //指明邮件的发件人
            message.setFrom(new InternetAddress("1738142558@qq.com"));

            //指明邮件的收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // 设置邮件标题
            message.setSubject("这是一封验证码邮箱，有效期为5分钟");

            code = Code();

            // 设置邮件的内容体
            message.setContent("<h1 style='color: blue;'>" + code + "</h1>", "text/html;charset=UTF-8");
            // 发送邮件
            ts.sendMessage(message, message.getAllRecipients());
            //关闭连接
            ts.close();
            return code;
        } catch (GeneralSecurityException | MessagingException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static String Code() {
        Random random = new Random();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static void main(String[] args) throws Exception { // 做测试用
//        MailUtils.sendMail("2217991987@qq.com","你好，这是一封测试邮件，无需回复。","测试邮件");
        System.out.println("发送成功");
    }



}
