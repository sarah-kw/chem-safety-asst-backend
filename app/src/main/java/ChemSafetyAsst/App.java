/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package ChemSafetyAsst;
import static spark.Spark.*;
// import ChemSafetyAsst.Chemical;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;

public class App {
    private static String pugViewUrl = "https://pubchem.ncbi.nlm.nih.gov/rest/pug_view/data/compound/";
    // <cid>/JSON?heading=GHS%20Classification
    private static String pugRestUrl = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/";

    public String getGreeting() {
        return "Hello World! Test";
    }

// Enables CORS on requests. This method is an initialization method and should be called once.
// From SparkJava tutorial https://sparkjava.com/tutorials/cors
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            // response.type("application/json");
        });
    }

    private static HttpRequest hazardsFromCIDrequest(String cid) {
        HttpRequest getPUGViewData = HttpRequest.newBuilder()
            .uri(
                URI.create(pugViewUrl+cid+"/JSON?heading=GHS%20Classification")
            ).build();
        System.out.println(getPUGViewData.uri());
        return getPUGViewData;
    }

    private static ArrayList<String[]> createHazardList(JsonNode hazardData) {
        ArrayList<String[]> hazardReturn = new ArrayList<String[]>();
        Iterator<JsonNode> hazards = hazardData.elements();
        while (hazards.hasNext()){
            JsonNode oneHazard = hazards.next();
            System.out.println(oneHazard.path("String").textValue().split(":")[0]);
            String hazInfo[] = oneHazard.path("String").textValue().split(":");
            hazardReturn.add(hazInfo);
        }
        return hazardReturn;
    }

    private static HttpRequest cidFromSMILESRequest(String smiles){
        HttpRequest getPUGRestCID = HttpRequest.newBuilder()
        .uri(
            URI.create(pugRestUrl+"smiles/"+smiles+"/cids/json")
        ).build();
        return getPUGRestCID;
    }
    
    private static HttpRequest cidFromNameRequest(String name){
        HttpRequest getPUGRestCID = HttpRequest.newBuilder()
        .uri(
            URI.create(pugRestUrl+"name/"+name+"/cids/json")
        ).build();
        return getPUGRestCID;
    }

    private static void getHazardsByCID(Chemical chemical, HttpClient client, ObjectMapper objectMapper) throws IOException, InterruptedException{
        HttpRequest hazardRequest = hazardsFromCIDrequest(chemical.getCID());
        HttpResponse<String> hazardResponse;
        try {
            hazardResponse = client.send(
                hazardRequest, HttpResponse.BodyHandlers.ofString()
                );
            } catch (IOException e) {
                System.out.println("IO exception occurred");
                throw new IOException("error in hazardRequest");
            } catch (InterruptedException e){
                System.out.println("Interrupted exception occurred");
                throw new InterruptedException("error in hazardRequest");
            }

        JsonNode hazardRoot = objectMapper.readTree(hazardResponse.body());

        JsonNode hazardCheckpoint = hazardRoot
            .path("Record")
            .path("Section")
            .get(0)
            .path("Section")
            .get(0)
            .path("Section")
            .get(0)
            .path("Information");
                        
        System.out.println("the size is "+ hazardCheckpoint.size());
        if (hazardCheckpoint.size() > 1){
            JsonNode hazardCodes = hazardRoot
                .path("Record")
                .path("Section")
                .get(0)
                .path("Section")
                .get(0)
                .path("Section")
                .get(0)
                .path("Information")
                .get(2)
                .path("Value")
                .path("StringWithMarkup");
            ArrayList<String[]> hazardList = createHazardList(hazardCodes);
                            // System.out.println(toPrint.get(0)[1]);
            System.out.println(hazardList);
            chemical.setHazards(hazardList);

            JsonNode precautionCodes = hazardRoot
                .path("Record")
                .path("Section")
                .get(0)
                .path("Section")
                .get(0)
                .path("Section")
                .get(0)
                .path("Information")
                .get(3)
                .path("Value")
                .path("StringWithMarkup")
                .get(0);

                            // String[] precInfo = precautionCodes
                            //     .path("String").textValue().split(", ");
            String[] precInfo = precautionCodes
                .path("String").textValue().split(", and |, |and ");
            System.out.println(precautionCodes);
            System.out.println(precInfo);
            chemical.setPrecautions(precInfo);
        } else {
            ArrayList<String[]> hazardList = new ArrayList<String[]>();
            chemical.setHazards(hazardList);
            String[] precautionList = new String[0];
            chemical.setPrecautions(precautionList);
        };
        chemical.setGotHazards();
    }

    //main app starts here
    public static void main(String[] args) {
        // Does this make the app??? check later
        // System.out.println(new App().getGreeting());
        port(443);
        // Setup; need one of each of these 
        enableCORS("*", "GET", "*");
        HttpClient client = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        get("/chemicals", (req, res) -> {
            ArrayList<Chemical> chemicals = new ArrayList<Chemical>();
            for(String param : req.queryParams()){
                System.out.println(param);
                for (String value : req.queryParamsValues(param)){
                    Chemical newChemical = new Chemical(value);
                    // Try search by name
                    // HttpRequest cidRequest = cidFromSMILESRequest(newChemical.urlify());
                    HttpRequest cidRequest = cidFromNameRequest(newChemical.urlify());
                    System.out.println(cidRequest.uri());
                    HttpResponse<String> cidResponse;
                    try {
                        cidResponse = client.send(
                            cidRequest, HttpResponse.BodyHandlers.ofString()
                            );
                    } catch (IOException e) {
                        System.out.println("IO exception occurred");
                        throw new IOException("error in cidRequest");
                    }
                    // if (cidResponse.statusCode() != 200){
                    //     // Try search by SMILES
                    //     HttpRequest cidRequestSMILES = cidFromSMILESRequest(newChemical.urlify());
                    //     System.out.println(cidRequestSMILES.uri());
                    //     HttpResponse<String> cidResponseSMILES;
                    //     try {
                    //         cidResponseSMILES = client.send(
                    //             cidRequestSMILES, HttpResponse.BodyHandlers.ofString()
                    //             );
                    //     } catch (IOException e) {
                    //         System.out.println("IO exception occurred");
                    //         throw new IOException("error in cidRequest");
                    //     }
                    // }

                    if (cidResponse.statusCode() == 200){
                        JsonNode rootNode = objectMapper.readTree(cidResponse.body());
                        String cid = rootNode.path("IdentifierList")
                            .path("CID")
                            .get(0)
                            .toString();
                        
                        newChemical.setCID(cid);
                        getHazardsByCID(newChemical, client, objectMapper);
                        // HttpRequest hazardRequest = hazardsFromCIDrequest(cid);
                        // HttpResponse<String> hazardResponse;
                        // try {
                        //     hazardResponse = client.send(
                        //         hazardRequest, HttpResponse.BodyHandlers.ofString()
                        //         );
                        // } catch (IOException e) {
                        //     System.out.println("IO exception occurred");
                        //     throw new IOException("error in hazardRequest");
                        // }

                        // JsonNode hazardRoot = objectMapper.readTree(hazardResponse.body());

                        // JsonNode hazardCheckpoint = hazardRoot
                        //     .path("Record")
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Information");
                        
                        // System.out.println("the size is "+ hazardCheckpoint.size());
                        // if (hazardCheckpoint.size() > 1){
                        //     JsonNode hazardCodes = hazardRoot
                        //     .path("Record")
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Information")
                        //     .get(2)
                        //     .path("Value")
                        //     .path("StringWithMarkup");
                        //     ArrayList<String[]> hazardList = createHazardList(hazardCodes);
                        //     // System.out.println(toPrint.get(0)[1]);
                        //     System.out.println(hazardList);
                        //     newChemical.setHazards(hazardList);

                        //     JsonNode precautionCodes = hazardRoot
                        //     .path("Record")
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Section")
                        //     .get(0)
                        //     .path("Information")
                        //     .get(3)
                        //     .path("Value")
                        //     .path("StringWithMarkup")
                        //     .get(0);

                        //     // String[] precInfo = precautionCodes
                        //     //     .path("String").textValue().split(", ");
                        //     String[] precInfo = precautionCodes
                        //         .path("String").textValue().split(", and |, |and ");
                        //     System.out.println(precautionCodes);
                        //     System.out.println(precInfo);
                        //     newChemical.setPrecautions(precInfo);
                        // } else {
                        //     ArrayList<String[]> hazardList = new ArrayList<String[]>();
                        //     newChemical.setHazards(hazardList);
                        //     String[] precautionList = new String[0];
                        //     newChemical.setPrecautions(precautionList);
                        // };
                        // newChemical.setGotHazards();
                        chemicals.add(newChemical);

                    } else {
                        // Try search by SMILES
                        HttpRequest cidRequestSMILES = cidFromSMILESRequest(newChemical.urlify());
                        System.out.println(cidRequestSMILES.uri());
                        HttpResponse<String> cidResponseSMILES;
                        try {
                            cidResponseSMILES = client.send(
                                cidRequestSMILES, HttpResponse.BodyHandlers.ofString()
                                );
                        } catch (IOException e) {
                            System.out.println("IO exception occurred");
                            throw new IOException("error in cidRequest");
                        };

                        if (cidResponseSMILES.statusCode() == 200){
                            JsonNode rootNode = objectMapper.readTree(cidResponseSMILES.body());
                            String cid = rootNode.path("IdentifierList")
                                .path("CID")
                                .get(0)
                                .toString();
                            
                            newChemical.setCID(cid);
                            getHazardsByCID(newChemical, client, objectMapper);
                            chemicals.add(newChemical);
                        } else{
                        chemicals.add(newChemical);}
                    };
                };
            };

            String chemicalJson = objectMapper.writeValueAsString(chemicals);
            return chemicalJson;
        });
        
        get("/testrequest", (req, res) -> {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://pubchem.ncbi.nlm.nih.gov/rest/pug/substance/sid/53789435/synonyms/json"))
            .build();
            HttpResponse<String> response;
            try {
                response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
                    );
            } catch (IOException e) {
                System.out.println("IO exception occurred");
                return "error";
            }
            return response.body();
        });

        get("/hello", (req, res) -> 
        {
            return "{\"Hello World\"}";
        });

        get("/stop", (req, res) -> {
            stop();
            return "stopping";
        });
    }
}
