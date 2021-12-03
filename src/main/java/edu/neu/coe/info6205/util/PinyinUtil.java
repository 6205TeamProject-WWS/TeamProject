package edu.neu.coe.info6205.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class PinyinUtil {

    public static String getPinyin(String text, String separator) {
        char[] chars = text.toCharArray();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 设置大小写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 设置声调表示方法
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        // 设置字母u表示方法
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        String[] s;
        String rs = null;
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                // 判断是否为汉字字符
                if (String.valueOf(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    s = PinyinHelper.toHanyuPinyinStringArray(chars[i], format);
                    if (s != null) {
                        sb.append(s[0]).append(separator);
                        continue;
                    }
                }
                sb.append(String.valueOf(chars[i]));
                if ((i + 1 >= chars.length) || String.valueOf(chars[i + 1]).matches("[\\u4E00-\\u9FA5]+")) {
                    sb.append(separator);
                }
            }
            rs = sb.substring(0, sb.length() - 1);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static String[] readAllChinese() {
        String token1 = "";
        List<String> temps = new ArrayList<>();
        try {

            Scanner inFile1 = new Scanner(new File("./src/shuffledChinese.txt"));
            while(inFile1.hasNext()) {
                //find next line
                token1 = inFile1.next();
                temps.add(token1);
            }
            inFile1.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        String[] tempsArray = temps.toArray(new String[0]);

        return tempsArray;

    }

//    public static void main(String[] args) {
//        String[] s = readAllChinese();
//        for (int i = 0; i < s.length; i++)
//        System.out.println(getPinyin(s[i]," "));
//    }

}
