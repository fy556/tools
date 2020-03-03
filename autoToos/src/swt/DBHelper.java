package swt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper implements DBConfig{

	private static Connection conn;
	private static Statement stat;
	private static ResultSet rs;
	public static Connection getConnection() {                                      // ���Connection���Ͷ���
		try {
			Class.forName(Driver);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			System.out.println("���ݿ����ӳɹ�");
			return conn;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Statement openStatement() {                                       // ���Statement���Ͷ���

		try {

			stat = getConnection().createStatement();

			return stat;

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return null;

	}

	public static int update(String sql) {                                                // ������ݵĸ��²���

		try {

			return openStatement().executeUpdate(sql);

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return -1;

	}
	public static ResultSet query(String sql) {                                       // ������ݵĲ�ѯ����

		try {

			rs = openStatement().executeQuery(sql);

			return rs;

		} catch (SQLException e) {

			e.printStackTrace();

		}

		return null;

	}
	public static void close() {                                                      // �ͷ���Դ

		if (rs != null) {

			try {

				rs.close();

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		if (stat != null) {

			try {

				stat.close();

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

		if (conn != null) {

			try {

				conn.close();

			} catch (SQLException e) {

				e.printStackTrace();

			}

		}

	}


}