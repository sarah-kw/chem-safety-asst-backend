# Chemistry Safety Assistant

Chemistry Safety Assistant was built by Sarah Williams as a capstone project for
Ada Developers Academy (Cohort 17).

## Routes
This app has one main route, /chemicals. For each query parameter passed to this route, the PUG REST API is called to find that query's PubChem chemical identifier (CID). On success, PUG View is called to look up that chemical's hazard information. Additional information about the chemicals is also requested for chemicals with valid CIDs. Information is returned to the user in the form of a nested JSON object, as well as a key indicating whether GHS hazard lookup was successful. 

Possible first-level keys:
- "[CID number]": The CID for the query
- "noCID_[user input]": No CID was found for this query

Chemical info keys:
- "userInputName": the initial query parameter
- "cid": PubChem CID
- "canonicalSMILES": a [canonicalized](https://en.wikipedia.org/wiki/Simplified_molecular-input_line-entry_system#Terminology) SMILES representation of the molecule
- "commonName": the title of the PubChem article referring to tthe input chemical
- "hazards": a nested array of GHS hazards. Subarrays contain the hazard code at [0] and a hazard statement at [1].
- "precautions: an array of GHS precaution codes.
- "gotHazards": a boolean "true" if GHS hazard lookup was executed, "false" if not. 

Example:

```{"5280795":{"userInputName":"CC","cid":"5280795","canonicalSMILES":"CC(C)CCCC(C)C1CCC2C1(CCCC2=CC=C3CC(CCC3=C)O)C","iupacName":"(1S,3Z)-3-[(2E)-2-[(1R,3aS,7aR)-7a-methyl-1-[(2R)-6-methylheptan-2-yl]-2,3,3a,5,6,7-hexahydro-1H-inden-4-ylidene]ethylidene]-4-methylidenecyclohexan-1-ol","commonName":"Cholecalciferol","hazards":[["H300"," Fatal if swallowed [Danger Acute toxicity, oral]"],["H310"," Fatal in contact with skin [Danger Acute toxicity, dermal]"],["H330"," Fatal if inhaled [Danger Acute toxicity, inhalation]"],["H372"," Causes damage to organs through prolonged or repeated exposure [Danger Specific target organ toxicity, repeated exposure]"]],"precautions":["P260","P262","P264","P270","P271","P280","P284","P301+P316","P302+P352","P304+P340","P316","P319","P320","P321","P330","P361+P364","P403+P233","P405","P501"],"gotHazards":true},"noCID_hello":{"userInputName":"hello","cid":null,"canonicalSMILES":null,"iupacName":null,"commonName":null,"hazards":null,"precautions":null,"gotHazards":false},"180":{"userInputName":"acetone","cid":"180","canonicalSMILES":"CC(=O)C","iupacName":"propan-2-one","commonName":"Acetone","hazards":[["H225"," Highly Flammable liquid and vapor [Danger Flammable liquids]"],["H319"," Causes serious eye irritation [Warning Serious eye damage/eye irritation]"],["H336"," May cause drowsiness or dizziness [Warning Specific target organ toxicity, single exposure; Narcotic effects]"]],"precautions":["P210","P233","P240","P241","P242","P243","P261","P264+P265","P271","P280","P303+P361+P353","P304+P340","P305+P351+P338","P319","P337+P317","P370+P378","P403+P233","P403+P235","P405","P501"],"gotHazards":true}}```


## About the App
The Chemistry Safety Assistant backend is built in Java with the [Gradle Build Tool](https://gradle.org/). 

This app uses 
- The [SparkJava](https://sparkjava.com/) web framework
- [Jackson](https://github.com/FasterXML/jackson-core) for JSON handling
- The [PUG REST](https://pubchemdocs.ncbi.nlm.nih.gov/pug-rest) and [PUG View](https://pubchemdocs.ncbi.nlm.nih.gov/pug-view) APIs for data


A live version of the app is available at [www.chemsafety.me](www.chemsafety.me). Both this backend and the project's JavaScript frontend are hosted on Azure. 

This project is open source under the terms of the [MIT License](License.md).

## Run the app
To run this app locally, clone the GitHub repository and run ./gradlew run. For deployment, the Spark server is set up to use port 80; however, this can be changed using ```port()``` under the main class in [App.java](app/src/main/java/ChemSafetyAsst/App.java). ***OPTIONALLY*** uncomment the "/stop" route to halt the development server from the browser. 

## About Ada Developers Academy
Ada Developers Academy is "a non-profit, tuition-free
coding school for women and gender-expansive
adults. Founded in 2013 to solve the tech pipeline
issue and help create diverse teams, we focus on
serving low-income Black, Latine, Indigenous, Pacific
Islander, and LGBTQIA+ communities." Learn more at https://adadevelopersacademy.org/ .
