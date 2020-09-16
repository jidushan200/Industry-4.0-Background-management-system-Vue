package org.tsinghuatj.framework.utils.excel;

import org.joda.time.DateTime;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilsTest {

    public static void main(String[] args) {
        read();
//        export();
    }

    private static void read(){
        // 基于注解,将Excel内容读至List<Student2>对象内
        List<Student> students = null;

        try (InputStream inputStream = new ClassPathResource("test.xlsx").getInputStream()) {
            students = ExcelUtils.getInstance().readExcel2Objects(inputStream, Student.class, 1, 100, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("读取Excel至对象数组(支持类型转换)：");
        for (Student st : students) {
            System.out.println(st);
        }
    }

    private static void export(){
        List<Student> list = new ArrayList<>();
        list.add(new Student("1010001", DateTime.now().toDate(), "盖伦", "六年级三班"));
        list.add(new Student("1010002", DateTime.now().toDate(), "古尔丹", "一年级三班"));
        list.add(new Student("1010003", DateTime.now().toDate(), "蒙多(被开除了)", "六年级一班"));
        list.add(new Student("1010004", DateTime.now().toDate(), "萝卜特", "三年级二班"));
        list.add(new Student("1010005", DateTime.now().toDate(), "奥拉基", "三年级二班"));
        list.add(new Student("1010006", DateTime.now().toDate(), "得嘞", "四年级二班"));
        list.add(new Student("1010007", DateTime.now().toDate(), "瓜娃子", "五年级一班"));
        list.add(new Student("1010008", DateTime.now().toDate(), "战三", "二年级一班"));
        list.add(new Student("1010009", DateTime.now().toDate(), "李四", "一年级一班"));
        // 不基于模板导出Excel
        try {
            ExcelUtils.getInstance().exportObjects2Excel(list, Student.class, true, null, true, "D:/B.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
