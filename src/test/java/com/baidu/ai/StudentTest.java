package com.baidu.ai;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentTest {

    @Test
    public void testSortWithStream() {


        List<Student> studentList = new ArrayList<>();

        studentList.add(0,new Student(Long.valueOf(27), "Li", Integer.valueOf(88)));
        studentList.add(0,new Student(Long.valueOf(30), "Zhang", Integer.valueOf(99)));
        studentList.add(0,new Student(Long.valueOf(2), "Zhang", Integer.valueOf(100)));
        studentList.add(0,new Student(Long.valueOf(2), "Shao", Integer.valueOf(99)));

        /*使用Java8 Stream order*/
        List<Student> sortedList = studentList.stream().sorted(Comparator.comparing(Student::getScore).reversed()).collect(Collectors.toList());
        System.out.println(studentList);
        System.out.println(sortedList);

        /*使用Java8 Stream order按照score、name逆序排序*/
        List<Student> sortedList1 = studentList.stream().sorted(Comparator.comparing(Student::getScore).thenComparing(Student::getName).reversed()).collect(Collectors.toList());
        System.out.println(sortedList1);

        //使用list的sort方法排序
        Comparator<Student> compareByScoreAndNameReverse = Comparator.comparing(Student::getScore).thenComparing(Student::getName).reversed();
        studentList.sort(compareByScoreAndNameReverse);
        System.out.println(studentList);

        String numbers = "12,21";
        String[] nList = numbers.split(",");

        System.out.println(nList.length);
    }

}
