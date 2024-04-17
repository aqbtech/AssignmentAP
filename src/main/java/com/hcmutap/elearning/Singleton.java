package com.hcmutap.elearning;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Singleton {
    private static Singleton instance = null;
    private boolean student_state;
    private boolean teacher_state;
    private Singleton(){
        this.student_state = this.teacher_state = false;
    }
    public static Singleton getInstance() {
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }

}
