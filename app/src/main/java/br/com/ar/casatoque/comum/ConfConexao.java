
package br.com.ar.casatoque.comum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.content.Context;
import android.widget.Toast;


public class ConfConexao {

	private HttpClient client;
	private String URL;

	// mensagens recebidas do SAR   
	public static final String MSG_SUCESSO = "0";
    public static final String MSG_ERRO = "1";
	
	// mensagens enviadas para o SAR   
    public static final String MSG_AUTENTICACAO = "2";
    public static final String MSG_CONTROLE = "3";
    public static final String REQUEST_DISP = "4";
    public static final String MSG_VARREDURA = "5";
    public static final String MSG_PERFIL = "6";
    public static final String REQUEST_TIPO = "7";   
    public static final String MSG_SET_NOME = "11";
    public static final String REQUEST_PERFIL_UUID = "12";
    public static final String REQUEST_PERFIL = "13";
    public static final String SET_VARREDURA = "14";

	public ConfConexao(Context context, String url, String porta, boolean isHttp) {
			
		if(isHttp == true){
			URL = "http://"+url+":"+porta+"/DroidLarWS/Servidor";
			client = new DefaultHttpClient();
			client.getParams().setParameter("http.useragent", "DroidLar - Cliente Android");
		}else{
			
			try {
				
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				SchemeRegistry schemeRegistry = new SchemeRegistry();
				schemeRegistry.register(new Scheme("https", sf, Integer.parseInt(porta)));
				HttpParams params = new BasicHttpParams();
				SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);

				URL = "https://" + url + ":" + porta + "/DroidLarWS/Servidor";
				client = new DefaultHttpClient(mgr, params);
				
			} catch (Exception e) {
				
				URL = "http://" + url + ":" + porta + "/DroidLarWS/Servidor";
				client = new DefaultHttpClient();				
				Toast.makeText(context, "Erro no HTTPS - Conectado com HTTP", Toast.LENGTH_LONG).show();
				
			}finally{
				client.getParams().setParameter("http.useragent", "DroidLar - Cliente Android");
			}
		}
		
	}

	/**
	 * Envia mensagens HTTP para o SAR utilizando o metodo POST.
	 * @param tipo tipo de mensagem enviada
	 * @param valor1 campo variavel
	 * @param valor2 campo variavel
	 * @param valor3 campo variavel
	 * @return resposta do SAR a mensagem enviada
	 */
	public String enviaMensagem(String tipo, String valor1, String valor2, String valor3) {
		String resposta = "";
		try {

			String campo1 = "";
			String campo2 = "";
			String campo3 = "";

			if (tipo.equals(MSG_AUTENTICACAO)) {
				campo1 = "usuario";
				campo2 = "senha";
			} else {
				campo1 = "modulo";
				campo2 = "mensagem";
				campo3 = "opcao";
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("tipo", "" + tipo));
			nameValuePairs.add(new BasicNameValuePair(campo1, valor1));
			nameValuePairs.add(new BasicNameValuePair(campo2, valor2));
			nameValuePairs.add(new BasicNameValuePair(campo3, valor3));

			HttpPost httppost = new HttpPost(URL);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Executa o HTTP Post Request
			HttpResponse response = client.execute(httppost);
			
			resposta = lerResposta(response.getEntity().getContent()).toString();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resposta;
	}

	private StringBuilder lerResposta(InputStream is) throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		// Return full string
		return total;
	}
	
	private class MySSLSocketFactory extends SSLSocketFactory {
	    SSLContext sslContext = SSLContext.getInstance("TLS");

	    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
	        super(truststore);

	        TrustManager tm = new X509TrustManager() {
	            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	            }

	            public X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
	        };

	        sslContext.init(null, new TrustManager[] { tm }, null);
	    }

	    @Override
	    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {

            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	    }

	    @Override
	    public Socket createSocket() throws IOException {

            return sslContext.getSocketFactory().createSocket();
	    }
	}
}