package bantest;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
	public List<Transaction> transactions = new ArrayList<>();
	public boolean isNewHistory = true;
	
	public void add(Transaction trans) {
		isNewHistory = false;
		transactions.add(trans);
	}
	
	public boolean isNew(Transaction trans) {
		return !isNewHistory && 
				!transactions.contains(trans);
	}
	
}
	