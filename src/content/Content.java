package content;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import org.postgresql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Content
 */
@WebServlet("/Content")
public class Content extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  public Content() {
    super();
    // TODO Auto-generated constructor stub
}
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
    
    String allMathes = new String();  
    
 // создаём пустой коннект к базе
    Connection connect = null;
    try {
      // Загружаем драйвер
      Class.forName("org.postgresql.Driver");
      // System.out.println("Драйвер подключен");
      // Создаём соединение
      connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Dota2stat", "postgres", "qwerty");
      // System.out.println("Соединение установлено");
      java.sql.Statement statement = null;
      statement = connect.createStatement();
      ResultSet rs = null;
      rs = statement.executeQuery("select match_id from matches");
      while(rs.next()){
        allMathes += "<A href = \"/MatchDetail2User?" + rs.getLong(1) + "\">" + rs.getLong(1) + "\n</br>";
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    
    PrintWriter out = response.getWriter();
    out.write("<!DOCTYPE html>\n" +
       "<html>\n" +
       "<head><title>A servlet v3.</title></head>\n" +
       "<body bgcolor=\"#fdf5e6\">\n" +"<td>"+
       "<h1>Note:</h1>\n" +
       "<p>This whole page was created via servlet v3</p>\n" +"</td>" +
       allMathes+
       "</body></html>");
  }  
}
