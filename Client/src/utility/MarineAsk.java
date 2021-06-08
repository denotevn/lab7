package utility;

import App.ApplicationClient;
import data.*;
import exception.NoContentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;

public class MarineAsk {
    private final long MIN_ID = 0;
    private final long MIN_HEALTH = 0;
    private final int MAX_Y = 4000;

    private BufferedReader userReader;
    private boolean fileMode;

    InputChek inputChek = new InputChek();
    BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
    /**@author Dinh Ngoc Tuan
     * New Space Marine Initialization Method
     * @return new SpaceMarine*/
    public MarineAsk(BufferedReader userReader){
        this.userReader = userReader;
        fileMode = false;
    }

    /**
     * Sets marine asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets marine asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**Insert Id
     * @return id*/
    public Long idAsker(){
        Random random = new Random();
        System.out.println("Insert Id : ");
        Long newId = random.nextLong();
        if (newId == 0 || newId > MIN_ID ){
            System.out.println("input invalid. Please enter value > 0");
            return idAsker();
        }else{
            return newId;
        }
    }
    /**insert coordinate x
     @return int x*/
    public int askX() throws IOException {
        String strX;
        int x;
        while (true){
            Outputer.println("Enter the value of x: ");
            strX = bfr.readLine().trim();
            if (inputChek.IntegerValidCheck(strX,Integer.MIN_VALUE,Integer.MAX_VALUE) == false) throw new NumberFormatException();
            x = Integer.parseInt(strX);
            break;
        }
        return x;
    }
    /**insert  coordinate y
     @return double x*/
    public int askY() throws IOException {
        String strY;
        int y;
        while(true){
            Outputer.println("Enter the value of y: ");
            strY = bfr.readLine().trim();
            if (inputChek.IntegerValidCheck(strY,Integer.MIN_VALUE,MAX_Y) == false) throw new NumberFormatException();
            y = Integer.parseInt(strY);
            break;
        }
        return y;
    }

    /**Insert coordinates*/
    /**@return Marine's coordinate*/
    public Coordinates coordinatesAsker() throws IOException {
        int x;
        int y;
        x = askX();
        y = askY();
        return new Coordinates(x,y);
    }

    public java.util.Date dateAsker(){
        Date date = java.util.Calendar.getInstance().getTime();
        return date;
    }
    /**create field health*/
    public Long healthAsker() throws IOException {
        Long health;
        while(true){
            Outputer.println("Enter the value of field health: ");
            health = Long.valueOf(bfr.readLine());
            if (health<= MIN_HEALTH){
                System.out.println("health must be greater than 0");
                System.out.println("Re-insert health information: ");
                continue;
            }else{
                break;
            }
        }
        return  health;
    }
    /**Insert name of Collection or chapter
     * @return name*/
    public String nameAsker() throws IOException {
        String name;
        while (true){
            try{
                System.out.println("Insert name chapter or name Marine: ");
                Outputer.println(ApplicationClient.PS2);
                name = bfr.readLine().trim();
                if(name.equalsIgnoreCase(null)||name.equalsIgnoreCase(""))
                {
                    System.out.println("Field cannot be null, String cannot be empty");
                    System.out.println("Insert name chapter or name marine: ");
                    continue;
                }else
                    break;
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
        return name;
    }
    /**input parentLegion in Chapter*/
    /**@return parentLegion*/
    public String legionAsker() throws IOException {
        Outputer.println("Insert parentLegion: ");
        String s = bfr.readLine();
        while (s.equalsIgnoreCase(null)||s.equalsIgnoreCase("")){
            System.out.println("Field cannot be null, String cannot be empty");
            s = bfr.readLine();
        }
        return s;
    }
    /**
     * Asks a user the marine's category.
     * @return Marine's category.
     * @throws IOException if wrong.
     */
    public AstartesCategory categoryAsker() throws IOException {
        while(true){
           Outputer.println("List of category: "+ AstartesCategory.listCategory());
            System.out.println("Insert categorry: ");
            String inputOfCategory[] = bfr.readLine().trim().split(" ");
            if (inputOfCategory.length != 1){
                System.out.println("please insert exactly category.");
                continue;
            }
            AstartesCategory astartesCategory = AstartesCategory.valueOf(inputOfCategory[0]);
            return astartesCategory;
        }
    }
    /**set field weapon*/
    /**@return weapon
     * @throws IOException*/
    public Weapon weaponAsker() throws IOException {
        while (true){
            Outputer.println("List of weapon: "+Weapon.outWeapon());
            System.out.println("Insert weapon: ");
            String inputWeapon[] = bfr.readLine().trim().split(" ");
            if (inputWeapon.length != 1){
                System.out.println("Please Insert exactly weapon");
                continue;
            }
            Weapon weapon = Weapon.valueOf(inputWeapon[0]);
            return weapon;
        }
    }
    /**Insert MeleeWeapon*/
    /**@return meleeWeapon*/
    public MeleeWeapon meleeWeaponAsker() throws IOException {
        while(true){
            System.out.println("List of meleeWeapon: "+MeleeWeapon.listMeleeWeapon());
            System.out.println("Insert meleeWeapon: ");
            String inputMeleeWeapon[] = bfr.readLine().trim().split(" ");
            if (inputMeleeWeapon.length !=1){
                System.out.println("Please Insert exactly input of MeleeWeapon");
                continue;
            }
            MeleeWeapon meleeWeapon = MeleeWeapon.valueOf(inputMeleeWeapon[0]);
            return meleeWeapon;
        }
    }
    /**
     * Asks a user the marine's chapter.
     * @return Marine's chapter.
     * @throws IOException If script is running and something goes wrong.
     */
    public Chapter askChapter() throws  IOException {
        String name;
        name = nameAsker();
        String parentLegion = legionAsker();
        return new Chapter(name, parentLegion);
    }


    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     * @throws NoContentException If tThe question has no content.
     * @throws IOException
     */
    public boolean askQuestions(String question){
        String finalQuestion = question + "(yes/no)";
        String answer;
        while(true){
            try{
                Outputer.println(finalQuestion);
                answer = bfr.readLine().trim();
                if (answer.equals(null)) throw new NoContentException();
                if (answer.equals("yes")) return true;
                if (answer.equalsIgnoreCase("no")) return false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoContentException e) {
                e.printStackTrace();
                Outputer.printerror("The question has no content. Please enter the question correctly.");
            }
        }
    }
    public  boolean areYouSure() throws IOException {
        while(true){
            Outputer.println("Are you sure about that? (yes / no): ");
            Outputer.print(ApplicationClient.PS2);
            String answer = userReader.readLine().trim();
            if (answer.equalsIgnoreCase("yes")) return true;
            return false;
        }
    }
}
