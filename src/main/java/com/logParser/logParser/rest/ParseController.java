package com.logParser.logParser.rest;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parse")
public class ParseController {

    @PostMapping("/timestamps")
    public ResponseEntity<?> parseForTs(@RequestBody String logToParse) throws JSONException {
        String answer = "";
        String lineToParse = "";
        if (logToParse.isBlank()) {
            answer = "Empty log field";
        } else {
            String[] lines = logToParse.split("\n");
            for (String line : lines) {
                if (line.contains("Got operator response") || lineToParse.contains("Got operator response")) {


                    if (line.indexOf("{") > 0 && line.indexOf("}") > 0) {
                        answer = answer.concat(parseLine(line));

                    } else {
                        lineToParse = lineToParse.concat(line);
                        if (lineToParse.indexOf("{") > 0 && lineToParse.indexOf("}") > 0) {
                            answer = answer.concat(parseLine(lineToParse));
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
