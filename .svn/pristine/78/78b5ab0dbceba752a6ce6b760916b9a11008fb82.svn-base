package com.telek.hemsipc.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telek.hemsipc.service.impl.Iec104DataCollectionServiceImpl;


/**
 * 字符串工具类
 * 
 * @author wwggzz
 */
public class StringUtil {
    private static Logger log = LoggerFactory.getLogger(StringUtil.class);
    public static DecimalFormat df = new DecimalFormat("#0.00");
    public static DecimalFormat dfFour = new DecimalFormat("#0.0000");
    public static DecimalFormat dfFloat = new DecimalFormat("#0.0000");
    public static String regexStr = "^[A-Za-z0-9-_\u4e00-\u9fa5]+$";

    /**
     * 日期格式化函數
     * 
     * @param date : 格式化前的字符串,長度必須為8,且是YYYYMMDD格式; ?? 如果為null,或空串或只有空格的字符串,返回空串;
     *        ?? 如果長度是不為8的字符串,返回空串;
     * @return 格式為: YYYY/MM/DD 的字符串;
     */
    public static String formatDate(String date) {
        if (date == null) {
            return "";
        }
        date = date.trim();
        if (date.equals("&nbsp;")) {
            return "";
        }
        if (date.length() == 0 || date.length() != 8) {
            return "";
        }
        date = date.substring(0, 4) + "/" + date.substring(4, 6) + "/" + date.substring(6, 8);
        return date;
    }

