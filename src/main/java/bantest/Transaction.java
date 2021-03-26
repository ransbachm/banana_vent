package bantest;

import java.math.BigInteger;

public class Transaction {
	// https://docs.nano.org/commands/rpc-protocol/#account_history
	public TransactionType type;
	public String account;
	public BigInteger amount;
	public int height;
	public String hash;

	private long local_timestamp;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Transaction)) {
			return false;
		}
		Transaction trans = (Transaction) obj;
		return trans.hash.equals(hash);
	}

	public long getLocal_timestamp() {
		return local_timestamp;
	}

	public void setLocal_timestamp(long local_timestamp) {
		this.local_timestamp = local_timestamp * 1000; // adjust to miliseconds

	}
	
	@Override
	public String toString() {
		return "Transaction [type=" + type + ", account=" + account + ", amount=" + amount + ", local_timestamp="
				+ local_timestamp + ", height=" + height + ", hash=" + hash + "]";
	}

}
