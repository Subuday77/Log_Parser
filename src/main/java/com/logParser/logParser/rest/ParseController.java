package com.logParser.logParser.rest;

import com.google.gson.Gson;
import com.logParser.logParser.beans.AnalyseBeans.*;
import com.logParser.logParser.beans.Answer;
import com.logParser.logParser.beans.SearchData;

import com.logParser.logParser.output.ExcelOutput;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.logParser.logParser.beans.Constants.KEYS;
import static com.logParser.logParser.beans.Constants.BETTYPES;
import static com.logParser.logParser.beans.Constants.GAMEIDS;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/parse")
public class ParseController {
    @Autowired
    ExcelOutput excelOutput;
    @Autowired
    HttpServletRequest servletRequest;

    @PostMapping("/timestamps")
    public ResponseEntity<?> parseForTs(@RequestBody SearchData searchData) {
        if (searchData.getLogToParse().startsWith("**")) {
            searchData.setLogToParse(prepareProductionLog(searchData.getLogToParse()));
        }
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
        if (searchData.getLogToParse().startsWith("**")) {
            searchData.setLogToParse(prepareProductionLog(searchData.getLogToParse()));
        }
        ArrayList<Answer> answers = new ArrayList<>();
        String lineToParse = "";
        Map initialTokenMap = new TreeMap<Long, String>();
        if (searchData.getLogToParse().isBlank()) {
            return new ResponseEntity<String>("Empty log field", HttpStatus.NO_CONTENT);
        } else {
            String[] lines = searchData.getLogToParse().split("\n");
            for (String line : lines) {
                if (line.contains("Arriving authentication request") || line.contains("Arriving authetication request")) {
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

    @PostMapping("/stress")
    public ResponseEntity<?> stressTest(@RequestParam(name = "operator") long operatorId, @RequestBody SearchData searchData) {
        ResultST resultST = createResult(operatorId, searchData);

        return new ResponseEntity<ResultST>(resultST, HttpStatus.OK);
    }

    @PostMapping("/report")
    public ResponseEntity<?> createReport(@RequestParam(name = "operator") long operatorId, @RequestBody String result) {
        Gson gson = new Gson();
        ResultST resultST = gson.fromJson(result, ResultST.class);
        return excelOutput.createReport(operatorId, resultST);
    }

    @GetMapping("/getfile")
    public ResponseEntity<?> getFile(@RequestParam(name = "filename") String fileName) throws IOException {
//        String fileName = servletRequest.getHeader("fileName");
        return excelOutput.sendFile(fileName);
    }

    private static ResultST createResult(long operatorId, SearchData searchData) {
        ArrayList<RoundST> roundsST = new ArrayList<>();
        ArrayList<String> uids = new ArrayList<>();
        int transactionsCount = 0;
        String linesToParse = "";
        HashSet<String> temp = new HashSet<>();
        String x = prepareLines(1, operatorId, searchData).get("result");
        String[] lines = x.split("\n");
        for (String line : lines) {
            if (line.contains("{")) {
                linesToParse = linesToParse.concat(line + "\n");
                temp.add(getUid(line));
            }
        }
        for (String uid : temp) {
            if (!uid.equals("")) {
                uids.add(uid);
            }
        }
        for (long roundId : searchData.getRoundsToAnalyse()) {
            RoundST roundST = createRound(operatorId, linesToParse, roundId, uids);
            roundsST.add(roundST);
            transactionsCount = transactionsCount + roundST.getTransactionsCount();

        }
        roundsST.sort(Comparator.comparingLong(RoundST::getRoundId).reversed());
        ResultST resultST = new ResultST(transactionsCount, roundsST);

        return resultST;
    }

    private static RoundST createRound(long operatorId, String linesToParse, long roundId, ArrayList<String> uids) {
        int transactionCount = 0;
        ArrayList<UserST> users = new ArrayList<>();

        for (String uid : uids) {
            UserST user = createUser(operatorId, linesToParse, roundId, uid);
            if (user.getTransactions().size() > 0) {
                users.add(user);
                transactionCount = transactionCount + users.get(users.size() - 1).getTransactionsCount();
            }
        }
        RoundST roundST = new RoundST(roundId, users.size(), transactionCount, users);

        return roundST;
    }

    private static UserST createUser(long operatorId, String linesToParse, long roundId, String uid) {
        BidiMap<String, Integer> revertedBetTypes = BETTYPES.inverseBidiMap();
        int debitsCount = 0;
        int creditsCount = 0;
        ArrayList<TransactionST> transactionsTS = new ArrayList<>();
        ArrayList<TransactionST> issuedTransactionsTS = new ArrayList<>();

        LinkedHashMap<String, TransactionST> allTransactions = prepareTransactionsForConstCheck(operatorId, linesToParse);
        for (TransactionST transactionST : allTransactions.values()) {

            if (transactionST.getRoundId() == roundId && transactionST.getUid().equals(uid)) {
                transactionsTS.add(transactionST);
                if (transactionST.getErrorCode() < 100) {
                    if (revertedBetTypes.get(transactionST.getBetType()) >= 100) {
                        creditsCount++;
                    }
                    if (revertedBetTypes.get(transactionST.getBetType()) < 100) {
                        debitsCount++;
                    }
                }
            }
        }

        for (int i = transactionsTS.size() - 1; i >= 0; i--) {
            if (transactionsTS.get(i).getErrorCode() >= 100) {
                issuedTransactionsTS.add(transactionsTS.get(i));
                if (transactionsTS.size() > 1) {
                    transactionsTS.remove(transactionsTS.get(i));
                }
            }
        }
        String revenueCalc[] = {"0", "0", "0"};
        if (transactionsTS.size() > 0) {
            revenueCalc = calculateRevenue(transactionsTS);
        }
        transactionsTS.sort(Comparator.comparingInt(TransactionST::getOrderNum).reversed());
        UserST user = new UserST(uid, transactionsTS.size(), debitsCount, creditsCount,
                Double.parseDouble(revenueCalc[0]), Double.parseDouble(revenueCalc[1]), Double.parseDouble(revenueCalc[2]), transactionsTS, issuedTransactionsTS);

        return user;
    }

    private static LinkedHashMap<String, TransactionST> prepareTransactionsForConstCheck(long operatorId, String linesToParse) {
        int orderNum = 1;
        linesToParse = linesToParse.replaceAll(" status 200","");
        String[] lines = linesToParse.split("\n");
        String requestsToParse = "";
        String responsesToParse = "";
        LinkedHashMap<String, TransactionST> transactions = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> transactionOrder = new LinkedHashMap<>();
        for (String line : lines) {
            if (line.contains("Posting financial post to url")) {
                line = line.substring(line.indexOf("{"));
                requestsToParse = requestsToParse.concat(line + "\n");
                JSONObject temp = new JSONObject(line);
                if (temp.getInt("betTypeID") < 100) {
                    transactionOrder.put(temp.getString("transactionId"), -1);
                } else {
                    transactionOrder.put(temp.getString("transactionId"), orderNum);
                    orderNum++;
                }
            } else {
                line = line.substring(line.indexOf("{"));
                responsesToParse = responsesToParse.concat(line + "\n");
                JSONObject temp = new JSONObject(line);
                if (transactionOrder.containsKey(temp.optString("transactionId"))) {
                    transactionOrder.replace(temp.getString("transactionId"), -1, orderNum);
                    orderNum++;
                }
            }
        }
        String[] requests = requestsToParse.split("\n");

        String[] responses = responsesToParse.split("\n");

        for (String request : requests) {
            Double betAmount = 0.0;
            Double winAmount = 0.0;
            String type = "";
            JSONObject toParse = new JSONObject(request);
            if (request.contains("debitAmount")) {
                betAmount = toParse.getDouble("debitAmount");
                type = "debit";


            }
            if (request.contains("rollbackAmount")) {
                betAmount = toParse.getDouble("rollbackAmount");
                type = "rollback";


            }
            if (request.contains("creditAmount")) {
                winAmount = toParse.getDouble("creditAmount");
                type = "credit";

            }
            String transactionId = toParse.getString("transactionId");
            if (!transactions.containsKey(transactionId)) {
                transactions.put(transactionId, new TransactionST((toParse.getLong("roundId")), operatorId, toParse.getString("uid"), GAMEIDS.get(toParse.getInt("gameId")), toParse.optInt("tableId"),
                        toParse.optString("seatId"), toParse.optLong("timestamp") > 0 ? formatDate(new java.util.Date((long) toParse.optLong("timestamp"))) : "No TS in Request", BETTYPES.get(toParse.getInt("betTypeID")), betAmount, winAmount, toParse.getString("currency"),
                        0, 0, "OK", transactionId, 0, transactionOrder.get(transactionId), true));

            } else {
                if (Arrays.stream(responses).anyMatch(r -> r.contains(transactionId))) {
                    if (type.equals("rollback")) {
                        transactions.replace(transactionId, new TransactionST(operatorId, toParse.getLong("roundId"), toParse.getString("uid"), toParse.getString("transactionId"), 100, "TLB or Rollback retry"));
                    }
                    if (type.equals("credit")) {
                        transactions.replace(transactionId, new TransactionST(operatorId, toParse.getLong("roundId"), toParse.getString("uid"), toParse.getString("transactionId"), 101, "Credit retry"));
                    }
                } else {
                    transactions.replace(transactionId, new TransactionST(operatorId, toParse.getLong("roundId"), toParse.getString("uid"), toParse.getString("transactionId"), 102, "No answer"));
                }

            }
        }

        for (String response : responses) {
            JSONObject toParse = new JSONObject(response);
            String transactionId = toParse.optString("transactionId");
            if (!transactions.containsKey(transactionId)) {
                transactions.put(transactionId, new TransactionST(operatorId, toParse.optLong("roundId"), toParse.optString("uid"), transactionId, 104, "Request not found"));
            }
            if (transactions.get(transactionId).getErrorCode() < 100) {
                try {
                    transactions.get(transactionId).setErrorCode(toParse.getInt("errorCode"));
                } catch (JSONException e) {
                    transactions.get(transactionId).setErrorCode(Integer.parseInt(toParse.getString("errorCode")));
                }
                try {
                    String[] toCheckFormat = response.split(",");
                    String balanceToCheckFormat = "";
                    for (String val : toCheckFormat) {
                        if (val.contains("balance")) {
                            balanceToCheckFormat = val;
                            try {
                                if (balanceToCheckFormat.endsWith("\"") || balanceToCheckFormat.endsWith("}")) {
                                    if (balanceToCheckFormat.endsWith("\"}")) {
                                        balanceToCheckFormat = balanceToCheckFormat.substring(balanceToCheckFormat.indexOf("."), balanceToCheckFormat.length() - 2);
                                    } else {
                                        balanceToCheckFormat = balanceToCheckFormat.substring(balanceToCheckFormat.indexOf("."), balanceToCheckFormat.length() - 1);
                                    }
                                } else {
                                    balanceToCheckFormat = balanceToCheckFormat.substring(balanceToCheckFormat.indexOf("."));
                                }
                            } catch (StringIndexOutOfBoundsException e) {
                                balanceToCheckFormat = balanceToCheckFormat.substring(balanceToCheckFormat.indexOf(":") + 1);
                                while (balanceToCheckFormat.charAt(balanceToCheckFormat.length() - 1) < 48 || balanceToCheckFormat.charAt(balanceToCheckFormat.length() - 1) > 57) {
                                    if (balanceToCheckFormat.startsWith("\"")) {
                                        balanceToCheckFormat = balanceToCheckFormat.substring(1);
                                    }
                                    balanceToCheckFormat = balanceToCheckFormat.substring(0, balanceToCheckFormat.length() - 1);
                                }
                                try {
                                    int temp = Integer.parseInt(balanceToCheckFormat);
                                    balanceToCheckFormat = ".00";
                                } catch (NumberFormatException exception) {
                                    balanceToCheckFormat = "0000";
                                }
                            }
                        }
                    }
                    boolean flag = balanceToCheckFormat.length() > 3;
                    transactions.get(transactionId).setBalance(toParse.getDouble("balance"));
                    if (Double.parseDouble(formatMyDouble(toParse.getDouble("balance"))) != toParse.getDouble("balance") || flag) {
                        transactions.get(transactionId).setBalance(-1);
                        transactions.get(transactionId).setErrorCode(103);
                        transactions.get(transactionId).setErrorDescription("Invalid balance format or missing balance");
                    }
                } catch (JSONException e) {
                    try {
                        transactions.get(transactionId).setBalance(Double.parseDouble(toParse.getString("balance")));
                    } catch (JSONException | NumberFormatException ex) {
                        transactions.get(transactionId).setBalance(-1);
                        transactions.get(transactionId).setErrorCode(103);
                        transactions.get(transactionId).setErrorDescription("Invalid balance format or missing balance");
                    }
                }
                try {
                    transactions.get(transactionId).setTimestamp(toParse.optLong("timestamp"));
                } catch (JSONException e) {
                    transactions.get(transactionId).setTimestamp(Long.parseLong(toParse.optString("timestamp")));
                }
                if (transactions.get(transactionId).getTimestamp() == 0) {
                    transactions.get(transactionId).setErrorCode(105);
                    transactions.get(transactionId).setErrorDescription("No timestamp in response");
                }
            }
        }

        return checkOrder(transactions);
    }

    private static LinkedHashMap<String, TransactionST> checkOrder(LinkedHashMap<String, TransactionST> transactions) {
        ArrayList<TransactionST> txns = new ArrayList<>(transactions.values());
        BidiMap<String, Integer> revertedBetTypes = BETTYPES.inverseBidiMap();
        HashSet<String> users = new HashSet<>();
        LinkedHashSet<Long> rounds = new LinkedHashSet<>();
        for (TransactionST txn : txns) {
            users.add(txn.getUid());
            rounds.add(txn.getRoundId());
        }

        ArrayList<TransactionST> tempTransactionList = new ArrayList<>();
        for (String user : users) {
            ArrayList<TransactionST> lastRoundTxns = new ArrayList<>();
            Collections.sort(txns, new Comparator<TransactionST>() {
                @Override
                public int compare(TransactionST o1, TransactionST o2) {
                    return new CompareToBuilder().append(o1.getTimestamp(), o2.getTimestamp()).append(o2.getBet(), o1.getBet()).
                            append(o2.getWin(), o1.getWin()).toComparison();
                }
            });
            for (int i = txns.size() - 1; i >= 0; i--) {
                if (!txns.get(i).getUid().equals(user)) {
                    tempTransactionList.add(txns.get(i));
                    txns.remove(i);
                }
            }
            long lastRound = Collections.max(rounds);
            for (TransactionST txn : txns) {
                if (txn.getRoundId() == lastRound) {
                    lastRoundTxns.add(txn);
                }
            }

            String revenueCalc[] = {"0", "0", "0"};
            if (lastRoundTxns.size() > 0) {
                revenueCalc = calculateRevenue(lastRoundTxns);
            }

            for (int i = 0; i <= txns.size() - 2; i++) {
                if (txns.get(i + 1).getTimestamp() == txns.get(i).getTimestamp()) {
                    if (txns.get(i + 1).getBalance() != txns.get(i).getBalance()) {
                        TransactionST temp = transactions.get(txns.get(i + 1).getTransactionId());
                        temp.setCorrectPlace(false);
                    }
                } else {
                    if (revertedBetTypes.get(txns.get(i + 1).getBetType()) < 100) {
                        if (txns.get(i + 1).getBalance() != txns.get(i).getBalance() && txns.get(i + 1).getBalance() > Double.parseDouble(formatMyDouble(txns.get(i).getBalance() - txns.get(i + 1).getBet()))) {
                            TransactionST temp = transactions.get(txns.get(i + 1).getTransactionId());
                            temp.setCorrectPlace(false);
                        }
                    } else {
                        if (txns.get(i + 1).getBalance() != txns.get(i).getBalance() && txns.get(i + 1).getBalance() < Double.parseDouble(formatMyDouble(txns.get(i).getBalance() + txns.get(i + 1).getWin()))) {
                            TransactionST temp = transactions.get(txns.get(i + 1).getTransactionId());
                            temp.setCorrectPlace(false);
                        }
                    }
                }
                if (i == txns.size() - 2 && !revenueCalc[1].equals(revenueCalc[0]) && !revenueCalc[1].equals(revenueCalc[2]) && !revenueCalc[2].equals("10000000000000000000.00")) {
                    TransactionST temp = transactions.get(txns.get(i + 1).getTransactionId());
                    temp.setCorrectPlace(false);
                }
            }
            txns = (ArrayList<TransactionST>) tempTransactionList.clone();
            tempTransactionList.clear();
        }
        return transactions;
    }

    private static String[] calculateRevenue(ArrayList<TransactionST> transactionsST) {
        String[] res = new String[3];
        double debitSum = 0.0;
        double creditSum = 0.0;
        double maxDebitBalance = -1.0;
        TransactionST maxDebitTransactionST = new TransactionST();
        double minDebitBalance = 10000000000000000000.0;
        double maxCreditBalance = 0.0;
        TransactionST maxCreditTransactionST = new TransactionST();
        double minCreditBalance = 10000000000000000000.0;
        boolean tipOnly = true;
        boolean surrenderOnly = true;
        boolean lastTransactionTip = false;
        ArrayList<TransactionST> debits = new ArrayList<>();
        ArrayList<TransactionST> credits = new ArrayList<>();
        HashMap<String, Double> seatAndBet = new HashMap<>();
        BidiMap<String, Integer> revertedBetTypes = BETTYPES.inverseBidiMap();
        int index = 0;
        for (TransactionST transaction : transactionsST) {

            debitSum = debitSum + transaction.getBet();
            creditSum = creditSum + transaction.getWin();

            if (transaction.getErrorCode() < 100 && revertedBetTypes.get(transaction.getBetType()) < 100) {
                if (revertedBetTypes.get(transaction.getBetType()) != 3) {
                    tipOnly = false;
                }
                if (index == transactionsST.size() - 1 && revertedBetTypes.get(transaction.getBetType()) == 3 && !tipOnly) {
                    lastTransactionTip = true;
                }
                debits.add(transaction);
                seatAndBet.put(transaction.getSeatId(), transaction.getBet());
                if (transaction.getBalance() > maxDebitBalance && !lastTransactionTip && credits.size() == 0) {
                    maxDebitBalance = transaction.getBalance();
                    maxDebitTransactionST = transaction;
                }
                if (transaction.getBalance() < minDebitBalance) {
                    minDebitBalance = transaction.getBalance();
                }
                if (lastTransactionTip) {
                    maxCreditBalance = transaction.getBalance();
                }
            } else {
                credits.add(transaction);
                if ((seatAndBet.size() == 0 || !surrender(transaction, seatAndBet)) || minCreditBalance < 10000000000000000000.0) {
                    surrenderOnly = false;
                    if (transaction.getBalance() > maxCreditBalance) {
                        maxCreditBalance = transaction.getBalance();
                        maxCreditTransactionST = transaction;
                    }
                }
                if (transaction.getBalance() < minCreditBalance) {
                    minCreditBalance = transaction.getBalance();
                }
            }
            index++;
        }
        transactionsST.sort(Comparator.comparingLong(TransactionST::getTimestamp).reversed());
        res[0] = formatMyDouble(creditSum - debitSum);
        res[1] = formatMyDouble((transactionsST.get(0).getBalance() - transactionsST.get(transactionsST.size() - 1).getBalance()) - transactionsST.get(transactionsST.size() - 1).getBet());
        if (tipOnly) {
            res[2] = formatMyDouble(minDebitBalance - maxDebitBalance - maxDebitTransactionST.getBet());

        } else if (surrenderOnly) {
            res[2] = formatMyDouble(minCreditBalance - maxDebitBalance - maxDebitTransactionST.getBet());
        } else {
            res[2] = formatMyDouble(maxCreditBalance - maxDebitBalance - maxDebitTransactionST.getBet());

        }
        return res;
    }


    private static HashMap<String, String> prepareLines(int requestType, long operatorId, SearchData searchData) {
        if (searchData.getLogToParse().startsWith("**")) {
            searchData.setLogToParse(prepareProductionLog(searchData.getLogToParse()));
        }
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
        String[] roundSTS = new String[searchData.getRoundsToAnalyse().size()];
        for (int i = 0; i < roundSTS.length; ++i) {
            roundSTS[i] = String.valueOf(searchData.getRoundsToAnalyse().get(i));
        }
        String[] lines = searchData.getLogToParse().split("\n");
        for (String line : lines) {
            if ((((line.contains("Got operator response") || line.contains("Posting financial post to url")) ||
                    (lineToParse.contains("Got operator response") || lineToParse.contains("Posting financial post to url"))
                            && (line.contains("{") || lineToParse.contains("{"))) || line.contains("Post to operator took"))) {
                if ((line.indexOf("{") > 0 && line.indexOf("}") > 0) && Arrays.stream(roundSTS).anyMatch(line::contains) && line.contains(String.valueOf(operatorId))
                        || (line.contains("Post to operator took") && !line.contains("null")) && Arrays.stream(roundSTS).anyMatch(line::contains) && line.contains(String.valueOf(operatorId))) {
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
//                        System.out.println(x[1].strip());
//                        if (x[1].endsWith("|")) {
//                            x[1] = x[1].substring(0, x[1].length() - 1).trim();
//                        }
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
                        if (Arrays.stream(roundSTS).anyMatch(lineToParse::contains) && lineToParse.contains(String.valueOf(operatorId))) {
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
        response.put("averageResponseTime", String.valueOf(timeSum / timeCount));
        response.put("result", result);
        return response;
    }

    private static synchronized Object getTSOperatorIdRoundId(int dataType, SearchData searchData, String
            searchType, long operatorId) {
        if (searchData.getLogToParse().startsWith("**")) {
            searchData.setLogToParse(prepareProductionLog(searchData.getLogToParse()));
        }
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

        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);

            return (toParse.getString("transactionId") + "\t" + toParse.getBigInteger("timestamp") + "\n");

        } catch (JSONException e) {
            return "";
        }

    }

    private static String getOperatorId(String line) {

        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);

            return (String.valueOf(toParse.getLong("operatorId")));

        } catch (JSONException e) {
            return "";
        }
    }

    private static String getUid(String line) {
        line = line.substring(line.indexOf("{"));
        try {
            JSONObject toParse = new JSONObject(line);

            return toParse.getString("uid");

        } catch (JSONException e) {
            return "";
        }
    }

    private static String getRoundId(String line, long operatorId) {

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

    private static String prepareProductionLog(String log) {
        String[] lines = log.split("\n");
        List linesArray = Arrays.asList(lines);
        Collections.reverse(linesArray);
        log = "";
        for (Object line : linesArray) {
            String[] x = String.valueOf(line).split("\\|", 3);
            if (x[x.length - 1].endsWith("|")) {
                x[x.length - 1] = x[x.length - 1].substring(0, x[x.length - 1].length() - 1).trim();
            }
            log = log.concat(x[x.length - 1] + "\n");
        }
        return log;
    }

    private static boolean surrender(TransactionST transaction, HashMap<String, Double> seatAndBet) {
        try {
            if (transaction.getGameType().equals("Blackjack") && transaction.getBetType().equals("Game Credit") && transaction.getWin() == (seatAndBet.get(transaction.getSeatId()) / 2)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        return format.format(date);
    }

    private static String formatMyDouble(double num) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(num);
    }
}
