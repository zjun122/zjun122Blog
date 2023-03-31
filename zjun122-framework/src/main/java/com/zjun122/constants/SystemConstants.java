package com.zjun122.constants;

public class SystemConstants {

    /**
     * 文章是草稿状态
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     * 文章是正常状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEW_COUNT = "article:ViewCount";

    /**
     * 正常状态
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 友链状态为 审核通过
     */
    public static final String LINK_STATUS_NORMAL = "0";

    /**
     * 文章评论为 根评论
     */
    public static final int COMMENT_ROOT = -1;

    /**
     * 评论类型为：文章
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 评论类型为：友链
     */
    public static final String LINK_COMMENT = "1";

    /**
     * 菜单类型：C
     */
    public static final String MENU = "C";

    /**
     * 菜单类型：F
     */
    public static final String BUTTON = "F";

    /**
     * 前台登录用户redis的标识信息
     */
    public static final String REDIS_BLOG_LOGIN = "blogLogin：";

    /**
     * 后台登录用户redis的标识信息
     */
    public static final String REDIS_LOGIN = "login：";

    /**
     * 用户状态为：后台用户
     */
    public static final String ADMIN = "1";

    /**
     * 文章是否可以评论：是
     */
    public static final String ARTICLE_IS_COMMENT = "0";

    /**
     * 验证码：收件人邮箱标识
     */
    public static final String USER_EMAIL_KEY = "USER_EMAIL_KEY";

    /**
     * 验证码：邮箱验证码
     */
    public static final String EMAIL_CODE_KEY = "EMAIL_CODE_KEY";

    /**
     * 从前台页面注册的用户
     */
    public static final String IS_REG_USER = "999";
}
