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
      long id = Long.valueOf(request.getParameter("id"));
      // long id = 1762832017L;
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
          mathOverView += "<table width = 100%><th>Match number</th><th>Duration</th><th>Game mode</th><th>Date</th><tr align = \"center\"><td>"
              + rs.getLong(1) + "</td><td>" + rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60 + "</td><td>" + rs.getInt(3)
              + "</td><td>" + "</td><td></table>";
        }
        rs1.next();
        slots = "<div ><table width = 50%>"
            + "<td width = 25%>"
            + "<tr>" + rs1.getInt(2) + "</tr>"
            + "<tr><td>" + rs1.getInt(8)  + "</td><td>" + rs1.getInt(9) + "</td><td>" + rs1.getInt(10) + "</td></tr>"
            + "<tr><td>" + rs1.getInt(11) + "</td><td>" + rs1.getInt(12) + "</td><td>" + rs1.getInt(13) + "</td></tr>"
            + "</td>" 
            + "<td width = 25%>"
            + "<tr> " + rs1.getLong(3) + " </tr><tr> " + rs1.getLong(19) + " </tr><tr> " + rs1.getLong(19) + " </tr>"
            + "</td>"
            + "</table></div>";
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      PrintWriter out = response.getWriter();
      out.write("<!DOCTYPE html>\n" + "<html>\n"
          + "<head><meta charset=\"windows-1251\"><style type = \"text/css\">.layer1, .layer2 {background: #F2EFE6; border: 1px solid #B25538; padding: 10px; margin: 20px;}</style><title>Details for Match</title></head>\n"
          + "<body bgcolor=\"#fdf5e6\"><div class=\"layer1\">" + mathOverView + "</div>" + "<div class=\"layer2\">"
          + slots + "</div>" + "</body></html>");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }
  
}
