// import java.net.http;
package ChemSafetyAsst;
// import java.net.http.HttpClient;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;

public class Chemical {
    private String identifier;
    private ArrayList<String> hazards;
    private Boolean gotHazards = false;
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
    public String urlify() throws UnsupportedEncodingException{
        return URLEncoder.encode(this.identifier, "UTF-8");
    }
    public String deUrlify() throws UnsupportedEncodingException{
        return URLDecoder.decode(this.identifier, "UTF-8");
    }
    // public HashMap getSafetyData(){
    //     return
    // }
}