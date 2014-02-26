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
 * �ַ���������
 * 
 * @author Recolar
 * @version 1.0.0 2006-11-02
 * @since JDK1.5.0
 */
public class StringUtil {

    private static Random rand = new Random();

    public static final String SPACE = " ";

    /**
     * ���з��������Ӧ�ļ�������
     */
    private static String jianTi;

    /**
     * ���з�������
     */
    private static String fanTi;

    private static StringUtil stringUtil = null;

    /**
     * ��ȡStringUtilʵ��
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
     * ��ȡ32λ���ȵ�����ַ���,���а������ֺ���ĸ�Ļ��<br>
     * 
     * @return 32λ���ȵ��ַ���
     */
    public static String getRandomStrBy32BitLength() {
        return getRandomStrByLenth(32);
    }

    /**
     * ��ȡ16λ���ȵ�����ַ���,���а������ֺ���ĸ�Ļ��<br>
     * 
     * @return 16λ���ȵ��ַ���
     */
    public static String getRandomStrBy16BitLength() {
        return getRandomStrByLenth(16);
    }

    /**
     * ��ȡ8λ���ȵ�����ַ���,���а������ֺ���ĸ�Ļ��<br>
     * 
     * @return 8λ���ȵ��ַ���
     */
    public static String getRandomStrBy8BitLength() {
        return getRandomStrByLenth(8);
    }

    /**
     * ���ݲ���Ҫ��ĳ��Ȼ�ȡ����ַ���,���а������ֺ���ĸ�Ļ��<br>
     * 
     * @param len ���Ȳ���
     * @return ����ַ���
     */
    public static String getRandomStrByLenth(int len) {
        StringBuffer str = new StringBuffer("");
        for (int i = 0; i < len; i++) {
            if (rand.nextBoolean()) {// ��������
                str.append(rand.nextInt(10));
            } else {
                str.append((char) (65 + rand.nextInt(26)));// �����ַ�
            }
        }
        return str.toString();
    }

    /**
     * ���ݲ�����ָ���ĳ���,��ȡ��������ַ���
     * 
     * @param len ��Ҫ��ȡ�ĳ���
     * @return String ��������ַ���
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
     * ��������
     */
    private static void loadFont() {
        InputStream fantiFileStream = StringUtil.class.getResourceAsStream(FILENAME_FANTI);
        fanTi = SysUtil.readStringFromInputStream(fantiFileStream, false);
        InputStream jiantiFileStream = StringUtil.class.getResourceAsStream(FILENAME_JIANTI);
        jianTi = SysUtil.readStringFromInputStream(jiantiFileStream, false);
    }

