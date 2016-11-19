package content;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.swing.text.CountingPrintable;

/**
 * Servlet implementation class MatchDetail2User
 */
@WebServlet("/MatchDetail2User")
public class MatchDetail2User extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  public MatchDetail2User() {
    super();
    // TODO Auto-generated constructor stub
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    try {
      // long id = Long.valueOf(request.getParameter("id"));
      long id = 1762832017L;
      String mathOverView = new String(), slots = new String(), perks = new String();
      
      // создаём пустой коннект к базе
      Connection connect = null;
      try {
        // Загружаем драйвер
        Class.forName("org.postgresql.Driver");
        // System.out.println("Драйвер подключен");
        // Создаём соединение
        connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Dota2stat", "postgres", "qwerty");
        // System.out.println("Соединение установлено");
        PreparedStatement statementMatch = null, statementSlot = null;
        statementMatch = connect.prepareStatement("select * from matches where match_id = ?");
        statementSlot = connect
            .prepareStatement("select * from all_player_slots where match_id = ? order by slot_number");
        ResultSet rs = null, rs1 = null;
        statementMatch.setLong(1, id);
        statementSlot.setLong(1, id);
        rs = statementMatch.executeQuery();
        rs1 = statementSlot.executeQuery();
        while (rs.next()) {
          mathOverView += "<table><th>Номер матча</th><th>Длительность</th><th>Режим</th><th>Дата</th><tr><td>"
              + rs.getLong(1) + "</td><td>" + rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60 + "</td><td>" + rs.getInt(3)
              + "</td><td>" + "</td><td>";
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      PrintWriter out = response.getWriter();
      out.write("<!DOCTYPE html>\n" + "<html>\n" + "<head><meta charset=\"windows-1251\"><title>Details for Match</title></head>\n"
          + "<body bgcolor=\"#fdf5e6\">\n" + mathOverView + "</body></html>");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }
  
}
