// import java.net.http;
package ChemSafetyAsst;
// import java.net.http.HttpClient;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.URLDecoder;

public class Chemical {
    // the name given by the user (smiles or name)
    private String userInputName;
    // cid obtained from PUGrest
    private String cid;

    private String canonicalSMILES;
    private String iupacName;
    private String commonName;
    public void setCanonicalSMILES(String canonicalSMILES){
        this.canonicalSMILES = canonicalSMILES;
    }
    public String getCanonicalSMILES(){
        return this.canonicalSMILES;
    }
    public void setIupacName(String iupacName){
        this.iupacName = iupacName;
    }
    public String getIupacName(){
        return this.iupacName;
    }
    public void setCommonName(String commonName){
        this.commonName = commonName;
    }
    public String getCommonName(){
        return this.commonName;
    }



    // hazard codes+descriptors from PUGView
    private ArrayList<String[]> hazards;
    // precaution codes from PUGView
    private String[] precautions;
    // whether or not successfully got hazards from PUGView
    private Boolean gotHazards = false;
    public static void main(String[] args){
        //for testing
        Chemical testChemical = new Chemical("whassup");
        testChemical.printIdentifier();
        System.out.print(testChemical.getCID());
    }
    public Chemical (String userInputName) {
        this.userInputName = userInputName;
    }

    public String getuserInputName() {
        return this.userInputName;
    }
    public String getCID() {
        return this.cid;
    }
    public void setCID (String cid){
        this.cid = cid;
    }
    public void setHazards (ArrayList<String[]> hazards){
        this.hazards = hazards;
    }

    public ArrayList<String[]> getHazards () {
        return this.hazards;
    }

    public void setPrecautions (String[] precautions){
        this.precautions = precautions;
    }

    public String[] getPrecautions() {
        return this.precautions;
    }
    
    public void setGotHazards(){
        this.gotHazards = true;
    }
    public Boolean getGotHazards() {
        return this.gotHazards;
    }



    public String printIdentifier(){
        System.out.println("the identifier is "+cid);
        return "The identifier is " + this.cid;
    }
    public String urlify() throws UnsupportedEncodingException{
        return URLEncoder.encode(this.userInputName, "UTF-8");
    }
    public String deUrlify() throws UnsupportedEncodingException{
        return URLDecoder.decode(this.cid, "UTF-8");
    }

}