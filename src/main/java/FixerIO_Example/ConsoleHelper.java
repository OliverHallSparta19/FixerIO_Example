package FixerIO_Example;

public class ConsoleHelper {
    private String lastLine = "";

    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    private byte anim;

    public void animate(String line) {
        switch (anim) {
            case 1:
                print("[ \\ ] " + line);
                break;
            case 2:
                print("[ | ] " + line);
                break;
            case 3:
                print("[ / ] " + line);
                break;
            default:
                anim = 0;
                print("[ - ] " + line);
        }
        anim++;
    }

//    public static void main(String[] args) throws InterruptedException {
//        ConsoleHelper consoleHelper = new ConsoleHelper();
//        for (int i = 0; i < 101; i++) {
//            consoleHelper.animate(i + "%");
//            //simulate a piece of task
//            Thread.sleep(550);
//        }
//    }



        public static void main(String[] args) {
            char[] animationChars = new char[]{'|', '/', '-', '\\'};

            for (int i = 0; i <= 100; i++) {
                System.out.print("Processing: " + i + "% " + animationChars[i % 4] + "\r");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Processing: Done!          ");
        }


}