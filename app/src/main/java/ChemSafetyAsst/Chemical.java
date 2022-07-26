// import java.net.http;
package Chemical;
import java.net.http.HttpClient;

public class Chemical {
    String identifier;
    public static void main(String[] args){
        Chemical testChemical = new Chemical("whassup");
        testChemical.printIdentifier();
    }
    public Chemical (String input_identifier) {
        identifier = input_identifier;
    }
    public String printIdentifier(){
        System.out.println("the identifier is");
        System.out.println(identifier);
        return "The identifier is " + identifier;
    }
}