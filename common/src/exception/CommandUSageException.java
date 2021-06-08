package exception;

public class CommandUSageException  extends Exception{
    public CommandUSageException(){
        super();
    }
    public CommandUSageException(String message) {
        super(message);
    }
}
