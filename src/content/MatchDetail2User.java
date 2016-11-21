package content;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
      String mathOverView = new String(), slotsRadiant = new String(), slotsDire = new String(), perks = new String();
      
      // ������ ������ ������� � ����
      Connection connect = null;
      try {
        // ��������� �������
        Class.forName("org.postgresql.Driver");
        // System.out.println("������� ���������");
        // ������ ����������
        connect = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Dota2stat", "postgres", "qwerty");
        // System.out.println("���������� �����������");
        PreparedStatement statementMatch = null, statementSlot = null;
        statementMatch = connect.prepareStatement("select * from matches where match_id = ?");
        statementSlot = connect
            .prepareStatement("select * from all_player_slots where match_id = ? order by slot_number");
        ResultSet rs = null, rs1 = null;
        statementMatch.setLong(1, id);
        statementSlot.setLong(1, id);
        rs = statementMatch.executeQuery();
        rs1 = statementSlot.executeQuery();
        if (rs.next()) {
          mathOverView += "<table width = 100%><th>Match number</th><th>Duration</th><th>Game mode</th><th>Date</th><tr align = \"center\"><td>"
              + rs.getLong(1) + "</td><td>" + rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60 + "</td><td>" + rs.getInt(3)
              + "</td><td>" + "</td><td></table>";
        }
        int num;
        while (rs1.next()) {
          num = rs1.getInt(4);
          if (num < 5)
            slotsRadiant += ("<div ><table width = 50%, border = 2px>" + "<tr align = \"center\"><td width = 30%>"
                + rs1.getInt(2) + "</td><td width = 30%>" + rs1.getLong(3)
                + " </td><td><table><tr align = \"center\"><td>" + rs1.getLong(5) + "</td><td>" + rs1.getLong(6)
                + "</td><td>" + rs1.getLong(7) + "</td></tr></table></td><td width = 10%>"
                + (rs1.getLong(14) / (rs.getInt(2) / 60)) + "</td></tr><tr align = \"center\"><td>"
                + "<table><tr align = \"center\"><td>" + rs1.getLong(8) + "</td><td>" + rs1.getLong(9) + "</td><td>"
                + rs1.getLong(10) + "</td></tr>" + "<tr align = \"center\"><td>" + rs1.getLong(11) + "</td><td>"
                + rs1.getLong(12) + "</td><td>" + rs1.getLong(13) + "</td></tr></table>" + "</td><td><table><tr><td>"
                + rs1.getLong(19) + "</td></tr><tr><td>" + rs1.getLong(20) + "</td></tr></table></td><td>"
                + rs1.getInt(21) + "</td><td><table><tr><td>" + (rs1.getLong(16) / (rs.getInt(2) / 60))
                + "</td></tr><tr><td>" + new BigDecimal((rs1.getLong(18) / (rs.getInt(2) / 60f)))
                    .setScale(1, RoundingMode.HALF_UP).floatValue()
                + "</td></tr></table></td></tr>" + "</table></div>");
          if (num > 127)
            slotsDire += ("<div ><table width = 50%, border = 2px>" + "<tr align = \"center\"><td width = 10%>"
                + (rs1.getLong(14) / (rs.getInt(2) / 60)) + "</td><td width = 30%><table><tr align = \"center\"><td>"
                + rs1.getLong(5) + "</td><td>" + rs1.getLong(6) + "</td><td>" + rs1.getLong(7)
                + "</td></tr></table></td><td width = 30%>" + rs1.getLong(3) + " </td><td>" + rs1.getInt(2)
                + "</td></tr>" + "<tr align = \"center\"><td><table><tr><td>" + (rs1.getLong(16) / (rs.getInt(2) / 60))
                + "</td></tr><tr><td>"
                + new BigDecimal((rs1.getLong(18) / (rs.getInt(2) / 60f))).setScale(1, RoundingMode.HALF_UP)
                    .floatValue()
                + "</td></tr></table></td><td>" + rs1.getInt(21) + "</td><td><table><tr><td>" + rs1.getLong(19)
                + "</td></tr><tr><td>" + rs1.getLong(20) + "</td></tr></table></td><td>"
                + "<table><tr align = \"center\"><td>" + rs1.getLong(8) + "</td><td>" + rs1.getLong(9) + "</td><td>"
                + rs1.getLong(10) + "</td></tr>" + "<tr align = \"center\"><td>" + rs1.getLong(11) + "</td><td>"
                + rs1.getLong(12) + "</td><td>" + rs1.getLong(13) + "</td></tr></table>" + "</td>" + "</tr>"
                + "</table></div>");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      PrintWriter out = response.getWriter();
      out.write("<!DOCTYPE html>\n" + "<html>\n" + "<head><meta charset=\"windows-1251\"><style type = \"text/css\">"
          + ".layer1, .layer2 {background: #F2EFE6; border: 1px solid #B25538; padding: 10px; margin: 20px;},"
          + ".radiant{float: left},.dire{float: right}</style><title>Details for Match</title></head>\n"
          + "<body bgcolor=\"#fdf5e6\"><div class=\"layer1\">" + mathOverView + "</div>"
          + "<p><div class = \"radiant\">" + slotsRadiant + "</div></p>" + "<div class = \"dire\">" + slotsDire
          + "</div>" + "</body></html>");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }
  
}
