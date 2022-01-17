package ToDoMate.ToDoMate.service;

import java.util.Random;

public class AuthenticationService {
    public String generateRandomNumber(){
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d",number);
    }
}
