package com.rec.service;

import java.io.InputStream;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import com.rec.web.dbutil.BaseConfig;

/**
 * 字符串工具类
 * 
 * @author Recolar
 * @version 1.0.0 2006-11-02
 * @since JDK1.5.0
 */
public class StringUtil {

    private static Random rand = new Random();

    public static final String SPACE = " ";

    /**
     * 所有繁体字体对应的简体字体
     */
    private static String jianTi;

    /**
     * 所有繁体字体
     */
    private static String fanTi;

    private static StringUtil stringUtil = null;

    /**
     * 获取StringUtil实例
     * 
     * @return StringUtil
     */
    public static StringUtil getCurrentInstance() {
        if (stringUtil == null)
            stringUtil = new StringUtil();
        return stringUtil;
    }

    private StringUtil() {
    }

    /**
     * 获取32位长度的随机字符串,当中包含数字和字母的混合<br>
     * 
     * @return 32位长度的字符串
     */
    public static String getRandomStrBy32BitLength() {
        return getRandomStrByLenth(32);
    }

    /**
     * 获取16位长度的随机字符串,当中包含数字和字母的混合<br>
     * 
     * @return 16位长度的字符串
     */
    public static String getRandomStrBy16BitLength() {
        return getRandomStrByLenth(16);
    }

    /**
     * 获取8位长度的随机字符串,当中包含数字和字母的混合<br>
     * 
     * @return 8位长度的字符串
     */
    public static String getRandomStrBy8BitLength() {
        return getRandomStrByLenth(8);
    }

    /**
     * 根据参数要求的长度获取随机字符串,当中包含数字和字母的混合<br>
     * 
     * @param len 长度参数
     * @return 随机字符串
     */
    public static String getRandomStrByLenth(int len) {
        StringBuffer str = new StringBuffer("");
        for (int i = 0; i < len; i++) {
            if (rand.nextBoolean()) {// 产生数字
                str.append(rand.nextInt(10));
            } else {
                str.append((char) (65 + rand.nextInt(26)));// 产生字符
            }
        }
        return str.toString();
    }

