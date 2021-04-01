package business;

import business.Executor;

public class MainClass {

    public static void main(String []args){
        Executor executor = new Executor("Activities.txt");
        executor.execute();
    }
}
