package com.cjh.ttt.base.util;

/**
 * 信息加星号
 *
 * @author cjh
 * @date 2020/3/25
 */
public class StringReplaceUtil {

    /**
     * 根据姓名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 姓名
     * @return 替换后的姓名
     *     <p>
     *     ?<= 开头保留
     *     ?= 结尾保留
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced;

        if (userName == null) {
            userName = "";
        }

        int nameLength = userName.length();

        if (nameLength <= 1) {
            userNameAfterReplaced = "";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=.).");
        } else {
            userNameAfterReplaced = replaceAction(userName, "(?<=.).(?=.+)");
        }

        return userNameAfterReplaced;
    }

    /**
     * 身份证号替换，保留前六位和后四位
     * <p>
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     */
    public static String idCardReplaceWithStar(String idCard) {
        if (idCard == null || idCard.isEmpty()) {
            return null;
        } else {
            return replaceAction(idCard, "(?<=\\d{6})\\d(?=\\d{4})");
        }
    }


    public static String telReplaceWithStar(String tel) {
        if (tel == null || tel.isEmpty()) {
            return null;
        } else {
            return replaceAction(tel, "(?<=\\d{3})\\d(?=\\d{4})");
        }
    }

    /**
     * 银行卡替换，保留后四位
     * <p>
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     */
    public static String bankCardReplaceWithStar(String bankCard) {

        if (bankCard == null || bankCard.isEmpty()) {
            return null;
        } else {
            return replaceAction(bankCard, "(?<=\\d{0})\\d(?=\\d{4})");
        }
    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }


    public static void main(String[] args) {
        System.out.println(userNameReplaceWithStar("正则匹配"));
        ;
    }
}
