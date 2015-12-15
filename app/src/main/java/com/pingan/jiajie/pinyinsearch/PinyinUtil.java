package com.pingan.jiajie.pinyinsearch;


import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拼音辅助类 解决汉字转拼音 姓氏多音字问题
 *
 * @name 陈大龙
 * @date 2014-2-18
 * @time 下午9:30:46
 */
public class PinyinUtil {
    private static final String TAG = "ImPinyinUtil";

    // 常见的多音字姓氏数组
    protected static String[][] polyphone = {{"阿", "A"}, {"艾", "A"}, {"秘", "B"}, {"重", "C"}, {"褚", "C"},
            {"种", "C"}, {"长", "C"}, {"乘", "C"}, {"传", "C"}, {"车", "C"}, {"晁", "C"}, {"丁", "D"},
            {"费", "F"}, {"冯", "F"}, {"弗", "F"}, {"盖", "G"}, {"句", "G"}, {"谷", "G"}, {"郇", "H"},
            {"和", "H"}, {"适", "K"}, {"阚", "K"}, {"可", "K"}, {"乜", "N"}, {"区", "O"}, {"繁", "P"},
            {"仇", "Q"}, {"铖", "Q"}, {"覃", "Q"}, {"折", "S"}, {"色", "S"}, {"谌", "S"}, {"单", "S"},
            {"莘", "S"}, {"眭", "S"}, {"宿", "S"}, {"石", "S"}, {"食", "S"}, {"盛", "S"}, {"塔", "T"},
            {"陶", "T"}, {"吾", "W"}, {"浣", "W"}, {"颉", "X"}, {"解", "X"}, {"许", "X"}, {"厘", "X"},
            {"夏", "X"}, {"尉", "Y"}, {"乐", "Y"}, {"於", "Y"}, {"查", "Z"}, {"曾", "Z"}, {"翟", "Z"},
            {"藏", "Z"}, {"祭", "Z"}, {"卒", "Z"}, {"宓", "F"}};

    /**
     * 转换为拼音
     */
    public static String change2Pinyin(String text) {
        if (StringUtils.isEmpty(text)) {
            return "#";
        }
        if (isPolyphone(text)) {
            // 取得多音字
            return getPolyphone(text);
        } else {
            // 取得普通拼音
            return getPingYin(text);
        }
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @return
     */
    public static String getPingYin(String inputStr) {

        // 进一步优化 对于昵称字数很多的话截断处理 优化算法
        String inputString = inputStr.trim();

        // 按照中国常见姓名3个汉字决定 国外只取前三个
        if (inputString.length() > 3) {
            inputString = inputString.substring(0, 3);
        }

        String outStr = HanziToPinyin.getPinYin(inputString);
        // 判断是否是有值
        if (!StringUtils.isEmpty(outStr.trim())) {
            outStr = outStr.substring(0, 1).toUpperCase();
        } else {
            return "#";
        }

        // 正则表达式，判断首字母是否是大写英文字母
        if (outStr.matches("[A-Z]")) {
            return outStr;
        } else {
            return "#";
        }

    }


    /**
     * 判断是否属于多音字
     *
     * @param chinese
     * @return
     * @name 陈大龙
     * @date 2014-2-13
     * @time 下午3:16:48
     */
    public static boolean isPolyphone(String chinese) {
        if (StringUtils.isEmpty(chinese)) {
            return false;
        }

        if (chinese.trim().length() > 1) {
            chinese = chinese.substring(0, 1);
        }

        int index = -1;
        for (int i = 0; i < polyphone.length; i++) {
            if (polyphone[i][0].equals(chinese)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 得到准确的多音字
     *
     * @param chinese
     * @return
     * @name 陈大龙
     * @date 2014-2-13
     * @time 下午3:17:59
     */
    public static String getPolyphone(String chinese) {
        if (StringUtils.isEmpty(chinese)) {
            return "#";
        }

        if (chinese.trim().length() > 1) {
            chinese = chinese.substring(0, 1);
        }

        int index = 0;
        for (int i = 0; i < polyphone.length; i++) {
            if (polyphone[i][0].equals(chinese)) {
                index = i;
                break;
            }
        }
        return polyphone[index][1];
    }

    /**
     * 将字符串中的中文转化为小写拼音,其他字符不变
     *
     * @return
     */
    public static String getPingYinAll(String inputStr) {

        if (inputStr == null) {
            return "";
        }

        // 进一步优化 对于昵称字数很多的话截断处理 优化算法
        String inputString = inputStr.trim();

        String outStr = HanziToPinyin.getPinYin(inputString);
        // 判断是否是有值
        if (!StringUtils.isEmpty(outStr.trim())) {
            return outStr.toLowerCase();
        } else {
            return "";
        }

    }

    /**
     * 按号码-拼音搜索联系人
     *
     * @param str
     */
    public static ArrayList<Contact> search(String str,
                                            ArrayList<Contact> allContacts, ArrayList<Contact> contactList) {
        contactList.clear();
        // 如果搜索条件以0 1 +开头则按号码搜索
        if (str.startsWith("0") || str.startsWith("1") || str.startsWith("+")) {
            for (Contact contact : allContacts) {
                if (contact.getNumber() != null && contact.getName() != null) {
                    if (contact.getNumber().contains(str)
                            || contact.getName().contains(str)) {
                        contactList.add(contact);
                    }
                }
            }
            return contactList;
        }

        String result = "";
        for (Contact contact : allContacts) {
            //汉字全模式匹配
            Pattern f = Pattern.compile(str, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
            Matcher m = f.matcher(contact.getName());
            if (m.find()){

                contactList.add(contact);
                continue;
            } else if (contains(contact, str)) {
                //纯拼音全模式匹配
                contactList.add(contact);
                continue;
            }else {
            // 先将输入的字符串转换为拼音
            result = getPingYinAll(str);
            if (contains(contact, result)) {
                //汉拼混搭
                contactList.add(contact);
            }
            }
        }
        return contactList;
    }

    /**
     * 根据拼音搜索
     *
     * @return
     */
    public static boolean contains(Contact contact, String search) {
        if (TextUtils.isEmpty(contact.getName())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = FirstLetterUtil.getFirstLetter(contact
                    .getName());
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(getPingYinAll(contact.getName()));
            flag = matcher2.find();
        }

        return flag;
    }

    /**
     * @param chinese 汉字
     * @return 大写
     * @describe 获取所有汉字的首字母
     * @author 陈大龙
     * @date 2014-11-10
     * @time 下午4:46:49
     * @type String
     */
    public static String getChineseInitial(String chinese) {

        if (chinese == null) {
            return "";
        }
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {

                String t = HanziToPinyin.getPinYin(chinese.substring(i));

                if (StringUtils.isEmpty(t)) {
                    pybf.append(t.charAt(0));
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

}
