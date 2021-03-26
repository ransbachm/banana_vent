package bantest.requests;

public class Account_history_Request {
	// https://docs.nano.org/commands/rpc-protocol/#account_history
	public String action = "account_history";
	public String account;
	public int count;
	
	public Account_history_Request(String account,  int count) {
		this.account = account;
		this.count = count;
	}
	
}
