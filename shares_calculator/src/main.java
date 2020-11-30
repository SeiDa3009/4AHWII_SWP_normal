import models.*;

import java.io.IOException;
import java.net.MalformedURLException;

public class main {
    public static void main(String[] args) {
        methods m1 = new methods();
        try{
            m1.dataGetter(m1.sharesSelect());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
