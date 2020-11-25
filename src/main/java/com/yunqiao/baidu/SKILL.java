package com.yunqiao.baidu;

public enum SKILL {
    TRESPASSER("电子围栏", 10), STRANGER("陌生人检测", 12), CLIMBING("攀高检测", 13), LEAVE("离岗检测", 14),
    PEOPLENUM("人流过密预警", 15), SLEEP("睡岗检测", 16), ATTENTION("课堂专注度分析", 17),
    HELMET("安全帽佩戴合规检测", 18),SMOKEFIRE("烟火检测", 19), VEHICLE("车辆违停分析", 20);

    // 成员变量
    private String name;
    private int id;

    // 构造方法
    private SKILL(String name, int id) {
        this.name = name;
        this.id = id;
    }

    // 普通方法
    public static String getName(int id) {
        for (SKILL c : SKILL.values()) {
            if (c.id() == id) {
                return c.name;
            }
        }
        return null;
    }

    public static int id(String name) {
        for (SKILL c : SKILL.values()) {
            if (c.getName().equals(name)) {
                return c.id;
            }
        }
        return 0;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