    /**
     * 根据参数所指定的长度,获取随机数字字符串
     * 
     * @param len 需要获取的长度
     * @return String 随机数字字符串
     */
    public static final String getRandomNumberString(int len) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int num = rand.nextInt(10);
            if (i == 0 && num == 0) {
                num = 1;
            }
            str.append(num);
        }
        return str.toString();
    }

    private static boolean isLoadFont = false;

    private static final String FILENAME_JIANTI = "jianti.font.txt";

    private static final String FILENAME_FANTI = "fanti.font.txt";

    /**
     * 加载字体
     */
    private static void loadFont() {
        InputStream fantiFileStream = StringUtil.class.getResourceAsStream(FILENAME_FANTI);
        fanTi = SysUtil.readStringFromInputStream(fantiFileStream, false);
        InputStream jiantiFileStream = StringUtil.class.getResourceAsStream(FILENAME_JIANTI);
        jianTi = SysUtil.readStringFromInputStream(jiantiFileStream, false);
    }

    /**
     * 将字符串从简体转换为繁体<br>
     * 
     * @param str 需要转换的简体字符串
     * @return 转换后的繁体字符串
     */
    public static String changeGBKToBig5(String str) {
        if (!isLoadFont) {
            loadFont();
            isLoadFont = true;
        }
        StringBuffer su = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int index = jianTi.indexOf(String.valueOf(c));
            su.append(index == -1 ? c : fanTi.charAt(index));
        }
        return su.toString();
    }

    /**
     * 将字符串从繁体转换为简体<br>
     * 
     * @param str 需要转换的繁体字符串
     * @return 转换后的简体字符串
     */
    public static String changeBig5ToGBK(String str) {
        if (!isLoadFont) {
            loadFont();
            isLoadFont = true;
        }
        StringBuffer su = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int index = fanTi.indexOf(String.valueOf(c));
            su.append(index == -1 ? c : jianTi.charAt(index));
        }
        return su.toString();
    }

    /**
     * 把字符串数组中的所有为null的值去掉,然后返回一个不包含null值的新的字符串数组
     * 
     * @param str 字符串数组
     * @return 过滤后的字符串数组
     */
    public static String[] filterArrayOutOfNull(String[] str) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < str.length; i++) {
            if (str[i] != null) {
                list.add(str[i]);
            }
        }
        String[] retStr = new String[list.size()];
        int index = 0;
        for (Iterator<Object> i = list.iterator(); i.hasNext();) {
            retStr[index++] = (String) i.next();
        }
        return retStr;
    }

    /**
     * 把字符串数组中的所有为""的值去掉,然后返回一个不包含""值的新的字符串数组
     * 
     * @param str 字符串数组
     * @return 过滤后的字符串数组
     */
    public static String[] filterArrayOutOfEmpty(String[] str) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < str.length; i++) {
            if (str[i].trim().length() > 0) {
                list.add(str[i]);
            }
        }
        String[] retStr = new String[list.size()];
        int index = 0;
        for (Iterator<Object> i = list.iterator(); i.hasNext();) {
            retStr[index++] = (String) i.next();
        }
        return retStr;
    }

    /**
     * 将List对象保存的值转换为字符串数组对象并返回
     * 
     * @param list List对象
     * @return 转换后的字符串对象
     */
    public static String[] listToStrArray(List<Object> list) {
        String[] str = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            str[i] = (String) list.get(i);
        }
        return str;
    }

    /**
     * 过滤字符串对象，如果该字符串对象为null则返回"",否则返回本身对象
     * 
     * @param str 过滤的字符串对象
     * @return String 过滤后的返回值
     */
    public static String getFilterNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * 如果obj为null则返回空字符串"",否则返回str
     * 
     * @param obj 参数对象
     * @param str 参数字符串
     * @return String 过滤后的返回值
     */
    public static String getFilterNull(Object obj, String str) {
        return obj == null ? "" : str;
    }

    /**
     * 过滤字符串对象，如果该字符串参数对象str为null则返filter",否则返回本身对象
     * 
     * @param str 过滤的字符串对象
     * @param filter 过滤值
     * @return String 过滤后的返回值
     */
    public static String getFilterNull(String str, String filter) {
        return str == null ? filter : str;
    }

    /**
     * 过滤字符串对象,如果该字符串参数对象str为null或者为""则返回filter，否则返回本身
     * 
     * @param str 过滤的字符串对象
     * @param filter 过滤值
     * @return
     */
    public static String getFilterEmpty(String str, String filter) {
        return str == null ? filter : str.trim().length() == 0 ? filter : str;
    }

    /**
     * 获取随机中文字符串
     * 
     * @param length 要求的随机的中文字符串的长度
     * @return String
     */
    public static String getRandomChineseCode(int length) {
        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < length; i++) {
            randStr.append((char) (19968 + rand.nextInt(20902)));
        }
        return randStr.toString();
    }

    /**
     * 将传入的参数的第一个字母大写并返回
     * 
     * @param str String对象
     * @return String对象
     */
    public static String initCap(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() < 2) {
            return str.toUpperCase();
        }
        String fc = String.valueOf(str.charAt(0));
        return fc.toUpperCase() + str.substring(1);
    }

    /**
     * 获取一个字符串中之前的那个字符串
     * 
     * @param value
     * @param separater
     * @return
     */
    public static String getBeginStr(String value, String separater) {
        String result = value;
        int pos = value.indexOf(separater);
        if (pos != -1) {
            result = value.substring(0, pos);
        }
        return result;
    }

    /**
     * 获取一个字符串中之后的那个字符串
     * 
     * @param value
     * @param separater
     * @return
     */
    public static String getEndStr(String value, String separater) {
        String result = value;
        int pos = value.indexOf(separater);
        if (pos != -1) {
            result = value.substring(pos + separater.length());
        }
        return result;
    }

    /**
     * 对象数组合中各个对象toString后并成一个字符串并返回,各个数组元素之间用符号隔开
     * 
     * @param value 对象数组
     * @param sliceStr 隔开的符号
     * @return 合并后的字符串数组
     */
    public static String arrayToStr(Object[] value, String sliceStr) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            str.append(value[i]);
            if (i < value.length - 1) {
                str.append(sliceStr);
            }
        }
        return str.toString();
    }

    /**
     * 把字符串转换为过滤条件形式的字符串<br>
     * 例如:aa,bb,cc转换为'aa','bb','cc',用于拼凑sql过滤条件语句<br>
     * where field in ('aa','bb','cc')
     * 
     * @param str
     * @param type 字段类型(1:字符串,2:数字)
     * @return String
     */
    public static String changeStrToConditionStr(String str, int type) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        StringTokenizer st = new StringTokenizer(str, ",");
        while (st.hasMoreTokens()) {
            if (type == FILTER_TYPE_STRING) {
                sb.append("'" + st.nextToken() + "',");
            } else if (type == FILTER_TYPE_INT) {
                sb.append(st.nextToken() + ",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 把整型数组拼凑成过滤条件形式的SQL<br>
     * 如11,22,33构成一个数组,则返回字符串:11,22,33
     * 
     * @param nums 整形数组
     * @return String
     */
    public static String arrayToConditionStr(int[] nums) {
        if (nums == null || nums.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nums.length; i++) {
            sb.append(nums[i] + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 將集合對象List中的String對象轉換為过滤条件形式的SQL<br>
     * 如11,22,33构成一个数组,则返回字符串:11,22,33
     * 
     * @param list
     * @return
     */
    public static String listToConditionStr(List<String> list, int type) {
        if (type == FILTER_TYPE_INT) {
            int[] nums = new int[list.size()];
            for (int i = 0, len = list.size(); i < len; i++) {
                nums[i] = Integer.parseInt(list.get(i));
            }
            return arrayToConditionStr(nums);
        } else {
            return arrayToConditionStr(list.toArray(new String[] {}), type);
        }
    }

    /**
     * 一个字符串是否在一个字符串数组中出现,如果是返回true否则返回false
     * 
     * @param values 字符串数组
     * @param value 验证是否出现的字符串
     * @return boolean
     */
    public static boolean valueExists(String values[], String value) {
        boolean result = false;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && values[i].equals(value)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static final int FILTER_TYPE_STRING = 1;

    public static final int FILTER_TYPE_INT = 2;

    public static final int FILTER_TYPE_DATE = 3;

    /**
     * 根据提供的参数获取SQL查询过滤子句<br>
     * 
     * @param filedName 字段名称(字段有表别名的必须带有表别名,如:n1.typeid)
     * @param operator 操作符,如"=","Like",">=","<","in"
     * @param value 字段值
     * @param type 字段类型(1:字符串 2:数字)
     * @return 拼凑之后的SQL子句,如:" And n1.billid<=999"
     */
    public static String getQueryFilterSQL(String fieldName, String operator, String value, int type) {
        StringBuffer sql = new StringBuffer("");
        operator = operator.trim();
        if (value != null && value.trim().length() > 0) {
            if (type == FILTER_TYPE_STRING) {
                if ("LIKE".equals(operator.toUpperCase())) {
                    sql.append("\n And ").append(fieldName).append(" LIKE '%" + value + "%'");
                } else if ("IN".equals(operator.toUpperCase())) {
                    sql.append("\n And ").append(fieldName).append(" In (").append(value).append(")");
                } else {
                    sql.append("\n And ").append(fieldName).append(operator).append("'").append(value).append("'");
                }
            } else if (type == FILTER_TYPE_INT) {
                if ("IN".equals(operator.toUpperCase())) {
                    sql.append("\n And ").append(fieldName).append(" In (").append(value).append(")");
                } else {
                    sql.append("\n And ").append(fieldName).append(operator).append(value);
                }
            }
        }
        return sql.toString();
    }

    /**
     * 把字符串数组拼凑成过滤条件形式的SQL<br>
     * 如字符串aa,bb,cc构成一个数组,则返回字符串:'aa','bb','cc'
     * 
     * @param strArrays 字符串数组
     * @param type 字段类型(1:字符串,2:数字)
     * @return String
     */
    public static String arrayToConditionStr(String[] strArrays, int type) {
        if (strArrays == null || strArrays.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strArrays.length; i++) {
            if (strArrays[i] != null || strArrays[i].length() > 0) {
                if (type == FILTER_TYPE_STRING) {
                    sb.append("'" + strArrays[i] + "',");
                } else if (type == FILTER_TYPE_INT) {
                    sb.append(strArrays[i] + ",");
                }
            }
        }
        if (sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 把集合对象中的元素拼凑成过滤条件形式的SQL<br>
     * 如果集合中包含了元素aa,bb和cc,则返回字符串:'aa','bb','cc'
     * 
     * @param list Collection集合对象
     * @param type 字段类型(1:字符串,2:数字)
     * @return String
     */
    public static String collectionToConditionStr(Collection<Object> list, int type) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");
        for (Iterator<Object> i = list.iterator(); i.hasNext();) {
            Object obj = i.next();
            if (obj instanceof String) {
                String param = (String) obj;
                if (type == FILTER_TYPE_STRING) {
                    sb.append("'").append(param).append("'").append(",");
                } else if (type == FILTER_TYPE_INT) {
                    sb.append(param).append(",");
                }
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 根据提供的参数获取SQL查询过滤子句(Between..And..模式)<br>
     * 主要是获取某个字段的范围
     * 
     * @param filedName 字段名称(字段有表别名的必须带有表别名,如:n1.typeid)
     * @param operator 操作符
     * @param value 字段值
     * @param type 字段类型(1:字符串 2:数字)
     * @return 拼凑之后的SQL子句
     */
    public static String getQueryBetweenFilterSQL(String fieldName, String beginValue, String endValue, int type) {
        StringBuffer sql = new StringBuffer("");
        beginValue = (beginValue == null ? "" : beginValue.trim());
        endValue = (endValue == null ? "" : endValue.trim());
        if (beginValue.length() == 0 && endValue.length() == 0) {
            return "";
        }
        if (beginValue.length() > 0 || endValue.length() > 0) {
            if (beginValue.length() == 0) {
                sql.append(getQueryFilterSQL(fieldName, "<=", endValue, type));
            } else if (endValue.length() == 0) {
                sql.append(getQueryFilterSQL(fieldName, ">=", beginValue, type));
            } else {
                if (type == FILTER_TYPE_STRING) {
                    sql.append("\n And ").append(fieldName).append(" Between '").append(beginValue).append("' And '")
                            .append(endValue).append("'");
                } else if (type == FILTER_TYPE_INT) {
                    sql.append("\n And ").append(fieldName).append(" Between ").append(beginValue).append(" And ")
                            .append(endValue);
                }
            }
        }
        return sql.toString();
    }

    /**
     * 过滤日期条件,返回的是过滤条件子句,如:" And n1.ttime >=??? And n1.ttime<=??? "<br>
     * 如果结束日期为null则返回>=开始日期,如果开始日期为null则返回<=结束日期
     * 
     * @param dateBegin 开始日期
     * @param dateEnd 结束日期
     * @param fieldName 日期字段名称(如果有表别名记得要带表别名,如:n1.ttime)
     * @return String 过滤日期的Sql语句
     */
    public static String getDateCondition(String dateBegin, String dateEnd, String fieldName) {
        String dateCons = "";
        dateBegin = (dateBegin == null ? "" : dateBegin.trim());
        dateEnd = (dateEnd == null ? "" : dateEnd.trim());
        if (BaseConfig.getDATABASE_TYPE() == BaseConfig.DATABASE_ORACLE) {// Oracle
            if (dateBegin.length() > 0 || dateEnd.length() > 0) {
                if (dateBegin.length() == 0) {// 用户只输入了结束日期
                    dateCons = "\n And " + fieldName + " <= To_Date('" + dateEnd + "', 'yyyy-mm-dd')";
                } else if (dateEnd.length() == 0) {// 用户只输入了开始日期
                    dateCons = "\n And " + fieldName + " >= To_Date('" + dateBegin + "','yyyy-mm-dd')";
                } else {
                    if (dateBegin.equals(dateEnd)) {
                        dateCons = "\n And " + fieldName + " = To_Date('" + dateBegin + "','yyyy-mm-dd')";
                    } else {
                        dateCons = "\n And " + fieldName + " >= To_Date('" + dateBegin + "','yyyy-mm-dd')";
                        dateCons += " And " + fieldName + " <= To_Date('" + dateEnd + "', 'yyyy-mm-dd')";
                    }
                }
            }
        } else if (BaseConfig.getDATABASE_TYPE() == BaseConfig.DATABASE_SQLSERVER) {// SQLServer
            if (dateBegin.length() > 0 || dateEnd.length() > 0) {
                if (dateBegin.length() == 0) {// 用户只输入了结束日期
                    dateCons = "\n And " + fieldName + " <='" + dateEnd + "'";
                } else if (dateEnd.length() == 0) {// 用户只输入了开始日期
                    dateCons = "\n And " + fieldName + " >='" + dateBegin + "'";
                } else {
                    if (dateBegin.equals(dateEnd)) {
                        dateCons = "\n And " + fieldName + " ='" + dateBegin + "'";
                    } else {
                        dateCons = "\n And " + fieldName + " Between '" + dateBegin + "' And '" + dateEnd + "'";
                    }
                }
            }
        }
        return dateCons;
    }

    /**
     * 过滤日期条件,根据某个月份来进行月份的第一天到最后一天过滤日期<br>
     * 暂时还不支持SQL SERVER
     * 
     * @param month 月份参数,如:"2007-08"
     * @param fieldName 日期字段名称(如果有表别名记得要带表别名,如:n1.ttime)
     * @return 过滤sql
     */
    public static String getDateMonthCondition(String month, String fieldName) {
        if (month == null || month.length() == 0) {
            return "";
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" And " + fieldName + ">=To_Date('" + month + "-01','yyyy-mm-dd')").append(
                " And " + fieldName + "<=Last_Day(To_Date('" + month + "', 'yyyy-mm'))");
        return sql.toString();
    }

    /**
     * 获取两个月份日期之间的过滤sql<br>
     * 
     * @param monthFrom 月份从
     * @param monthTo 月份到
     * @param fieldName 字段名称
     * @return 过滤sql
     */
    public static String getDateMonthBetweenCondition(String monthFrom, String monthTo, String fieldName) {
        StringBuffer sql = new StringBuffer("");
        // monthFrom = (monthFrom == null ? "" : monthFrom + "-01");
        monthFrom = (monthFrom == null ? "" : (monthFrom.trim().length() == 0 ? "" : monthFrom + "-01"));
        monthTo = (monthTo == null ? "" : monthTo);
        if (monthFrom.length() > 0 || monthTo.length() > 0) {
            if (monthFrom.length() == 0) {
                sql.append(" And ").append(fieldName).append("<=Last_Day(To_Date('" + monthTo + "', 'yyyy-mm'))");
            } else if (monthTo.length() == 0) {
                sql.append(" And ").append(fieldName).append(">=To_Date('").append(monthFrom).append("','yyyy-mm-dd')");
            } else {
                sql.append(" And ").append(fieldName).append(">=To_Date('").append(monthFrom).append("','yyyy-mm-dd')");
                sql.append(" And ").append(fieldName).append("<=Last_Day(To_Date('" + monthTo + "', 'yyyy-mm'))");
            }
        }
        return sql.toString();
    }

    /**
     * 过滤日期条件,根据某个年份来进行当年的第一天到最后一天过滤日期<br>
     * 暂时还不支持SQL SERVER
     * 
     * @param year 年份
     * @param fieldName 日期字段名称(如果有表别名记得要带表别名,如:n1.ttime)
     * @return 过滤sql
     */
    public static String getDateYearCondition(String year, String fieldName) {
        if (year == null || year.trim().length() == 0) {
            return "";
        }
        final String tempStr = "And {0}>=to_date(''{1}-01-01'',''yyyy-mm-dd'') And {0}<=to_date(''{1}-12-31'',''yyyy-mm-dd'')";
        return MessageFormat.format(tempStr, new Object[] { fieldName, year });
    }

    /**
     * 把数字加上千分号并且返回此字符串<br>
     * 默认保留两位有效数字
     * 
     * @param 浮点数字
     * @return String
     */
    public static String formatDoubleDigit(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(d);
    }

    /**
     * 判断一个字符串是否为一个非空字符串
     * 
     * @param value
     * @return 如果value!=null&&value.trim().length()>0返回True,否则返回False
     */

    public final static boolean isNotEmptyString(String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * 判断一个字符串是否Null或者EmptyString,字符串使用Trim去掉两端空格
     * 
     * @param value
     * @return 如果value==null||value.trim().length()==0 则返回True 否则返回True
     */

    public final static boolean isNullOrTrimEmptyString(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 判断一个字符串是否Null或者EmptyString,字符串不作处理进行判断
     * 
     * @param value
     * @return 如果value==null||value.length()==0 则返回True 否则返回True
     */

    public final static boolean isNullOrEmptyString(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 根据字符标记将字符串劈开为字符串数组,组装成List并返回
     * 
     * @param str String
     * @param regex 字符串标记
     * @return List
     */
    public final static List<Object> splitStringToList(String str, String regex) {
        List<Object> list = new ArrayList<Object>();
        if (str == null || str.trim().length() == 0) {
            return list;
        }
        String[] s = str.split(regex);
        for (int i = 0; i < s.length; i++) {
            list.add(s[i]);
        }
        return list;
    }

    /**
     * 将List对象中的各个字符串元素组装成String对象数组返回
     * 
     * @param list List集合对象
     * @return String[]
     */
    public static String[] listToStringArray(List<Object> list) {
        String[] sa = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj instanceof String) {
                sa[i] = (String) obj;
            }
        }
        return sa;
    }

    /**
     * 检查一个String是否为null或者empty，满足其一则返回true
     * 
     * @param str
     * @return
     */
    public static boolean checkNullAndEmptyString(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 检查一个String是否为null或者为trim之后的empty，满足其一则返回true
     * 
     * @param str
     * @return
     */
    public static boolean checkNullAndEmptyTrimString(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static final int CHAR_UPPERCASE = 1;

    public static final int CHAR_LOWERCASE = 2;

    public static final int CHAR_RANDOMCASE = 3;

    /**
     * 获取大写字母随机字符串
     * 
     * @param length 字符串长度
     * @return String
     */
    public static final String getRandomCharString(int length, int charCase) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (charCase == CHAR_LOWERCASE) {
                sb.append((char) (rand.nextInt(26) + 97));
            } else if (charCase == CHAR_UPPERCASE) {
                sb.append((char) (rand.nextInt(26) + 65));
            } else {
                sb.append((char) (rand.nextInt(26) + (rand.nextBoolean() ? 97 : 65)));
            }
        }
        return sb.toString();
    }

    /**
     * 在所提供的字符串参数中,将连接多个空格替换成一个空格并返回<br>
     * 
     * @param srcString
     * @return String
     */
    public static String filterStringFromSpaceString(String srcString) {
        StringBuffer str = new StringBuffer();
        char[] chrs = srcString.toCharArray();
        boolean canFetch = true;
        for (char c : chrs) {
            if (c != ' ') {
                str.append(c);
                canFetch = true;
            } else {
                if (canFetch) {
                    str.append(c);
                }
                canFetch = false;
            }
        }
        return str.toString();
    }

    /**
     * 计算一个子字符串在主字符串中出现过的次数
     * 
     * @param mainString 主字符串
     * @param subString 子字符串
     * @return 次数
     */
    public static int getStringAppearCountInMainString(String mainString, String subString) {
        if (mainString == null || subString == null) {
            return 0;
        }
        int count = 0;
        int fromIndex = 0;
        int temp = 0;
        while ((temp = mainString.indexOf(subString, fromIndex)) > -1) {
            count++;
            fromIndex = temp + subString.length();
        }
        return count;
    }

    /**
     * 检查Email字符串的格式是否正确
     * 
     * @param email 电子邮件字符串
     * @return 是否验证成功
     */
    public static boolean checkEmailFormatIsValid(String email) {
        if (email == null) {
            email = "";
        }
        return email.matches("\\w+@\\w+[.]{1}\\w+");
    }

    /**
     * 用多个参数代替字符串中的特定字符串<br>
     * 如 "Select * From :tableName Where IID=:iid",":tableName"和":iid" 就是需要代替的特定字符串
     * 
     * @param f
     * @return
     */
    public static String replaceFlagString(String f, String[] flags, Object... objs) {
        for (int i = 0; i < flags.length; i++) {
            String flag = flags[i];
            String value = objs[i].toString();
            f = f.replaceAll(flag, value);
        }
        return f;
    }

    public static List<String> stringArrayToList(String[] stringArray) {
        List<String> list = new ArrayList<String>();
        for (String s : stringArray) {
            list.add(s);
        }
        return list;
    }

    private static final SimpleDateFormat SQLDATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 返回SqlServer 的日期字符串格式
     * 
     * @param date
     * @return
     */
    public static String getSqlDateString(Date date) {
        return SQLDATEFORMATTER.format(date);
    }

    /**
     * 重复连接给定字符串,使用joinStr隔开各个字符串.
     * 
     * @param str
     * @param joinStr
     * @param size
     * @return
     */
    public static String joinStrBySize(String str, String joinStr, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(str).append(joinStr);
        }
        return sb.deleteCharAt(sb.length() - 1).toString();

    }

    /**
     * 将字符串中连续的多个空格替换成当个空格<br>
     * 
     * @param s
     * @return
     */
    public static String trimManySpaceToOneSpace(String s) {
        // while (s.indexOf(SPACE + SPACE) > -1) {
        // s = s.replaceAll(SPACE + SPACE, SPACE);
        // }
        // return s;
        return s.replaceAll("[ ]{2,}", " ");
    }

    public static void main(String[] args) {
        int count = StringUtil.getStringAppearCountInMainString("OAASUGAOUDAOADFAOUSASOAHD", "A");
        System.out.println(count);
        System.out.println(trimManySpaceToOneSpace("So   A  H sd      DD                    OSH a     AH"));
        System.out.println(StringUtil.joinStrBySize("?", ",", 5));
    }

}
