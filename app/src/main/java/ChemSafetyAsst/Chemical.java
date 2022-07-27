// import java.net.http;
package Chemical;
import java.net.http.HttpClient;
import java.util.HashMap;

public class Chemical {
    private String identifier;
    public static void main(String[] args){
        //for testing
        Chemical testChemical = new Chemical("whassup");
        testChemical.printIdentifier();
    }
    public Chemical (String input_identifier) {
        this.identifier = input_identifier;
    }
    public String printIdentifier(){
        System.out.println("the identifier is");
        System.out.println(identifier);
        return "The identifier is " + this.identifier;
    }
    // public HashMap getSafetyData(){
    //     return
    // }
}