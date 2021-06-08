package utility;

public class InputChek {
    /** Check whether the data is correct of type double or not*/
    public boolean DoubleInvalidCheck(String s, double min, double max){
        try{
            double x = Double.parseDouble(s);
            if (x - min >0 && x<max) return true;
            System.out.println("Input is invalid. Please enter the Double in correct range.");
            return false;
        }catch (NumberFormatException e){
            System.out.println("input invalid. Please enter a Double number.");
            return false;
        }
    }
    /**Check whether the data is correct of type Integer or not*/
    public boolean IntegerValidCheck(String s,int min, int max){
        try{
            int x = Integer.parseInt(s);
            if (x > min && x < max ) return true;
            System.out.println("input invalid. Please enter the Integer incorrect range.");
            return false;
        }catch (NumberFormatException e){
            System.out.println("input is invalid.");
            return false;
        }
    }
    /**Check whether the data is correct of type Integer or not*/
    public boolean longInValidCheck(String s,Long min, Long max){
        try{
            if (Long.parseLong(s) > min && Long.parseLong(s) < max) return true;
            System.out.println("id invalid. Please enter the long number in correct range");
            return false;
        }catch (NumberFormatException e){
            Outputer.printerror("This field data entered is incorrect. Please enter a Long number");
            e.printStackTrace();
            return false;
        }
    }
}
