package swt;

public interface DBConfig {
//	String Driver="com.mysql.jdbc.Driver";
	String Driver="com.mysql.cj.jdbc.Driver";
	String URL="jdbc:mysql://localhost:3308/test?serverTimezone=GMT%2B8";
	String USERNAME = "root";
	String PASSWORD = "123456"; 
}
