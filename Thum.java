import java.io.*;
import java.util.*;

public class Thum {
	static HashMap<String, Integer> var_value = new HashMap<String, Integer>();
	static HashMap<Integer, Integer> value_count = new HashMap<Integer, Integer>();
	static Stack<String> undo_commends = new Stack<String>();
	static int tranNum = 0;
	
	
	public enum Commend{
		GET, SET, UNSET, NUMEQUALTO, ROLLBACK, COMMIT, END, BEGIN
	}
	
	public static void main(String args[]){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String line = sc.nextLine();
			if (line.equals("END")) {
				sc.close();
				System.exit(0);
			}
			runLineCommend(line);
		}
		
	}
	
	public static void minusCount(int value){
		if(value_count.get(value) == 1){
			value_count.remove(value);
		}
		else{
			value_count.put(value, value_count.get(value) - 1);
		}
	}
	
	public static void addCount(int value){
		if(value_count.containsKey(value)){
			value_count.put(value, value_count.get(value) + 1);
		}
		else{
			value_count.put(value, 1);
		}
	}
	
	public static void unsetCount(String var){
		minusCount(var_value.get(var));
	}
	
	public static void setCount(String var, int value){
		if(var_value.containsKey(var)){
			minusCount(var_value.get(var));
			addCount(value);
		}
		else{
			addCount(value);
		}
	}
	
	public static void runUndoCommend(String line){
		String[] commend_strs = line.split("\\s+");
		if(commend_strs.length == 3 && commend_strs[0].equals("SET")){
			setCount(commend_strs[1], Integer.valueOf(commend_strs[2]));
			var_value.put(commend_strs[1], Integer.valueOf(commend_strs[2]));
		}
		else if(commend_strs.length == 2 && commend_strs[0].equals("UNSET")){
			unsetCount(commend_strs[1]);
			var_value.remove(commend_strs[1]);
		}
		else{
			System.out.println("Invalid Commend");
		}
	}
	
	public static void runLineCommend(String line){
		String[] commend_strs = line.split("\\s+");
		if(commend_strs.length < 1){
			System.out.println("Invalid Commend!");
		}
		switch(Commend.valueOf(commend_strs[0].toUpperCase())){
			case GET:
				getCommend(commend_strs);
				break;
			case SET:
				setCommend(commend_strs);
				break;
			case UNSET:
				unsetCommend(commend_strs);
				break;
			case NUMEQUALTO:
				numequaltoCommend(commend_strs);
				break;
			case ROLLBACK:
				rollbackCommend(commend_strs);
				break;
			case COMMIT:
				commitCommend(commend_strs);
				break;
			case BEGIN:
				beginCommend(commend_strs);
				break;
			case END:
				return;
			default:
				System.out.println("Main:");
				System.out.println("Invalid Commend");
				
		}
	}
	
	public static void getCommend(String[] commend_strs){
		if(commend_strs.length != 2){
			System.out.println("Invalid Commend");
			return;
		}
		if(var_value.containsKey(commend_strs[1])){
			System.out.println(var_value.get(commend_strs[1]));
		}
		else{
			System.out.println("NULL");
		}
		return;
	}
	
	public static void setCommend(String[] commend_strs){
		if(commend_strs.length != 3){
			System.out.println("Invalid Commend");
			return;
		}
		setCount(commend_strs[1], Integer.valueOf(commend_strs[2]));
		if(var_value.containsKey(commend_strs[1])){
			int preNum = var_value.get(commend_strs[1]);
			String undoStr = "SET " +  commend_strs[1] + " " + Integer.toString(preNum);
			undo_commends.add(undoStr);
			var_value.put(commend_strs[1], Integer.valueOf(commend_strs[2]));
			
		}
		else{
			String undoStr = "UNSET " +  commend_strs[1];
			undo_commends.add(undoStr);
			var_value.put(commend_strs[1], Integer.valueOf(commend_strs[2]));
		}
		return;
	}
	
	public static void unsetCommend(String[] commend_strs){
		if(commend_strs.length != 2){
			System.out.println("Invalid Commend");
			return;
		}
		
		if(var_value.containsKey(commend_strs[1])){
			unsetCount(commend_strs[1]);
			int preNum = var_value.get(commend_strs[1]);
			String undoStr = "SET " +  commend_strs[1] + " " + Integer.toString(preNum);
			undo_commends.add(undoStr);
			var_value.remove(commend_strs[1]);
			
		}
		return;
	}
	
	public static void numequaltoCommend(String[] commend_strs){
		if(commend_strs.length != 2){
			System.out.println("Invalid Commend");
			return;
		}
		if(value_count.containsKey(Integer.valueOf(commend_strs[1]))){
			System.out.println(value_count.get(Integer.valueOf(commend_strs[1])));
		}
		else{
			System.out.println("No such value!");
		}
	}
	
	public static void beginCommend(String[] commend_strs){
		if(commend_strs.length != 1){
			System.out.println("Invalid Commend");
			return;
		}
		undo_commends.add("|");
		tranNum++;
		
	}
	
	public static void rollbackCommend(String[] commend_strs){
		if(tranNum <= 0){
			System.out.println("Nothing to rollback!");
			return;
		}
		if(commend_strs.length != 1){
			System.out.println("Invalid Commend");
			return;
		}
		if(undo_commends.isEmpty()){
			System.out.println("Nothing to rollback!");
		}
		while(!undo_commends.isEmpty()){
			if(undo_commends.peek() == "|"){
				undo_commends.pop();
				break;
			}
			runUndoCommend(undo_commends.peek());
			undo_commends.pop();
		}
		tranNum--;
	}
	public static void commitCommend(String[] commend_strs){
		tranNum = 0;
		if(commend_strs.length != 1){
			System.out.println("Invalid Commend");
			return;
		}
		undo_commends.clear();
	}
	
}
