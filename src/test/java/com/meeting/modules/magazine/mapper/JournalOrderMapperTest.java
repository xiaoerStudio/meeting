package com.meeting.modules.magazine.mapper;


public class JournalOrderMapperTest {

    private static Integer count = 707829217;

    public static void main(String[] args) {
        for (int i = 3; i < Math.sqrt(count); i++) {
            if (isPrimne(i)) {
                if (count % i == 0) {
                    int v = count / i;
                    if (isPrimne(v)){
                        System.out.println(i);
                        System.out.println(v);
                    }
                }
            }
        }
    }

    private static boolean isPrimne(int a) {
        boolean flag = true;
        if (a < 2) {
            return false;
        } else {
            for (int i = 2; i <= Math.sqrt(a); i++) {
                if (a % i == 0) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

}