    /**
     * 获取当前日期格式化函数
     * 
     * @param dateFormat 传入需要输出的日期格式 ；如果為null,或空串或只有空格的字符串，则默认为yyyyMMddHHmmss格式
     * @return 格式為: 输入的日期格式 的字符串; 默认为 yyyyMMddHHmmss
     */
    public static String getNow(String dateFormat) {
        if (isBlank(dateFormat)) {
            dateFormat = "yyyyMMddHHmmss";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(dateFormat);
        return dateformat.format(System.currentTimeMillis());
    }

    /**
     * 日期格式化函數
     * 
     * @param date : 格式化前的字符串,長度必須為10,且是YYYY/MM/DD格式; ??
     *        如果為null,或空串或只有空格的字符串,返回空串; ?? 如果長度是不為8的字符串,返回空串;
     * @return 格式為: YYYYMMDD 的字符串;
     */
    public static String formatDateToStr(String date) {
        if (date == null) {
            return "";
        }
        date = date.trim();
        if (date.equals("&nbsp;")) {
            return "";
        }
        if (date.length() == 0 || date.length() != 10) {
            return "";
        }
        date = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        return date;
    }

    /**
     * 字符转double Description: Date:2012-11-3
     * 
     * @author wm
     * @param @param str
     * @param @return
     * @return double
     */
    public static double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return 0d;
        }
    }

    public static float parseFloat(String str) {
        try {
            return Float.parseFloat(str.trim());
        } catch (Exception e) {
            return 0F;
        }
    }
    
    /**
     * Object转Double类型.
     * 
     * @param obj 传入object对象
     * @return double
     */
    public static double parseDouble(final Object obj) {
        try {
            return Double.parseDouble(toString(obj));
        } catch (Exception e) {
            return 0d;
        }
    }

    /**
     * 字符转long Description: Date:2012-11-3
     * 
     * @author wm
     * @param @param str
     * @param @return
     * @return long
     */
    public static long parseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * Description： object对象转Long类型2016-1-8
     * 
     * @author wll
     * @param obj
     * @return
     */
    public static long parseLong(Object obj) {
        try {
            return Long.parseLong(toString(obj));
        } catch (Exception e) {
            return 0L;
        }
    }

    /**
     * 字符转int Description: Date:2012-11-3
     * 
     * @author wm
     * @param @param str
     * @param @return
     * @return long
     */
    public static int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Object转int类型.
     * 
     * @param obj 传入object对象
     * @return double
     */
    public static int parseInt(final Object obj) {
        try {
            return Integer.parseInt(toString(obj));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 判斷英數字
     * 
     * @param val
     * @return
     */
    public static boolean isAlpha(String val) {
        if (val == null) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);
            if ((ch < 'a' || ch > 'z') && (ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判斷是否為大寫英文字母
     * 
     * @param val
     * @return
     */
    public static boolean isCapsEnglish(String val) {
        if (val == null) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);
            if (ch < 'A' || ch > 'Z') {
                return false;
            }
        }
        return true;
    }

    /**
     * @param val
     * @return
     */
    public static boolean isNumber(String val) {
        if (val == null) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-10-17下午3:58:18
     * @Describe:isIDStr 判断是否是正常的字符串。看是否包含除了汉字大小写字母和数字-以外的其他字符
     * @return
     */
    public static boolean isNormalStr(String str) {
        if (isBlank(str)) {
            return false;
        }
        if (str.matches(regexStr)) {
            return true;
        } else {
            log.info("lxftest,isNormalStr判断为false.str=" + str);
            return false;
        }
    }

    public static String sqlQueryFilter(String str) {
        if (!isBlank(str)) {
            if (str.contains("[")) {
                str = str.replace("[", "\\[");
            }
            if (str.contains("]")) {
                str = str.replace("]", "\\]");
            }
            if (str.contains("_")) {
                str = str.replace("_", "\\_");
            }
            if (str.contains("%")) {
                str = str.replace("%", "\\%");
            }
        }
        return str;
    }

    /**
     * @param val
     * @return
     */
    public static boolean isNumberOrCapsEnglish(String val) {
        if (val == null) {
            return false;
        }
        for (int i = 0; i < val.length(); i++) {
            char ch = val.charAt(i);
            if ((ch < 'A' || ch > 'Z') && (ch < '0' || ch > '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午1:19:21
     * @Describe:isBlank 空判断 如果是空返回true 支持map,list,String,JSONArray,set,
     *                   JSONObject,其他大对象建议直接判断。
     * @param object
     * @return
     */
    public static boolean isBlank(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return isBlank(object.toString());
        } else if (object instanceof List<?>) {
            return isBlankList((List<?>) object);
        } else if (object instanceof Map<?, ?>) {
            return isBlankMap((Map<?, ?>) object);
        } else if (object instanceof JSONArray) {
            return isBlankJSONArray((JSONArray) object);
        } else if (object instanceof JSONObject) {
            return isBlankJSONObject((JSONObject) object);
        } else if (object instanceof Set<?>) {
            return isBlankSet((Set<?>) object);
        }
        return isBlank(object.toString());
    }

    private static boolean isBlankSet(Set<?> set) {
        return set == null || set.size() == 0 || set.isEmpty();
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午1:19:21
     * @Describe:isBlank 多传参 非空判断。所有都是非空，返回true。支持map list String JSONArray
     *                   JSONObject,其他大对象建议直接判断。
     * @param object
     * @return
     */
    public static boolean isAllNotBlank(Object... objs) {
        if (objs == null) {
            return false;
        }
        for (Object object : objs) {
            if (isBlank(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午1:19:21
     * @Describe:isBlank 多传参 空判断。所有都是空，返回true支持map list String JSONArray
     *                   JSONObject, 其他大对象建议直接判断。
     * @param object
     * @return
     */
    public static boolean isAllBlank(Object... objs) {
        if (objs == null) {
            return true;
        }
        for (Object object : objs) {
            if (!isBlank(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午1:19:21
     * @Describe:isBlank 多传参 空判断。只要存在一个空，就返回true支持map list String JSONArray
     *                   JSONObject, 其他大对象建议直接判断。
     * @param object
     * @return
     */
    public static boolean isContainBlank(Object... objs) {
        return !isAllNotBlank(objs);
    }

    /**
     * 判斷字符串是否為空
     * 
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().equals("");
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30上午10:15:44
     * @Describe:isBlank 判断list对象是否为空
     * @param list
     * @return
     */
    private static boolean isBlankList(List<?> list) {
        return list == null || list.size() == 0 || list.isEmpty();
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30上午10:15:44
     * @Describe:isBlank 判断对象是否为空
     * @param list
     * @return
     */
    private static boolean isBlankMap(Map<?, ?> map) {
        return map == null || map.size() == 0 || map.isEmpty();
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午2:01:04
     * @Describe:isBlankJSONArray 判断对象是否为空
     * @param object
     * @return
     */
    private static boolean isBlankJSONArray(JSONArray object) {
        return object == null || object.size() == 0 || object.isEmpty();
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午2:07:06
     * @Describe:isBlankJSONObject 判断对象是否为空
     * @param object
     * @return
     */
    private static boolean isBlankJSONObject(JSONObject object) {
        return object == null || object.size() == 0 || object.isEmpty();
    }

    /**
     * 取得字符串指定位置的字符
     * 
     * @param str
     * @param num
     * @return
     */
    public static String getStrChar(String str, int num) {
        if (str == null || num > str.length()) {
            return "";
        }
        return str.substring(num - 1, num);
    }

    /**
     * input 1---52 ,return 'A'----'Z'
     * 
     * @param i
     * @return
     */
    public static String convertintTochar(int i) {
        if (i <= 26) {
            return ((char) (64 + i)) + "";
        } else if (i <= 52) {
            return "A" + ((char) (64 + i - 26));
        } else if (i > 52) {
            return "B" + ((char) (64 + i - 52));
        } else {
            return "";
        }
    }

    /**
     * 返回对象字符串，对于出错情况返回空字符串
     * 
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        try {
            return obj == null ? "" : obj.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 把int型的list转成String
     * 
     * @param List <Integer> intList
     * @return List<String>
     */
    public static List<String> int2stringList(List<Integer> intList) {
        List<String> resultList = new ArrayList<String>();
        for (Integer i : intList) {
            resultList.add(StringUtil.toString(i));
        }
        return resultList;
    }

    /**
     * UUID生成
     * 
     * @author ycj
     * @return
     */
    public static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }

    public static String arrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        int length = array.length;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String v = array[i];
            if (v == null) {
                v = "0.0";
            }
            if (i == length - 1) {
                b.append(v);
            } else {
                b.append(v).append(",");
            }
        }
        return b.toString();
    }

    public static String doubleToString(Double[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        int length = array.length;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            double v = array[i];
            if (i == length - 1) {
                b.append(v);
            } else {
                b.append(v).append(",");
            }
        }
        return b.toString();
    }

    public static String doubleToString(double[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        int length = array.length;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) {
            double v = array[i];
            if (i == length - 1) {
                b.append(v);
            } else {
                b.append(v).append(",");
            }
        }
        return b.toString();
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-18上午9:46:51
     * @Describe:getRandomData 随机抖动
     * @param source 源数据
     * @param percent 浮动百分数，以小数表示。如0.1代表上下浮动百分之10.(现在先写死0.1)
     * @return
     */
    public static double getRandomData(double source) {
        // 默认配置0.1的抖动。
        double percent = 0.1;
        double offset = source * percent;
        if (Math.random() > 0.5) {
            source = source + offset * Math.random();
        } else {
            source = source - offset * Math.random();
        }
        return parseDouble(df.format(source));
    }

    public static String formatDoubleTwo(Object obj) {
        return df.format(parseDouble(obj));
    }

    public static String formatDoubleFour(Object obj) {
        return dfFour.format(parseDouble(obj));
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-25上午9:53:38
     * @Describe:mysqlInFormat 字符转换。形如1,2,3 转成mysql in 查询的 ('1','2','3')
     * @param str
     * @return
     */
    public static String mysqlInFormat(String str) {
        if (!StringUtil.isBlank(str)) {
            String[] statess = str.split(",");
            int length = statess.length;
            StringBuffer sbBuffer = new StringBuffer();
            // 目前状态只有5个
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    sbBuffer.append("(");
                }
                sbBuffer.append("'" + statess[i] + "',");
                if (i == length - 1) {
                    sbBuffer.replace(sbBuffer.length() - 1, sbBuffer.length(), ")");
                }
            }
            return sbBuffer.toString();
        }
        return "";
    }

    private static void testBlack() {
        Map<String, Map<String, String>> blackMap = new HashMap<String, Map<String, String>>();
        List<?> blackList = null;
        Map<String, String> notBlackMap = new HashMap<String, String>();
        notBlackMap.put("2", "2");
        List<String> notBlackList = new ArrayList<String>();
        notBlackList.add("12");
        System.out.println(isBlank(blackMap) == true);
        System.out.println(isBlank(notBlackMap) == false);
        System.out.println(isBlank(blackList) == true);
        System.out.println(isAllBlank(blackMap, blackList) == true);
        System.out.println(isAllBlank(blackMap, notBlackMap) == false);
        System.out.println(isAllNotBlank(notBlackMap, notBlackList) == true);
        System.out.println(isAllNotBlank(notBlackMap, blackList) == false);
        System.out.println(isContainBlank(notBlackList, notBlackMap, blackMap) == true);
        System.out.println(isContainBlank(notBlackList, notBlackMap, notBlackList) == false);
        // 下面是测试JSONArray
        JSONArray blackJSONArray = null;
        JSONArray notBlackJSONArray = new JSONArray();
        notBlackJSONArray.add("45");
        System.out.println(isBlank(blackJSONArray) == true);
        System.out.println(isBlank(notBlackJSONArray) == false);
        // 下面测试JSONObject
        JSONObject blackJSONObject = null;
        JSONObject notBlackJSONObject = new JSONObject();
        notBlackJSONObject.put("12", "2");
        System.out.println(isBlank(blackJSONObject) == true);
        System.out.println(isBlank(notBlackJSONObject) == false);
    }

    /**
     * @Modifier:lxf
     * @Date：2017-8-30下午7:30:52
     * @Describe:judgeDate 判断时间是否在时间段内
     * @param date 格式0212
     * @param airModelDate 时间格式必须是1101-1231,0101-0430
     * @return
     */
    public static boolean judgeDate(String date, String dates) {
        String[] strs = dates.split(",");
        int judgeDate = parseInt(date);
        for (String str : strs) {
            String[] strs2 = str.split("-");
            int start = parseInt(strs2[0]);
            int end = parseInt(strs2[1]);
            if (start <= judgeDate && judgeDate <= end) {
                return true;
            }
        }
        return false;
    }

    private static void testCompare() {
        String s1 = "20170802";
        String s2 = "20170802";
        String s3 = "2017080200000";
        String s4 = "20170801";
        String s5 = "20170801000";
        String s6 = "20170803";
        System.out.println(s1.compareTo(s2));
        System.out.println(s1.compareTo(s3));
        System.out.println(s1.compareTo(s4));
        System.out.println(s1.compareTo(s5));
        System.out.println(s1.compareTo(s6));
    }

    /**
     * @Modifier:lxf
     * @Date：2017-9-11下午5:23:58
     * @Describe:judgeModelDate 校验格式1101-1231,0101-0430。只做简单格式校验。不处理大小月之类。
     * @param airAutoModelDate
     * @return
     */
    public static boolean judgeModelDate(String modelDate) {
        if (StringUtil.isBlank(modelDate)) {
            return false;
        }
        if (modelDate.contains(",")) {
            String[] dates = modelDate.split(",");
            for (String date : dates) {
                if (!judgeModelDateSingle(date)) {
                    return false;
                }
            }
            return true;
        } else {
            if (modelDate.contains("-")) {
                return judgeModelDateSingle(modelDate);
            }
        }
        return false;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-9-11下午5:59:58
     * @Describe:judgeModelDateSingle 校验格式1101-1231。只做简单格式校验。不处理大小月之类。
     * @param modelDate
     */
    private static boolean judgeModelDateSingle(String modelDate) {
        String[] modelDates = modelDate.split("-");
        String date1 = modelDates[0];
        String date2 = modelDates[1];
        if (date1.length() == 4 && date2.length() == 4) {
            int month1 = StringUtil.parseInt(date1.substring(0, 2));
            int day1 = StringUtil.parseInt(date1.substring(2, 4));
            int month2 = StringUtil.parseInt(date2.substring(0, 2));
            int day2 = StringUtil.parseInt(date2.substring(2, 4));
            if (month1 >= 1 && month1 <= 12 && day1 >= 1 && day1 <= 31 && month2 >= 1 && month2 <= 12
                    && day2 >= 1 && day2 <= 31) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Modifier:lxf
     * @Date：2017-9-11下午6:08:50
     * @Describe:judgeControlTempute 校验格式26-29。
     * @param controlTempute
     * @return
     */
    public static boolean judgeControlTempute(String controlTempute) {
        if (StringUtil.isBlank(controlTempute)) {
            return false;
        }
        if (controlTempute.contains("-")) {
            String[] temputes = controlTempute.split("-");
            String tempute1 = temputes[0];
            String tempute2 = temputes[1];
            if (tempute1.length() == 2 && tempute2.length() == 2) {
                int t1 = Integer.parseInt(tempute1);
                int t2 = Integer.parseInt(tempute2);
                if (t1 >= 18 && t1 <= 32 && t2 >= 18 && t2 <= 32) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getRequestId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static JSONObject putFailedDesc(JSONObject obj, String fileddDesc) {
        if (obj == null) {
            obj = new JSONObject();
        }
        obj.put("result", "failed");
        obj.put("desc", fileddDesc);
        return obj;
    }

    public static int hexStringToInt(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        try {
            return Integer.parseInt(s, 16);
        } catch (Exception e) {
        }
        return 0;
    }
    
    /**   
     * @Modifier:lxf 
     * @Date：2018年10月25日上午10:18:01 
     * @Describe:reversal  float数值反转。即 42 40 00 00 字节对应的值，反转出 00 00 40 42 字节对应的值。    
     * @param source
     * @return                 
    */
    public static float reversal(float source) {
        int sourceInt =  Float.floatToIntBits(source);
        //转成4个字节的字符串。不足的补0
        String sourceStr = Integer.toHexString(sourceInt);
       // log.info("reversal.sourceStr="+sourceStr);
        int length = sourceStr.length(); 
        if(length<8) {
            for(int i=length;i<8;i++) {
                sourceStr = "0"+sourceStr;
            }
        }
        int b1 = hexStringToInt(sourceStr.substring(0, 2));
        int b2 = hexStringToInt(sourceStr.substring(2, 4));
        int b3 = hexStringToInt(sourceStr.substring(4, 6));
        int b4 = hexStringToInt(sourceStr.substring(6, 8));
        int targetInt = (b1 & 0xff) | ((b2 & 0xff) << 8)
                | ((b3 & 0xff) << 16) | ((b4 & 0xff) << 24);
        return Float.intBitsToFloat(targetInt);
    }
    public static void main(String[] args) {
        System.out.println(reversal(Float.intBitsToFloat(16450)));
    }
}
