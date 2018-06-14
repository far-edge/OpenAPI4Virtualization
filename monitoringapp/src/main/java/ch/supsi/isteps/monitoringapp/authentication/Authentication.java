package ch.supsi.isteps.monitoringapp.authentication;

public class Authentication {
	
	private String username;
	private String password;
	
	
	public Authentication() {
		setUsername("admin");
		setPassword("admin");
	}
	
	public Boolean authenticate(String username, String password){
		//decomment for testing purposes to login without credentials
		if(username.equals("")&&username.equals("")) {
			return true;
		}
		if(username.equals(getUsername()) && password.equals(getPassword())){
			return true;
		}
		return false;
	}

	private void setUsername(String username) {
		this.username = username;
	}
	
	private String getUsername(){
		return this.username;
	}

	private void setPassword(String password) {
		this.password = password;
	}
	
	private String getPassword(){
		return this.password;
	}
}