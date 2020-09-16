package org.tsinghuatj.framework.utils.excel.utils;

import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.tsinghuatj.framework.utils.excel.annotation.ExcelField;
import org.tsinghuatj.framework.utils.excel.handler.ExcelHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;


public class Utils {

    /**
     * <p>根据JAVA对象注解获取Excel表头信息</p></br>
     */
    static
    public List<ExcelHeader> getHeaderList(Class<?> clz) {
        List<ExcelHeader> headers = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        for (Field field : fields) {
            // 是否使用ExcelField注解
            if (field.isAnnotationPresent(ExcelField.class)) {
                ExcelField er = field.getAnnotation(ExcelField.class);
                headers.add(new ExcelHeader(er.title(), er.order(), field.getName(), field.getType()));
            }
        }
        Collections.sort(headers);
        return headers;
    }

    static
    public Map<Integer, ExcelHeader> getHeaderMap(Row titleRow, Class<?> clz) {
        List<ExcelHeader> headers = getHeaderList(clz);
        Map<Integer, ExcelHeader> maps = new HashMap<>();
        for (Cell c : titleRow) {
            String title = c.getStringCellValue();
            for (ExcelHeader eh : headers) {
                if (eh.getTitle().equals(title.trim())) {
                    maps.put(c.getColumnIndex(), eh);
                    break;
                }
            }
        }
        return maps;
    }

    static
    public void outPutFile(Workbook wb, String outFilePath){
        FileOutputStream fos = null;
        try {
            File f = new File(outFilePath);
            if (f.getParentFile().isDirectory() && !f.getParentFile().exists()) {
                if(!f.mkdirs()){
                    throw new RuntimeException("创建文件夹失败 > " + outFilePath);
                }
            }
            if (!f.exists()) {
                if(!f.createNewFile()){
                    throw new RuntimeException("创建文件失败 > " + outFilePath);
                }
            }
            fos = new FileOutputStream(outFilePath);
            wb.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 修正Cell的类型
     */
    static
    public void fixCellType(Cell c, Class<?> clazz){
        CellType cellType = c.getCellTypeEnum();
    	if(clazz == String.class && CellType.STRING.compareTo(cellType) != 0){
    		c.setCellType(CellType.STRING);
    	}
    }

    static
    public String getCellValue(Cell c) {
        String o;
        switch (c.getCellTypeEnum()) {
            case BLANK:
                o = "";
                break;
            case BOOLEAN:
                o = String.valueOf(c.getBooleanCellValue());
                break;
            case FORMULA:
                o = String.valueOf(c.getCellFormula());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(c)) {
                    o = new DateTime(c.getDateCellValue()).toString();
                } else {
                    o = String.valueOf(c.getNumericCellValue());
                    o = matchDoneBigDecimal(o);
                    o = RegularUtils.converNumByReg(o);
                }
                break;
            case STRING:
                o = c.getStringCellValue();
                break;
            default:
                o = null;
                break;
        }
        return o;
    }
    
    static 
    public Object str2TargetClass(String strField, Class<?> clazz){
        if (null == strField || "".equals(strField))
            return null;
        if ((Long.class == clazz) || (long.class == clazz)) {
            strField = matchDoneBigDecimal(strField);
            strField = RegularUtils.converNumByReg(strField);
            return Long.parseLong(strField);
        }
        if ((Integer.class == clazz) || (int.class == clazz)) {
            strField = matchDoneBigDecimal(strField);
            strField = RegularUtils.converNumByReg(strField);
            return Integer.parseInt(strField);
        }
        if ((Float.class == clazz) || (float.class == clazz)) {
            strField = matchDoneBigDecimal(strField);
            return Float.parseFloat(strField);
        }
        if ((Double.class == clazz) || (double.class == clazz)) {
            strField = matchDoneBigDecimal(strField);
            return Double.parseDouble(strField);
        }
        if ((Character.class == clazz) || (char.class == clazz)) {
            return strField.toCharArray()[0];
        }
        if (Date.class == clazz) {
            return new DateTime(strField).toDate();
        }
        return strField;
    }

    private static String matchDoneBigDecimal(String bigDecimal){
        // 对科学计数法进行处理
        boolean flg = Pattern.matches("^-?\\d+(\\.\\d+)?(E-?\\d+)?$", bigDecimal);
        if (flg) {
            BigDecimal bd = new BigDecimal(bigDecimal);
            bigDecimal = bd.toPlainString();
        }
        return bigDecimal;
    }
}
