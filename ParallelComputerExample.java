package MeetingRecorder;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

public class ParallelComputerExample {

    @Test
    public void runAllTests() {
        Class<?>[] classes = { ParallelTest1.class, ParallelTest2.class };

        // ParallelComputer(true,true) will run all classes and methods 
        // in parallel.  (First arg for classes, second arg for methods)
        JUnitCore.runClasses(new ParallelComputer(true, true), classes);
    }

    public static class ParallelTest1 {
        @Test
        public void test1a() {
        	
        	System.out.println("Hi test1a !!" );
            lookBusy(3000);
        }

        @Test
        public void test1b() {
        	
        	System.out.println("Hi test1b !!" );
            lookBusy(3000);
        }
    }

    public static class ParallelTest2 {
        
    	@Test
        public void test2a() {
        	
        	System.out.println("Hi test2a !!" );
            lookBusy(3000);
        }

        @Test
        public void test2b() {
        	
        	System.out.println("Hi test2b !!" );
            lookBusy(3000);
        }
    }

    public static void lookBusy(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }
}