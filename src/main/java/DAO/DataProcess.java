package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import DTO.Writer;

public class DataProcess {
	Connection conn = null;
	Statement stmt = null;
	
	public DataProcess() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void data_Connec() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/websitedb","root","1234");
			stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void data_Close() {
		try {
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void review_insert(Writer obj) {
		data_Connec();
		try {
			stmt = conn.createStatement();
			String command = "insert into bulletin_board_review values(0, '"
					+ obj.getUser_id() + "', now(), '"
					+ obj.getPost_title() + "', '"
					+ obj.getPost_body() + "', '"
					+ obj.getPost_file() + "', '"
					+ obj.getPost_category() + "', '"
					+ obj.getPost_img() + "', 0, "
					+ obj.getPost_like() + ", '"
					+ obj.getPost_travel_location() + "',"
					+ obj.getPost_rating() +
					");";
			stmt.executeUpdate(command);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_Close();
		}
	}
	
	public ArrayList<Writer> review_check() {
		data_Connec();
		ArrayList<Writer> arr = new ArrayList();
			try {
				ResultSet rs = stmt.executeQuery("select DATE_FORMAT(post_date, '%y.%m.%d') as post_date, post_category, post_readcount, post_like, post_num ,post_file, post_title, post_travel_location, post_rating, user_id from bulletin_board_review order by post_num desc;");
				while(rs.next()) {
					Writer wr = new Writer();
					
					wr.setPost_num(rs.getInt("post_num"));
					wr.setPost_category(rs.getString("post_category"));
					wr.setPost_readcount(rs.getInt("post_readcount"));
					wr.setPost_like(rs.getInt("post_like"));
					wr.setPost_title(rs.getString("post_title"));
					wr.setPost_date(rs.getString("post_date"));
					wr.setPost_file(rs.getString("post_file"));
					wr.setPost_travel_location(rs.getString("post_travel_location"));
					wr.setPost_rating(rs.getDouble("post_rating"));
					wr.setUser_id(rs.getString("user_id"));
					
					arr.add(wr);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				data_Close();
			}
			return arr;
		}
	
	public ArrayList<Writer> review_print(String title, int num) {
		data_Connec();
		ArrayList<Writer> arr = new ArrayList();
			try {
				ResultSet rs = stmt.executeQuery("select post_category, post_like, post_readcount, post_img, post_num,post_body,user_id,post_title,post_rating,post_travel_location, post_file, DATE_FORMAT(post_date, '%y-%m-%d') as post_date from bulletin_board_review where post_title='" + title + "' and post_num=" + num + "");
				while(rs.next()) {
					Writer wr = new Writer();
					wr.setPost_num(rs.getInt("post_num"));
					wr.setUser_id(rs.getString("user_id"));
					wr.setPost_title(rs.getString("post_title"));
					wr.setPost_rating(rs.getDouble("post_rating"));
					wr.setPost_travel_location(rs.getString("post_travel_location"));
					wr.setPost_file(rs.getString("post_file"));
					wr.setPost_date(rs.getString("post_date"));
					wr.setPost_body(rs.getString("post_body"));
					wr.setPost_category(rs.getString("post_category"));
					wr.setPost_like(rs.getInt("post_like"));
					wr.setPost_readcount(rs.getInt("post_readcount"));
					wr.setPost_img(rs.getString("post_img"));
					arr.add(wr);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				data_Close();
			}
			return arr;
		}
	public void review_readcount(int readcount, int num) {
		data_Connec();
		try {
			int count = readcount + 1;
			stmt = conn.createStatement();
			String command = "update bulletin_board_review set post_readcount=" + count + " where post_num=" + num + ";";
			stmt.executeUpdate(command);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_Close();
		}
	}
	public void review_update(Writer obj) {
		data_Connec();
		try {
			stmt = conn.createStatement();
			String command = "update bulletin_board_review set post_title='" + obj.getPost_title() + "',post_rating=" + obj.getPost_rating() + ",post_date=now(), post_body='" +  obj.getPost_body() + "' where post_num = " + obj.getPost_num() + ";";
			stmt.executeUpdate(command);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_Close();
		}
	}
	public void review_delete(Writer obj) {
		data_Connec();
		try {
			stmt = conn.createStatement();
			String command = "delete from bulletin_board_review where post_num="+obj.getPost_num()+"";
			stmt.executeUpdate(command);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			data_Close();
		}
	}
	
	public String review_before(int num) {
		data_Connec();
		String title = "";
			try {
				ResultSet rs = stmt.executeQuery("select post_title from bulletin_board_review where post_num = (select post_num from bulletin_board_review where post_num <" + num + " order by post_num desc limit 1);");
				while(rs.next()) {
				title = (rs.getString("post_title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				data_Close();
		}
			return title;
	}
	public String review_next(int num) {
		data_Connec();
		String title = "";
			try {
				ResultSet rs = stmt.executeQuery("select post_title from bulletin_board_review where post_num = (select post_num from bulletin_board_review where post_num >" + num + " order by post_num asc limit 1);");
				while(rs.next()) {
				title = (rs.getString("post_title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
				data_Close();
		}
			return title;
	}
	public Writer before_after(String title) {
		data_Connec();
			Writer obj = new Writer();
			try {
				ResultSet rs = stmt.executeQuery("select post_num, post_readcount from bulletin_board_review where post_title='" + title + "';");
				while(rs.next()) {
					 obj.setPost_num(rs.getInt("post_num"));
					 obj.setPost_readcount(rs.getInt("post_readcount"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				data_Close();
			}
			return obj;
		}
	public ArrayList<Writer> review_search(String val, String str) {
		data_Connec();
		ArrayList<Writer> arr = new ArrayList();
			if(str.equals("post_title")) {
				try {
					ResultSet rs = stmt.executeQuery("select DATE_FORMAT(post_date, '%y.%m.%d') as post_date, post_category, post_readcount, post_like, post_num ,post_file, post_title, post_travel_location, post_rating, user_id from bulletin_board_review where post_title like '%" + val + "%' order by post_num desc;");
					while(rs.next()) {
						Writer wr = new Writer();
						wr.setPost_num(rs.getInt("post_num"));
						wr.setPost_category(rs.getString("post_category"));
						wr.setPost_readcount(rs.getInt("post_readcount"));
						wr.setPost_like(rs.getInt("post_like"));
						wr.setPost_title(rs.getString("post_title"));
						wr.setPost_date(rs.getString("post_date"));
						wr.setPost_file(rs.getString("post_file"));
						wr.setPost_travel_location(rs.getString("post_travel_location"));
						wr.setPost_rating(rs.getDouble("post_rating"));
						wr.setUser_id(rs.getString("user_id"));
						arr.add(wr);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					data_Close();
				}
			} else {
				try {
					ResultSet rs = stmt.executeQuery("select DATE_FORMAT(post_date, '%y.%m.%d') as post_date, post_category, post_readcount, post_like, post_num ,post_file, post_title, post_travel_location, post_rating, user_id from bulletin_board_review where " + str + " ='" + val + "' order by post_num desc;");
					while(rs.next()) {
						Writer wr = new Writer();
						wr.setPost_num(rs.getInt("post_num"));
						wr.setPost_category(rs.getString("post_category"));
						wr.setPost_readcount(rs.getInt("post_readcount"));
						wr.setPost_like(rs.getInt("post_like"));
						wr.setPost_title(rs.getString("post_title"));
						wr.setPost_date(rs.getString("post_date"));
						wr.setPost_file(rs.getString("post_file"));
						wr.setPost_travel_location(rs.getString("post_travel_location"));
						wr.setPost_rating(rs.getDouble("post_rating"));
						wr.setUser_id(rs.getString("user_id"));
						arr.add(wr);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					data_Close();
				}
			}
			return arr;
		}
}
