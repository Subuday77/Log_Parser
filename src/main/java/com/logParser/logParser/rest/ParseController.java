package com.logParser.logParser.rest;

import com.logParser.logParser.beans.Answer;
import com.logParser.logParser.beans.SearchData;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        String searchType = "Got operator response";
        if (searchData.getSearchType() == 2) {
            searchType = "Posting financial post to url";
        }
        if (searchData.getLogToParse().isBlank()) {
            answer = "Empty log field";
        } else {
            answer = (String) getTSOperatorIdRoundId(0, searchData, searchType, 0);
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
                if (line.contains("Arriving authentication request")) {
                    initialTokenMap = updateInitialTokenMap(initialTokenMap, line);
                }
                if (line.contains("Got auth response") && (searchData.getAdditionalParam() == 0 || line.contains(String.valueOf(searchData.getAdditionalParam())))) {
                    answers.add(parseLineForLogin(line, initialTokenMap));
                }
            }
        }
        return new ResponseEntity<ArrayList<Answer>>(answers, HttpStatus.OK);
    }

    @PostMapping("/operators")
    public ResponseEntity<?> getOperatorsForStress(@RequestBody SearchData searchData) {
        HashSet<String> temp = new HashSet<>();
        ArrayList<String> operatorIDs = new ArrayList<>();
        if (searchData.getLogToParse().isBlank()) {
            return new ResponseEntity<String>("Empty log field", HttpStatus.NO_CONTENT);
        } else {
            String searchType = "Got operator response";
            temp = (HashSet<String>) getTSOperatorIdRoundId(1, searchData, searchType, 0);
        }
        for (String id : temp) {
            if (!id.equals("")) {
                operatorIDs.add(id);
            }
        }
        operatorIDs.sort(null);
        return new ResponseEntity<ArrayList<String>>(operatorIDs, HttpStatus.OK);
    }

    @PostMapping("/rounds")
    public ResponseEntity<?> getRoundsForStress(@RequestParam(name = "operator") long operatorId, @RequestBody SearchData searchData) {
        HashSet<String> temp = new HashSet<>();
        ArrayList<Long> roundIDs = new ArrayList<>();
        if (searchData.getLogToParse().isBlank()) {
            return new ResponseEntity<String>("Empty log field", HttpStatus.NO_CONTENT);
        } else {
            String searchType = "Got operator response";
            temp = (HashSet<String>) getTSOperatorIdRoundId(2, searchData, searchType, operatorId);
        }
        for (String id : temp) {
            if (id != "0") {
                roundIDs.add(Long.parseLong(id));
            }
        }
        roundIDs.sort((o1, o2) -> Math.toIntExact(o2 - o1));
        return new ResponseEntity<ArrayList<Long>>(roundIDs, HttpStatus.OK);
    }

    @PostMapping("/gettransactions")
    public ResponseEntity<?> analyseLog(@RequestParam(name = "operator") long operatorId, @RequestBody SearchData searchData) {
        return new ResponseEntity<HashMap>(prepareLines(1, operatorId, searchData), HttpStatus.OK);
    }

    private static HashMap<String, String> prepareLines(int requestType, long operatorId, SearchData searchData) {
        String lineToParse = "";
        String result = "";
        int resultCount = 0;
        int requestsCount = 0;
        int responsesCount = 0;
        int debitCount = 0;
        int creditCount = 0;
        int rollbackCount = 0;
        int minResponseTime = 10000000;
        int maxResponseTime = 0;
        int timeSum = 0;
        int timeCount = 0;
        String[] rounds = new String[searchData.getRoundsToAnalyse().size()];
        for (int i = 0; i < rounds.length; ++i) {
            rounds[i] = String.valueOf(searchData.getRoundsToAnalyse().get(i));
        }
        String[] lines = searchData.getLogToParse().split("\n");
        for (String line : lines) {
            if ((((line.contains("Got operator response") || line.contains("Posting financial post to url")) ||
                    (lineToParse.contains("Got operator response") || lineToParse.contains("Posting financial post to url"))
                            && (line.contains("{") || lineToParse.contains("{"))) || line.contains("Post to operator took"))) {
                if ((line.indexOf("{") > 0 && line.indexOf("}") > 0) && Arrays.stream(rounds).anyMatch(line::contains) && line.contains(String.valueOf(operatorId))
                        || (line.contains("Post to operator took") && !line.contains("null")) && Arrays.stream(rounds).anyMatch(line::contains) && line.contains(String.valueOf(operatorId))) {
                    switch (requestType) {
                        case 1:
                            String[] x = line.split(":", 4);
                            line = x[3];
                            break;
                        case 2:
                            if (!line.contains("Post to operator took")) {
                                line = line.substring(line.indexOf("{"));
                            } else {
                                x = line.split("tewayDefaultNewServiceProvider: ");
                                line = x[1];
                            }
                            break;
                    }
                    resultCount++;
                    if (line.contains("Posting financial post to url")) {
                        requestsCount++;
                        if (line.contains("debitAmount")) {
                            debitCount++;
                        }
                        if (line.contains("creditAmount")) {
                            creditCount++;
                        }
                        if (line.contains("rollbackAmount")) {
                            rollbackCount++;
                        }
                    } else if (line.contains("Got operator response")) {
                        responsesCount++;
                    } else {
                        String[] x = line.split("Post to operator took ");
                        int responseTime = Integer.parseInt(x[1]);
                        timeCount++;
                        timeSum = timeSum + responseTime;
                        if (responseTime < minResponseTime) {
                            minResponseTime = responseTime;
                        }
                        if (responseTime > maxResponseTime) {
                            maxResponseTime = responseTime;
                        }
                    }
                    result = result.concat(line + "\n");
                } else {
                    lineToParse = lineToParse.concat(line);
                    if (lineToParse.indexOf("{") == -1) {
                        lineToParse = "";
                    }
                    if (lineToParse.indexOf("{") > 0 && lineToParse.indexOf("}") > 0) {
                        if (Arrays.stream(rounds).anyMatch(lineToParse::contains) && lineToParse.contains(String.valueOf(operatorId))) {
                            switch (requestType) {
                                case 1:
                                    String[] x = lineToParse.split(":", 4);
                                    lineToParse = x[3];
                                    break;
                                case 2:
                                    lineToParse = lineToParse.substring(lineToParse.indexOf("{"));
                                    break;
                            }
                            resultCount++;
                            if (lineToParse.contains("Posting financial post to url")) {
                                requestsCount++;
                                if (lineToParse.contains("debitAmount")) {
                                    debitCount++;
                                }
                                if (lineToParse.contains("creditAmount")) {
                                    creditCount++;
                                }
                                if (lineToParse.contains("rollbackAmount")) {
                                    rollbackCount++;
                                }
                            } else if (lineToParse.contains("Got operator response")) {
                                responsesCount++;
                            } else {
                                String[] x = lineToParse.split("Post to operator took ");
                                int responseTime = Integer.parseInt(x[1]);
                                timeCount++;
                                timeSum = timeSum + responseTime;
                                if (responseTime < minResponseTime) {
                                    minResponseTime = responseTime;
                                }
                                if (responseTime > maxResponseTime) {
                                    maxResponseTime = responseTime;
                                }
                            }
                            result = result.concat(lineToParse + "\n");
                        }
                        lineToParse = "";
                    }
                }
            }
        }
        HashMap<String, String> response = new HashMap<>();
        response.put("resultCount", String.valueOf(resultCount));
        response.put("requestsCount", String.valueOf(requestsCount));
        response.put("responsesCount", String.valueOf(responsesCount));
        response.put("debitCount", String.valueOf(debitCount));
        response.put("creditCount", String.valueOf(creditCount));
        response.put("rollbackCount", String.valueOf(rollbackCount));
        response.put("minResponseTime", String.valueOf(minResponseTime));
        response.put("maxResponseTime", String.valueOf(maxResponseTime));
        response.put("averageResponseTime", String.valueOf(timeSum/timeCount));
        response.put("result", result);
        return response;
    }

    private static synchronized Object getTSOperatorIdRoundId(int dataType, SearchData searchData, String
            searchType, long operatorId) {
        String lineToParse = "";
        String answer = "";
        HashSet<String> temp = new HashSet<>();
        String[] lines = searchData.getLogToParse().split("\n");
        for (String line : lines) {
            if ((line.contains(searchType) || lineToParse.contains(searchType)) && (line.contains("{") || lineToParse.contains("{"))) {
                if (line.indexOf("{") > 0 && line.indexOf("}") > 0) {
                    if (searchData.getAdditionalParam() == 0 ||
                            line.contains(String.valueOf(searchData.getAdditionalParam())) ||
                            lineToParse.contains(String.valueOf(searchData.getAdditionalParam()))) {
                        switch (dataType) {
                            case 0:
                                answer = answer.concat(parseLine(line));
                                break;
                            case 1:
                                temp.add(getOperatorId(line));
                                break;
                            case 2:
                                temp.add(getRoundId(line, operatorId));
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    lineToParse = lineToParse.concat(line);
                    if (lineToParse.indexOf("{") > 0 && lineToParse.indexOf("}") > 0) {
                        if (searchData.getAdditionalParam() == 0 ||
                                line.contains(String.valueOf(searchData.getAdditionalParam())) ||
                                lineToParse.contains(String.valueOf(searchData.getAdditionalParam()))) {
                            switch (dataType) {
                                case 0:
                                    answer = answer.concat(parseLine(lineToParse));
                                    break;
                                case 1:
                                    temp.add(getOperatorId(lineToParse));
                                    break;
                                case 2:
                                    temp.add(getRoundId(lineToParse, operatorId));
                                    break;
                                default:
                                    break;
                            }
                        }
                        lineToParse = "";
                    }
                }
            }
        }
        return dataType == 0 ? answer : temp;
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

    private static String getOperatorId(String line) {
        // System.out.println(line);
        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);
            //System.out.println(toParse.getString("transactionId") + "\t" + toParse.getBigInteger("timestamp"));
            return (String.valueOf(toParse.getLong("operatorId")));

        } catch (JSONException e) {
            return "";
        }
    }

    private static String getRoundId(String line, long operatorId) {
        // System.out.println(line);
        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);
            if (toParse.getLong("operatorId") == operatorId) {
                return (String.valueOf(toParse.getLong("roundId")));
            } else {
                return "0";
            }

        } catch (JSONException e) {
            return "0";
        }
    }

}
