import java.net.http

public class Chemical {
    String identifier;
    public static void main(String[] args){
        Chemical testChemical = new Chemical("whassup");
        testChemical.printIdentifier();
    }
    public Chemical (String input_identifier) {
        identifier = input_identifier;
    }
    public void printIdentifier(){
        System.out.println("the identifier is");
        System.out.println(identifier);
    }
}