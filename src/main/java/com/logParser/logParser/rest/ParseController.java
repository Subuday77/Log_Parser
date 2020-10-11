package com.logParser.logParser.rest;


import com.logParser.logParser.beans.Answer;
import com.logParser.logParser.beans.SearchData;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import static com.logParser.logParser.beans.Constants.KEYS;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parse")
public class ParseController {

    @Autowired
    SearchData searchData;
    @Autowired
    Answer answer;

    @PostMapping("/timestamps")
    public ResponseEntity<?> parseForTs(@RequestBody SearchData searchData) {
//       System.out.println(searchData.getSearchType());
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
            int i = 1;
            for (String line : lines) {
                if ((line.contains(searchType) || lineToParse.contains(searchType)) && (line.contains("{") || lineToParse.contains("{"))) {
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
                                lineToParse = "";
                            }
                            lineToParse = "";
                        }
                    }

                }
            }
        }
        return new ResponseEntity<String>(answer, HttpStatus.OK);
    }

    @PostMapping("/logins")
    public ResponseEntity<?> parseForLogin(@RequestBody SearchData searchData) {
        ArrayList<Answer> answers = new ArrayList<>();
        String lineToParse = "";
        Map initialTokenMap = new TreeMap<Long, String>();
        if (searchData.getLogToParse().isBlank()) {
            return new ResponseEntity<String>("Empty log field", HttpStatus.NO_CONTENT);
        } else {
            String[] lines = searchData.getLogToParse().split("\n");
            for (String line : lines) {
                if (line.contains("Arriving authetication request")) {
                    initialTokenMap = updateInitialTokenMap(initialTokenMap, line);
                }
                if (line.contains("Got auth response") && (searchData.getAdditionalParam() == 0 || line.contains(String.valueOf(searchData.getAdditionalParam())))) {
                    answers.add(parseLineForLogin(line, initialTokenMap));
                }
            }
        }
        return new ResponseEntity<ArrayList<Answer>>(answers, HttpStatus.OK);
    }

    private Answer parseLineForLogin(String line, Map initialTokenMap) {
        line = line.substring(line.indexOf("{"));
        JSONObject toParse = new JSONObject(line);

        Answer answer = new Answer();
        for (String key : KEYS) {
            switch (key) {
                case "operatorId":
                    try {
                        answer.setOperatorId(String.valueOf(toParse.getLong(key)));
                    } catch (JSONException e) {
                        answer.setOperatorId("Can't define " + key);
                    }
                    break;
                case "playerTokenAtLaunch":
                    try {
                        answer.setInitialToken(toParse.getString(key));
                    } catch (JSONException e) {
                        try {
                            answer.setInitialToken((String) initialTokenMap.get(toParse.getLong("operatorId")));
                        } catch (JSONException ex) {
                            answer.setInitialToken("Can't define " + key);
                        }
                    }
                    break;
                case "uid":
                    try {
                        answer.setUid(toParse.getString(key));
                    } catch (JSONException e) {
                        answer.setUid("Can't define " + key);
                    }
                    break;
                case "token":
                    try {
                        answer.setSessionToken(toParse.getString(key));
                    } catch (JSONException e) {
                        answer.setSessionToken("Can't define " + key);
                    }
                    break;
                case "currency":
                    try {
                        answer.setCurrency(toParse.getString(key));
                    } catch (JSONException e) {
                        answer.setCurrency("Can't define " + key);
                    }
                    break;
                case "balance":
                    try {
                        answer.setBalance(String.valueOf(toParse.getDouble(key)));
                    } catch (JSONException e) {
                        answer.setBalance("Can't define " + key);
                    }
                    break;
                default:
                    break;
            }
        }
        return answer;
    }


    private Map updateInitialTokenMap(Map initialTokenMap, String line) {
        line = line.substring(line.indexOf("{"));
        JSONObject toParse = new JSONObject(line);
        long key = toParse.getLong("OperatorID");
        initialTokenMap.put(key, toParse.getString("Token"));

        return initialTokenMap;
    }

    private static String parseLine(String line) {
        // System.out.println(line);
        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);
            //System.out.println(toParse.getString("transactionId") + "\t" + toParse.getBigInteger("timestamp"));
            return (toParse.getString("transactionId") + "\t" + toParse.getBigInteger("timestamp") + "\n");

        } catch (JSONException e) {
            return "";
        }

    }
}
