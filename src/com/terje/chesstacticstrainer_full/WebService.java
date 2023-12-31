package com.terje.chesstacticstrainer_full;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;


	


	public class WebService extends AsyncTask<String, String, String>{

		public final String SERVICE_URI = "http://teraim.com/webservice.php?";

		public interface WsCallback {
			public void doSomething(String res);
		}

		WsCallback myC;
		public WebService(WsCallback c) {
			super();
			myC = c;
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			ByteArrayOutputStream out = null;
			try {
				response = httpclient.execute(new HttpGet(SERVICE_URI+uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					responseString = out.toString();
					out.close();

				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems..
			}  catch (IOException e) {
				//TODO Handle problems..
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			myC.doSomething(result);
		}
	}

