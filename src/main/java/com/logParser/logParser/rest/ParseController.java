package com.logParser.logParser.rest;


import com.logParser.logParser.beans.Answer;
import com.logParser.logParser.beans.SearchData;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parse")
public class ParseController {

    @Autowired
    SearchData searchData;
    @Autowired
    Answer answer;

    @PostMapping("/timestamps")
    public ResponseEntity<?> parseForTs(@RequestBody SearchData searchData) throws JSONException {
        //System.out.println(searchData.getAdditionalParam());
        String answer = "";
        String lineToParse = "";
        String searchType = "Got operator response";
        if (searchData.getSearchType() == 2) {
            searchType = "Posting financial post to url";
        }
        if (searchData.getLogToParse().isBlank()) {
            answer = "Empty log field";
        } else {
            String[] lines = searchData.getLogToParse().split("\n");
            for (String line : lines) {
                if (line.contains(searchType) || lineToParse.contains(searchType)) {
                    if (line.indexOf("{") > 0 && line.indexOf("}") > 0) {
                        if (searchData.getAdditionalParam() == 0 ||
                                line.contains(String.valueOf(searchData.getAdditionalParam())) ||
                                lineToParse.contains(String.valueOf(searchData.getAdditionalParam()))) {
                            answer = answer.concat(parseLine(line));
                        }
                    } else {
                        lineToParse = lineToParse.concat(line);
                        if (lineToParse.indexOf("{") > 0 && lineToParse.indexOf("}") > 0) {
                            if (searchData.getAdditionalParam() == 0 ||
                                    line.contains(String.valueOf(searchData.getAdditionalParam())) ||
                                    lineToParse.contains(String.valueOf(searchData.getAdditionalParam()))) {
                                answer = answer.concat(parseLine(lineToParse));
                            }
                            lineToParse = "";
                        }
                    }

                }
            }
        }
        return new ResponseEntity<String>(answer, HttpStatus.OK);
    }

    private static String parseLine(String line) throws JSONException {

        line = line.substring(line.indexOf("{"));
        JSONObject toParse = new JSONObject(line);
        try {
            return (toParse.getString("transactionId") + "\t" + toParse.getBigInteger("timestamp") + "\n");
        } catch (JSONException e) {
            return "";
        }

    }
}
