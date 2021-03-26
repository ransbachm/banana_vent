package bantest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import bantest.requests.Account_history_Request;


public class Main {
	static ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		// A public API is found here -> https://nanoo.tools/bananode-api
		// This program does use unconfirmed blocks. It's only meant as a POC
		System.err.println("Since this is a simple POC you will need to ensure that you can continiously receive your pending Ban.");
		System.err.println("I would sugest keeping your wallet open and refreshing it from time to time.");
		
		BigInteger oneMilliBan = new BigInteger("100000000000000000000000000");
		String endpoint = "https://api-beta.banano.cc";
		String accountToWatch = "--insert here--";
		
		File storage = Paths.get(System.getProperty("java.io.tmpdir")).resolve("bananoVending.json").toFile();
		TransactionHistory history;
		try {
			history = objectMapper.readValue(storage, TransactionHistory.class);
			// System.out.println("A db was loaded!");

		} catch (IOException e) {
			history = new TransactionHistory();
			// System.out.println("No db was loaded");
		}
		
		printProductInfo(accountToWatch);
		
		while (true) {
			try {
				Transaction trans = getLastTransaction(endpoint, accountToWatch);
				if (trans.type == TransactionType.receive) {
					if (history.isNew(trans)) {
						int priceMiliBan = trans.amount.divide(oneMilliBan).intValue();
						
						Product product = Product.valueOf(priceMiliBan);
						if (product != null) {
							System.out.println("You bought " + product + " for " + priceMiliBan + " 1/1000 Ban.");
						} else {
							System.out.println("That does not exist (" + priceMiliBan + " 1/1000 Ban).");
						}
						
						history.add(trans);
						objectMapper.writeValue(storage, history);
					} else if (history.isNewHistory) {
						history.add(trans);
						objectMapper.writeValue(storage, history);
					}
				}

			} catch (IOException | JSONException e) {
				System.err.println("There was an error fetching the history. "
						+ "Check your the given address and your internet conection.");
			}

			try {Thread.sleep(1000);} catch (Exception e) {e.printStackTrace();} // sleep for 1 second
		}
		
	}
	
	private static void printProductInfo(String accountToWatch) {
		System.out.println("This is a \"vending mashine\".");
		System.out.println("You can buy an item by sending the specified ammount of Ban to the address below.");
		System.out.println(accountToWatch);
		System.out.println(" --Available Products-- ");
		for(Product product : Product.values()) {
			System.out.printf("\t%s \t -> %d [%.02f Ban]%n", 
					product.toString(), 
					product.getPriceMiliBan(), 
					((float)product.getPriceMiliBan()) / 1000);
		}
	}
	
	private static Transaction getLastTransaction(String apiBase, String account) throws IOException {
		// this is to ensure not missing a transaction if there are multiple ones in a short span 
		String response = httpRequest(apiBase, new Account_history_Request(account, 6)); 
		
		JSONObject res = new JSONObject(response);
		JSONArray allTransactions = res.getJSONArray("history");
		JSONObject lastTransation = allTransactions.getJSONObject(0);
		
		Transaction transaction = objectMapper.readValue(lastTransation.toString(), Transaction.class);
		return transaction;
	}
	
	private static String httpRequest(String apiBase, Object content) throws IOException {
		URL url = new URL(apiBase);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		
		OutputStream os = con.getOutputStream();
		os.write(objectMapper.writeValueAsBytes(content));
		os.close();
		
		InputStream is = con.getInputStream();
		String response = new String(is.readAllBytes());

		con.disconnect();
		return response;
	}
	
}
