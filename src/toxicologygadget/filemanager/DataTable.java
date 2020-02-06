package toxicologygadget.filemanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DataTable {
	
	private ArrayList<ArrayList<Object>> mTable;
	private String[] mColumnIdentifiers;
	
	public DataTable(ArrayList<ArrayList<Object>> table, String[] columnIdentifiers){
		mTable = table;
		mColumnIdentifiers = columnIdentifiers;
	}
	
	public DataTable(String[][] table){
		
		mColumnIdentifiers = table[0];

		mTable = new ArrayList<ArrayList<Object>>();
		
		for(int i = 1; i < table.length; i++) {
			ArrayList<Object> row = new ArrayList<Object>();
			
			for(int j = 0; j < table[i].length; j++) {
				row.add(table[i][j]);
			}
			
			mTable.add(row);
			
		}
		
	}
	
	public boolean hasColumn(String col) {
		for(int i = 0; i < mColumnIdentifiers.length; i++) {
			if(mColumnIdentifiers[i].equals(col)) 
				return true;
		}
		return false;
	}
	
	public int columnIndex(String col){
		for(int i = 0; i < mColumnIdentifiers.length; i++) {
			if(mColumnIdentifiers[i].equals(col)) 
				return i;
		}
		return -1;
	}
		
	public static ArrayList<Integer> uniqueIndicies(String[] a, String[] b){
		
		ArrayList<Integer> aIndicies = new ArrayList<Integer>();
		
		for(int i = 0; i < a.length; i++) {
			boolean foundCol = false;
			for(int j = i; j < b.length; j++) {
				if(a[i].equals(b[j])){
					foundCol = true;
					break;
				}
			}
			
			if(!foundCol) {
				aIndicies.add(i);
			}
		}
		
		return aIndicies;
		
	}
	
	public static DataTable leftJoin(DataTable a, DataTable b, String keyCol) {
		
		// TODO: Make exceptions
		if(!a.hasColumn(keyCol) || !b.hasColumn(keyCol)) 
			return null; 
		
		int keyIndexA = a.columnIndex(keyCol);
		int keyIndexB = b.columnIndex(keyCol);
		
		ArrayList<Integer> aColumnSelect = uniqueIndicies(a.mColumnIdentifiers, b.mColumnIdentifiers);
		aColumnSelect.add(keyIndexA, 0);
		ArrayList<Integer> bColumnSelect = uniqueIndicies(b.mColumnIdentifiers, a.mColumnIdentifiers);
		
		String[] newColumnIdentifiers = joinColumns(aColumnSelect, bColumnSelect, a.mColumnIdentifiers, b.mColumnIdentifiers);
		ArrayList<Object> emptyArray = new ArrayList<Object>();
		
		for(int i = 0; i < bColumnSelect.size(); i++) {
			emptyArray.add(null);
		}
		
		
		ArrayList<ArrayList<Object>> newTable = new ArrayList<ArrayList<Object>>();
		
		
		for(int i = 0; i < a.mTable.size(); i++){
			
			boolean match = false;
			
			for(int j = i; j < b.mTable.size(); j++){
				Object aKey = a.mTable.get(i).get(keyIndexA);
				Object bKey = b.mTable.get(j).get(keyIndexB);
				
				
				
				if (aKey.equals(bKey)){
					ArrayList<Object> leftRow = a.mTable.get(i);
					ArrayList<Object> rightRow = b.mTable.get(j);
					ArrayList<Object> row = joinRows(aColumnSelect, bColumnSelect, leftRow, rightRow);
					newTable.add(row);
					match = true;
				}
				
				
			}
			
			if(!match) {
				ArrayList<Object> row = (ArrayList<Object>) a.mTable.get(i).clone();
				row.addAll(emptyArray);
				newTable.add(row);
			}
			
		}
		
		return new DataTable(newTable, newColumnIdentifiers);
		
	}

	private static ArrayList<Object> joinRows(ArrayList<Integer> aColumnSelect, ArrayList<Integer> bColumnSelect,
											  ArrayList<Object> leftRow, 		ArrayList<Object> rightRow) {
		ArrayList<Object> row = new ArrayList<Object>();
		for(Integer i : aColumnSelect) {
			row.add(leftRow.get(i));
		}
		
		for(Integer i : bColumnSelect) {
			row.add(rightRow.get(i));
		}
		return row;
	}
	
	private static String[] joinColumns(ArrayList<Integer> aColumnSelect, 
										ArrayList<Integer> bColumnSelect,
										String[] leftCol, 
										String[] rightCol) 
	{
		
		ArrayList<String> row = new ArrayList<String>();
		for(Integer i : aColumnSelect) {
			row.add(leftCol[i]);
		}
		
		for(Integer i : bColumnSelect) {
			row.add(rightCol[i]);
		}
		
		return row.toArray(new String[row.size()]);
		
	}	
	
	
}