    /**
     * ���ַ����Ӽ���ת��Ϊ����<br>
     * 
     * @param str ��Ҫת���ļ����ַ���
     * @return ת����ķ����ַ���
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
     * ���ַ����ӷ���ת��Ϊ����<br>
     * 
     * @param str ��Ҫת���ķ����ַ���
     * @return ת����ļ����ַ���
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
     * ���ַ��������е�����Ϊnull��ֵȥ��,Ȼ�󷵻�һ��������nullֵ���µ��ַ�������
     * 
     * @param str �ַ�������
     * @return ���˺���ַ�������
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
     * ���ַ��������е�����Ϊ""��ֵȥ��,Ȼ�󷵻�һ��������""ֵ���µ��ַ�������
     * 
     * @param str �ַ�������
     * @return ���˺���ַ�������
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
     * ��List���󱣴��ֵת��Ϊ�ַ���������󲢷���
     * 
     * @param list List����
     * @return ת������ַ�������
     */
    public static String[] listToStrArray(List<Object> list) {
        String[] str = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            str[i] = (String) list.get(i);
        }
        return str;
    }

    /**
     * �����ַ�������������ַ�������Ϊnull�򷵻�"",���򷵻ر������
     * 
     * @param str ���˵��ַ�������
     * @return String ���˺�ķ���ֵ
     */
    public static String getFilterNull(String str) {
        return str == null ? "" : str;
    }

    /**
     * ���objΪnull�򷵻ؿ��ַ���"",���򷵻�str
     * 
     * @param obj ��������
     * @param str �����ַ���
     * @return String ���˺�ķ���ֵ
     */
    public static String getFilterNull(Object obj, String str) {
        return obj == null ? "" : str;
    }

    /**
     * �����ַ�������������ַ�����������strΪnull��filter",���򷵻ر������
     * 
     * @param str ���˵��ַ�������
     * @param filter ����ֵ
     * @return String ���˺�ķ���ֵ
     */
    public static String getFilterNull(String str, String filter) {
        return str == null ? filter : str;
    }

    /**
     * �����ַ�������,������ַ�����������strΪnull����Ϊ""�򷵻�filter�����򷵻ر���
     * 
     * @param str ���˵��ַ�������
     * @param filter ����ֵ
     * @return
     */
    public static String getFilterEmpty(String str, String filter) {
        return str == null ? filter : str.trim().length() == 0 ? filter : str;
    }

    /**
     * ��ȡ��������ַ���
     * 
     * @param length Ҫ�������������ַ����ĳ���
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
     * ������Ĳ����ĵ�һ����ĸ��д������
     * 
     * @param str String����
     * @return String����
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
     * ��ȡһ���ַ�����֮ǰ���Ǹ��ַ���
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
     * ��ȡһ���ַ�����֮����Ǹ��ַ���
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
     * ����������и�������toString�󲢳�һ���ַ���������,��������Ԫ��֮���÷��Ÿ���
     * 
     * @param value ��������
     * @param sliceStr �����ķ���
     * @return �ϲ�����ַ�������
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
     * ���ַ���ת��Ϊ����������ʽ���ַ���<br>
     * ����:aa,bb,ccת��Ϊ'aa','bb','cc',����ƴ��sql�����������<br>
     * where field in ('aa','bb','cc')
     * 
     * @param str
     * @param type �ֶ�����(1:�ַ���,2:����)
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
     * ����������ƴ�ճɹ���������ʽ��SQL<br>
     * ��11,22,33����һ������,�򷵻��ַ���:11,22,33
     * 
     * @param nums ��������
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
     * �����ό���List�е�String�����D�Q�����������ʽ��SQL<br>
     * ��11,22,33����һ������,�򷵻��ַ���:11,22,33
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
     * һ���ַ����Ƿ���һ���ַ��������г���,����Ƿ���true���򷵻�false
     * 
     * @param values �ַ�������
     * @param value ��֤�Ƿ���ֵ��ַ���
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
     * �����ṩ�Ĳ�����ȡSQL��ѯ�����Ӿ�<br>
     * 
     * @param filedName �ֶ�����(�ֶ��б�����ı�����б����,��:n1.typeid)
     * @param operator ������,��"=","Like",">=","<","in"
     * @param value �ֶ�ֵ
     * @param type �ֶ�����(1:�ַ��� 2:����)
     * @return ƴ��֮���SQL�Ӿ�,��:" And n1.billid<=999"
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
     * ���ַ�������ƴ�ճɹ���������ʽ��SQL<br>
     * ���ַ���aa,bb,cc����һ������,�򷵻��ַ���:'aa','bb','cc'
     * 
     * @param strArrays �ַ�������
     * @param type �ֶ�����(1:�ַ���,2:����)
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
     * �Ѽ��϶����е�Ԫ��ƴ�ճɹ���������ʽ��SQL<br>
     * ��������а�����Ԫ��aa,bb��cc,�򷵻��ַ���:'aa','bb','cc'
     * 
     * @param list Collection���϶���
     * @param type �ֶ�����(1:�ַ���,2:����)
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
     * �����ṩ�Ĳ�����ȡSQL��ѯ�����Ӿ�(Between..And..ģʽ)<br>
     * ��Ҫ�ǻ�ȡĳ���ֶεķ�Χ
     * 
     * @param filedName �ֶ�����(�ֶ��б�����ı�����б����,��:n1.typeid)
     * @param operator ������
     * @param value �ֶ�ֵ
     * @param type �ֶ�����(1:�ַ��� 2:����)
     * @return ƴ��֮���SQL�Ӿ�
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
     * ������������,���ص��ǹ��������Ӿ�,��:" And n1.ttime >=??? And n1.ttime<=??? "<br>
     * �����������Ϊnull�򷵻�>=��ʼ����,�����ʼ����Ϊnull�򷵻�<=��������
     * 
     * @param dateBegin ��ʼ����
     * @param dateEnd ��������
     * @param fieldName �����ֶ�����(����б�����ǵ�Ҫ�������,��:n1.ttime)
     * @return String �������ڵ�Sql���
     */
    public static String getDateCondition(String dateBegin, String dateEnd, String fieldName) {
        String dateCons = "";
        dateBegin = (dateBegin == null ? "" : dateBegin.trim());
        dateEnd = (dateEnd == null ? "" : dateEnd.trim());
        if (BaseConfig.getDATABASE_TYPE() == BaseConfig.DATABASE_ORACLE) {// Oracle
            if (dateBegin.length() > 0 || dateEnd.length() > 0) {
                if (dateBegin.length() == 0) {// �û�ֻ�����˽�������
                    dateCons = "\n And " + fieldName + " <= To_Date('" + dateEnd + "', 'yyyy-mm-dd')";
                } else if (dateEnd.length() == 0) {// �û�ֻ�����˿�ʼ����
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
                if (dateBegin.length() == 0) {// �û�ֻ�����˽�������
                    dateCons = "\n And " + fieldName + " <='" + dateEnd + "'";
                } else if (dateEnd.length() == 0) {// �û�ֻ�����˿�ʼ����
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
     * ������������,����ĳ���·��������·ݵĵ�һ�쵽���һ���������<br>
     * ��ʱ����֧��SQL SERVER
     * 
     * @param month �·ݲ���,��:"2007-08"
     * @param fieldName �����ֶ�����(����б�����ǵ�Ҫ�������,��:n1.ttime)
     * @return ����sql
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
     * ��ȡ�����·�����֮��Ĺ���sql<br>
     * 
     * @param monthFrom �·ݴ�
     * @param monthTo �·ݵ�
     * @param fieldName �ֶ�����
     * @return ����sql
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
     * ������������,����ĳ����������е���ĵ�һ�쵽���һ���������<br>
     * ��ʱ����֧��SQL SERVER
     * 
     * @param year ���
     * @param fieldName �����ֶ�����(����б�����ǵ�Ҫ�������,��:n1.ttime)
     * @return ����sql
     */
    public static String getDateYearCondition(String year, String fieldName) {
        if (year == null || year.trim().length() == 0) {
            return "";
        }
        final String tempStr = "And {0}>=to_date(''{1}-01-01'',''yyyy-mm-dd'') And {0}<=to_date(''{1}-12-31'',''yyyy-mm-dd'')";
        return MessageFormat.format(tempStr, new Object[] { fieldName, year });
    }

    /**
     * �����ּ���ǧ�ֺŲ��ҷ��ش��ַ���<br>
     * Ĭ�ϱ�����λ��Ч����
     * 
     * @param ��������
     * @return String
     */
    public static String formatDoubleDigit(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(d);
    }

    /**
     * �ж�һ���ַ����Ƿ�Ϊһ���ǿ��ַ���
     * 
     * @param value
     * @return ���value!=null&&value.trim().length()>0����True,���򷵻�False
     */

    public final static boolean isNotEmptyString(String value) {
        return value != null && value.trim().length() > 0;
    }

    /**
     * �ж�һ���ַ����Ƿ�Null����EmptyString,�ַ���ʹ��Trimȥ�����˿ո�
     * 
     * @param value
     * @return ���value==null||value.trim().length()==0 �򷵻�True ���򷵻�True
     */

    public final static boolean isNullOrTrimEmptyString(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * �ж�һ���ַ����Ƿ�Null����EmptyString,�ַ���������������ж�
     * 
     * @param value
     * @return ���value==null||value.length()==0 �򷵻�True ���򷵻�True
     */

    public final static boolean isNullOrEmptyString(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * �����ַ���ǽ��ַ�������Ϊ�ַ�������,��װ��List������
     * 
     * @param str String
     * @param regex �ַ������
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
     * ��List�����еĸ����ַ���Ԫ����װ��String�������鷵��
     * 
     * @param list List���϶���
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
     * ���һ��String�Ƿ�Ϊnull����empty��������һ�򷵻�true
     * 
     * @param str
     * @return
     */
    public static boolean checkNullAndEmptyString(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * ���һ��String�Ƿ�Ϊnull����Ϊtrim֮���empty��������һ�򷵻�true
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
     * ��ȡ��д��ĸ����ַ���
     * 
     * @param length �ַ�������
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
     * �����ṩ���ַ���������,�����Ӷ���ո��滻��һ���ո񲢷���<br>
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
     * ����һ�����ַ��������ַ����г��ֹ��Ĵ���
     * 
     * @param mainString ���ַ���
     * @param subString ���ַ���
     * @return ����
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
     * ���Email�ַ����ĸ�ʽ�Ƿ���ȷ
     * 
     * @param email �����ʼ��ַ���
     * @return �Ƿ���֤�ɹ�
     */
    public static boolean checkEmailFormatIsValid(String email) {
        if (email == null) {
            email = "";
        }
        return email.matches("\\w+@\\w+[.]{1}\\w+");
    }

    /**
     * �ö�����������ַ����е��ض��ַ���<br>
     * �� "Select * From :tableName Where IID=:iid",":tableName"��":iid" ������Ҫ������ض��ַ���
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
     * ����SqlServer �������ַ�����ʽ
     * 
     * @param date
     * @return
     */
    public static String getSqlDateString(Date date) {
        return SQLDATEFORMATTER.format(date);
    }

    /**
     * �ظ����Ӹ����ַ���,ʹ��joinStr���������ַ���.
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
     * ���ַ����������Ķ���ո��滻�ɵ����ո�<br>
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
