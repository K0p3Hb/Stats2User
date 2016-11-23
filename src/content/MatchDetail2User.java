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
      String mathOverView = new String(), perks = new String();
      StringBuilder  slotsDire = new StringBuilder(),slotsRadiant = new StringBuilder();
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
        // первая зона - общая информация о матче
        if (rs.next()) {
          mathOverView += "<table width = 100%><th>Match number</th><th>Duration</th><th>Game mode</th><th>Date</th><tr align = \"center\"><td>"
              + rs.getLong(1) + "</td><td>" + rs.getInt(2) / 60 + ":" + rs.getInt(2) % 60 + "</td><td>" + rs.getInt(3)
              + "</td><td>" + "</td><td></table>";
        }
        int num;

        // заполняем слоты игроков
        while (rs1.next()) {
          num = rs1.getInt(4);
          // команда светлых
          if (num < 5){      
            slotsRadiant.append("<div>");
              slotsRadiant.append("<table border = 2px, cellspacing = 5px, width = 100%>");
                slotsRadiant.append("<tr align = \"center\">");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append(rs1.getInt(2)); //герой
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append(rs1.getLong(3)); //аккаунт
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 30%>");
                    slotsRadiant.append("<table>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(5));//убийства
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(6));//смерти
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(7));//ассисты
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td width = 10%>");
                    slotsRadiant.append(rs1.getLong(14)/(rs.getInt(2)/60));//урон героям в минуту
                  slotsRadiant.append("</td>");
                slotsRadiant.append("</tr>");
                slotsRadiant.append("<tr align = \"center\">");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table border = 1px, width = 100%>");//вещи в инвентаре героя (6штук)
                      slotsRadiant.append("<tr>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(8));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(9));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(10));
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(11));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(12));
                        slotsRadiant.append("</td>");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(13));
                        slotsRadiant.append("</td>");
                    slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table width = 100%>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(19));//золото за весь матч
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                        slotsRadiant.append(rs1.getLong(20));//опыт за весь матч
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append(rs1.getInt(21) + "%");//% участия в убийствах команды
                  slotsRadiant.append("</td>");
                  slotsRadiant.append("<td>");
                    slotsRadiant.append("<table width = 100%>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(rs1.getLong(16)/(rs.getInt(2)/60));
                        slotsRadiant.append("</td>");
                      slotsRadiant.append("</tr>");
                      slotsRadiant.append("<tr align = \"center\">");
                        slotsRadiant.append("<td>");
                          slotsRadiant.append(new BigDecimal((rs1.getLong(18)/(rs.getInt(2)/60f))) .setScale(1, RoundingMode.HALF_UP).floatValue());
                        slotsRadiant.append("</td>");
                    slotsRadiant.append("</tr>");
                    slotsRadiant.append("</table>");
                  slotsRadiant.append("</td>");
                slotsRadiant.append("</tr>");
              slotsRadiant.append("</table>");
            slotsRadiant.append("</div");
          }
            /*
             * slotsRadiant += ("<div ><table border = 2px>" +
             * "<tr align = \"center\"><td width = 30%>" + rs1.getInt(2) +
             * "</td><td width = 30%>" + rs1.getLong(3) +
             * " </td><td><table><tr align = \"center\"><td>" + rs1.getLong(5) +
             * "</td><td>" + rs1.getLong(6) + "</td><td>" + rs1.getLong(7) +
             * "</td></tr></table></td><td width = 10%>" + (rs1.getLong(14) /
             * (rs.getInt(2) / 60)) + "</td></tr><tr align = \"center\"><td>" +
             * "<table><tr align = \"center\"><td>" + rs1.getLong(8) +
             * "</td><td>" + rs1.getLong(9) + "</td><td>" + rs1.getLong(10) +
             * "</td></tr>" + "<tr align = \"center\"><td>" + rs1.getLong(11) +
             * "</td><td>" + rs1.getLong(12) + "</td><td>" + rs1.getLong(13) +
             * "</td></tr></table>" + "</td><td><table><tr><td>" +
             * rs1.getLong(19) + "</td></tr><tr><td>" + rs1.getLong(20) +
             * "</td></tr></table></td><td>" + rs1.getInt(21) +
             * "</td><td><table><tr><td>" + (rs1.getLong(16) / (rs.getInt(2) /
             * 60)) + "</td></tr><tr><td>" + new BigDecimal((rs1.getLong(18) /
             * (rs.getInt(2) / 60f))) .setScale(1,
             * RoundingMode.HALF_UP).floatValue() +
             * "</td></tr></table></td></tr>" + "</table></div>");
             */
            // команда тёмных
            if (num > 127){
              slotsDire.append("<div>");
              slotsDire.append("<table border = 2px, cellspacing = 5px, width = 100%>");
                slotsDire.append("<tr align = \"center\">");
                  slotsDire.append("<td width = 10%>");
                    slotsDire.append(rs1.getLong(14)/(rs.getInt(2)/60));//урон героям в минуту
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append("<table>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(5));//убийства
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(6));//смерти
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(7));//ассисты
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append(rs1.getLong(3)); //аккаунт
                  slotsDire.append("</td>");
                  slotsDire.append("<td width = 30%>");
                    slotsDire.append(rs1.getInt(2)); //герой
                  slotsDire.append("</td>");
                slotsDire.append("</tr>");
                slotsDire.append("<tr align = \"center\">");
                  slotsDire.append("<td>");
                    slotsDire.append("<table width = 100%>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(16)/(rs.getInt(2)/60));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(new BigDecimal((rs1.getLong(18)/(rs.getInt(2)/60f))) .setScale(1, RoundingMode.HALF_UP).floatValue());
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");
                  slotsDire.append("<td>");
                    slotsDire.append(rs1.getInt(21) + "%");//% участия в убийствах команды
                  slotsDire.append("</td>");  
                  slotsDire.append("<td>");
                    slotsDire.append("<table width = 100%>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(19));//золото за весь матч
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr align = \"center\">");
                        slotsDire.append("<td>");
                            slotsDire.append(rs1.getLong(20));//опыт за весь матч
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>");  
                  slotsDire.append("<td>");
                    slotsDire.append("<table border = 1px, width = 100%>");//вещи в инвентаре героя (6штук)
                      slotsDire.append("<tr>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(8));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(9));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(10));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                      slotsDire.append("<tr>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(11));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(12));
                        slotsDire.append("</td>");
                        slotsDire.append("<td>");
                          slotsDire.append(rs1.getLong(13));
                        slotsDire.append("</td>");
                      slotsDire.append("</tr>");
                    slotsDire.append("</table>");
                  slotsDire.append("</td>"); 
                slotsDire.append("</tr>");
              slotsDire.append("</table>");
            slotsDire.append("</div");
            }
             /* slotsDire += ("<div ><table  border = 2px, cellspacing = 5px, width = 100%>" + "<tr align = \"center\"><td width = 10%>"
                  + (rs1.getLong(14) / (rs.getInt(2) / 60)) + "</td><td width = 30%><table><tr align = \"center\"><td>"
                  + rs1.getLong(5) + "</td><td>" + rs1.getLong(6) + "</td><td>" + rs1.getLong(7)
                  + "</td></tr></table></td><td width = 30%>" + rs1.getLong(3) + " </td><td>" + rs1.getInt(2)
                  + "</td></tr>" + "<tr align = \"center\"><td><table><tr><td>"
                  + (rs1.getLong(16) / (rs.getInt(2) / 60)) + "</td></tr><tr><td>"
                  + new BigDecimal((rs1.getLong(18) / (rs.getInt(2) / 60f))).setScale(1, RoundingMode.HALF_UP)
                      .floatValue()
                  + "</td></tr></table></td><td>" + rs1.getInt(21) + "</td><td><table><tr><td>" + rs1.getLong(19)
                  + "</td></tr><tr><td>" + rs1.getLong(20) + "</td></tr></table></td><td>"
                  + "<table><tr align = \"center\"><td>" + rs1.getLong(8) + "</td><td>" + rs1.getLong(9) + "</td><td>"
                  + rs1.getLong(10) + "</td></tr>" + "<tr align = \"center\"><td>" + rs1.getLong(11) + "</td><td>"
                  + rs1.getLong(12) + "</td><td>" + rs1.getLong(13) + "</td></tr></table>" + "</td>" + "</tr>"
                  + "</table></div>");
              */
        }

      String caption = new String();
      if(rs.getBoolean(4))
        caption = "<td>Radiant <b><i>WIN</i></b></td><td>Dire<td></td>";
      else
        caption = "<td>Radiant></td><td>Dire <b><i>WIN</i></b<td></td>";
      PrintWriter out = response.getWriter();
      out.write("<!DOCTYPE html>\n" + "<html>\n" + "<head><meta charset=\"UTF-8\"><style type = \"text/css\">"
          + ".layer1, .layer2 {background: #F2EFE6; border: 1px solid #B25538; padding: 10px; margin: 20px;},"
          + ".radiant{float: left},.dire{float: right}</style><title>Details for Match</title></head>\n"
          + "<body bgcolor=\"#fdf5e6\"><div class=\"layer1\">" + mathOverView + "</div><div>"
          // + "<p><div class = \"radiant\">" + slotsRadiant + "</div></p>" +
          // "<div class = \"dire\">" + slotsDire
          + "<table width = 800px, align = \"center\"><tr  align = \"center\">" + caption + "</tr>"
          + "<tr><td>" + slotsRadiant +"</td><td>" + slotsDire
          + "</td></tr></table>"
          + "</div>" + "</body></html>");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(request, response);
  }
  
}
