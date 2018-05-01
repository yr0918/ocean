package com.xingyuan.refactoring.c6;

/**
 * <h2>提炼函数：</h2>你有一段代码可以被组织在一起并独立出来，将这段代码放进一个独立函数中，并让函数名称解释该函数的用途
 * <h2>动机：</h2>
 *
 * <h2>做法：</h2>
 *
 * @author yerong
 * @version 1.0
 * @since 1.0
 * 创建于18/5/2
 */
public class ExtractMethod {
    private String name;
    private String gender;
    private Float englishScore;
    private Float testScore;

    private void printInfo() {
        System.out.println("name:" + name);
        System.out.println("gender:" + gender);
    }

    //------before------
    private void printStudentScore() {
        printInfo();

        // print score
        System.out.println("englishScore:" + englishScore);
        System.out.println("testScore:" + testScore);
    }

    //------after------
    private void printScore() {
        System.out.println("englishScore:" + englishScore);
        System.out.println("testScore:" + testScore);
    }

    private void printStudentScoreRefactoring() {
        printInfo();
        printScore();
    }
}
