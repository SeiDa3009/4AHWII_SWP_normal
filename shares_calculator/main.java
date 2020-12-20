import models.*;

import java.io.IOException;
import java.net.MalformedURLException;

public class main {
    public static void main(String[] args) {
        methods m1 = new methods();
        try{
            //Method, where the data will get implemented from the api
            m1.dataGetter(m1.sharesSelect());
            //Database
            m1.db();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
