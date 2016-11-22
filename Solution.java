/* thumbtack  oa*/
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

 
public class Solution {
    private static HashMap<String, String> updateMap = new HashMap<>();
    private static HashMap<String, Integer> countMap = new HashMap<>();
    private static StringBuilder CommandHub = new StringBuilder();
    private static boolean state = false;
    
    public static void main(String args[] ) throws Exception {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT */
        Scanner scan = new Scanner(System.in);
        while(scan.hasNextLine()) {
            String cur = scan.nextLine();
            System.out.println(cur);
            if(cur.equals("END")) {
                scan.close();
                System.exit(0);
            }
            
            parseStr(cur);
        }
    }
    private static void parseStr(String str) {
        String[] strs = str.split(" ");
        switch(strs[0]) {
            case "SET": 
                if(strs.length < 3) {
                    System.out.println("Eg: SET a 10");
                } else {
                    set(strs[1], strs[2]);
                    CommandHub.append(str+",");
                }
           
            break;
            
            case "UNSET":
                if(strs.length < 2) {
                    System.out.println("Eg: UNSET a");                   
                } else {
                    set(strs[1], "NULL");
                    CommandHub.append(str+",");
                }
                break;
            case "GET":
                if(strs.length < 2) {
                    System.out.println("Eg: UNSET a");                   
                } else {
                    get(strs[1]);
                }
                break;
            case "NUMEQUALTO":
                if(strs.length < 2) {
                    System.out.println("Eg: NUMEQUALTO a");                   
                } else {
                    numEqualTo(strs[1]);
                }
                break;
            case "BEGIN":
                if(strs.length > 1) {
                    System.out.println("Eg: BEGIN");                   
                } else {
                    CommandHub.append("|");
                }
                state = true;
           
                break;
            case "COMMIT":
                if(strs.length > 1) {
                    System.out.println("Eg: COMMIT");                   
                } else {
                    if(!state) System.out.println("> NO TRANSACTION");
                    else {
                        CommandHub.setLength(0);
                        state = false;

                    }
                }
            
                break;
            case "ROLLBACK":
                if(strs.length > 1) {
                    System.out.println("Eg: ROLLBACK");                   
                } else {
                    if(!state) System.out.println("> NO TRANSACTION");
                    else {
                        rollBack();

                    }
                }
                break;
            default:
                System.out.println("Eg:\nSET a 10\nUNSET a\nGET a\nNUMEQUALTO a\nBEGIN\nCOMMIT\nROLLBACK");
                break;

        }
    }
    private static void set(String key, String val) {
        String oldVal = updateMap.get(key);
        if(oldVal != null && oldVal != val) {
            int tmp = countMap.get(oldVal)-1;
            countMap.put(oldVal, tmp);

        }
        updateMap.put(key,val);
        countMap.put(val, countMap.getOrDefault(val, 0)+1);

    }
    private static void get(String key) {
        String res = updateMap.get(key);
        System.out.println("> " + res);
    }
    private static void numEqualTo(String key) {
        System.out.println("> " + countMap.getOrDefault(key, 0));
    }
    private static void rollBack() {
        int index = CommandHub.toString().lastIndexOf("|");
        CommandHub.delete(index, CommandHub.length());
        String[] transactions = CommandHub.toString().split("\\|");
        for(String transactionBlock: transactions) {
            if(transactions.length == 1 && transactionBlock.isEmpty()) {
                for(String key: updateMap.keySet()) {
                    countMap.put(updateMap.get(key), countMap.get(updateMap.get(key))-1);
                    set(key, "NULL");
                }
                
            } else {
                String[] commands = transactionBlock.split(",");
                for(String command: commands) {
                    command = command.trim();
                    String[] each = command.split(" ");
                    switch (each[0]) {
                        case "SET":
                            set(each[1], each[2]);
                            break;
                        case "UNSET":
                            set(each[1], "NULL");
                           
                    }
                }
                state = true;
            }
        }
    }
    
}
